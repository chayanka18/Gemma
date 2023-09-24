package com.example.gem;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gem.Model.Products;
import com.example.gem.Model.Review;
import com.example.gem.Model.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProductDetails extends AppCompatActivity {
    private Button addToCart;
    private ImageView productImage;
    private TextView productPrice, productDescription;
    private TextView productName;
    private String productID = "",state="Normal";

    private ImageView wishlist;
    private Spinner spin;

    // Review
    private EditText editTextReview;
    private RatingBar ratingBar;
    private Button buttonSubmit,buyNow;
    private ListView listViewReviews;
    private DatabaseReference databaseRef;
    private RecyclerView recyclerView;
    private TextView averageRatingTextView,editTextUsername ;
    private ListView productReviewsListView;
    private String currentUserId;
    private List<Review> reviewList;
    private ReviewAdapter reviewAdapter;
   // private int pprice=0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        productID = getIntent().getStringExtra("pid");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        wishlist=findViewById(R.id.add_to_wishlist);
        addToCart = findViewById(R.id.pd_add_to_cart_button);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productPrice = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_description_details);

        spin=findViewById(R.id.spinner1);
        String[] quantity={"1","2","3","4","5"};
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,quantity);
        spin.setAdapter(adapter);

        // Initialize views for review
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextReview = findViewById(R.id.editTextReview);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        // listViewReviews = findViewById(R.id.listViewReviews);
        averageRatingTextView=findViewById(R.id.averageRatingTextView);
        productReviewsListView=findViewById(R.id.productReviewsListView);

        // Initialize the Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("ProductReviews");

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(ProductDetails.this, reviewList);
        productReviewsListView.setAdapter(reviewAdapter);

        // Set the click listener for the submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });

        // Display the reviews in the list view
        displayReviews();

        getProductDetails(productID);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToWishList(currentUserId);
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    //Toast.makeText(ProductDetails.this, "You can purchase more products once your order is\n shipped or confirmed", Toast.LENGTH_LONG).show();
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(ProductDetails.this);
                    alert.setTitle("ALERT");
                    alert.setIcon(R.drawable.baseline_warning_24);
                    alert.setMessage("You can purchase more products once your order is shipped or confirmed")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(ProductDetails.this, "Please Wait", Toast.LENGTH_SHORT).show();
                                }
                            });

                    androidx.appcompat.app.AlertDialog alert1 = alert.create();
                    alert1.show();
                }
                else {
                    Intent i=new Intent(ProductDetails.this,CartActivity.class);
                    startActivity(i);
                    finish();
                    addingToCartList(currentUserId);
                }
            }
        });
        String userId = FirebaseAuth.getInstance().getUid(); // Replace with the actual user ID
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Document exists, retrieve the data
                            // String emaily = documentSnapshot.getString("email");
                            String usernamey = documentSnapshot.getString("username");

                            // Do something with the retrieved email and username
                            // For example, set them in TextViews
                            editTextUsername.setText("UserName :"+ usernamey);
                        } else {
                            // Document does not exist
                            // Handle the case appropriately
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while retrieving the document
                        // Handle the failure appropriately
                    }
                });
    }

    private void addingToWishList(String currentUserId)
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("inputUser",currentUserId);
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity",spin.getSelectedItem().toString());
        cartMap.put("discount", "");
        //String pprice=productPrice.getText().toString();

        cartListRef.child("User View")
                .child("Users")
                .child(currentUserId)// Replace with the actual user ID of the current user
                .child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductDetails.this, "Product added to WishList successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetails.this, "Failed to add product to WishList.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void submitReview() {
        // Get the user inputs
        String username = editTextUsername.getText().toString();
        String review = editTextReview.getText().toString();
        float rating = ratingBar.getRating();
          if(username.length()==0||review.length()==0)
          {
              Toast.makeText(ProductDetails.this,"Fields cannot be Empty",Toast.LENGTH_SHORT).show();
          }else {
              // Create a new review object
              Review newReview = new Review(rating, review, username);

              // Generate a new unique key for the review
              String reviewId = databaseRef.push().getKey();

              // Store the review in the database using the generated key
              databaseRef.child(reviewId).setValue(newReview)
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              Toast.makeText(ProductDetails.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                              //editTextUsername.setText("");
                              editTextReview.setText("");
                              ratingBar.setRating(0);
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Toast.makeText(ProductDetails.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                          }
                      });
          }
    }
    private void displayReviews() {
        DatabaseReference productReviewsRef = FirebaseDatabase.getInstance().getReference().child("ProductReviews").child(productID);
        productReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null) {
                        reviews.add(review);
                    }
                }
                if (reviews.isEmpty()) {
                  //  Toast.makeText(ProductDetails.this, "No reviews available", Toast.LENGTH_SHORT).show();
                } else {
                    reviewList.clear();
                    reviewList.addAll(reviews);
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductDetails.this, "Failed to retrieve reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void displayReviews() {
        DatabaseReference productReviewsRef = FirebaseDatabase.getInstance().getReference().child("ProductReviews").child(productID);
        productReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> reviews = new ArrayList<>();
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null) {
                        String username = review.getUsername();
                        String reviewText = review.getReview();
                        float rating = review.getRating();
                        reviews.add(username + ": " + reviewText + " (Rating: " + rating + ")");
                    }
                }
                if (reviews.isEmpty()) {
                    Toast.makeText(ProductDetails.this, "No reviews available", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Reviews", reviews.toString()); // Log the retrieved reviews for debugging
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductDetails.this, android.R.layout.simple_list_item_1, reviews);
                    productReviewsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductDetails.this, "Failed to retrieve reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }*/




    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList(String currentUserId)
    {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("inputUser",currentUserId);
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity",spin.getSelectedItem().toString());
        cartMap.put("discount", "");

        cartListRef.child("User View")
                .child("Users")
                .child(currentUserId)// Replace with the actual user ID of the current user
                .child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View")
                                    .child("Users")
                                    .child(currentUserId)// Replace with the actual user ID of the current user
                                    .child("Products")
                                    .child(productID)
                                    .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    Toast.makeText(ProductDetails.this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                        }
                    }
                });
    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
               /* Products products = snapshot.getValue(Products.class);
                if (products != null) {
                    productName.setText(products.getPname());
                    productPrice.setText("Rs " + products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }*/
                productReviewsListView.setAdapter(null);
                Products products = snapshot.getValue(Products.class);
                if (products != null)
                {
                    productName.setText(products.getPname());
                    productPrice.setText("Rs " + products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                    String price=products.getPrice();

                    // Display average rating
                    float averageRating = calculateAverageRating(snapshot.child("reviews"));
                    averageRatingTextView.setText("Average Rating: " + averageRating);

                    // Display reviews
                    List<Review> reviewList = new ArrayList<>();
                    for (DataSnapshot reviewSnapshot : snapshot.child("reviews").getChildren())
                    {
                        Review review = reviewSnapshot.getValue(Review.class);
                        if (review != null)
                        {
                            reviewList.add(review);
                        }
                    }
                    ReviewAdapter reviewAdapter = new ReviewAdapter(ProductDetails.this, reviewList);
                    productReviewsListView.setAdapter(reviewAdapter);
                }
                displayReviews();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Handle the error appropriately
            }
        });
      // displayReviews();
    }

    private float calculateAverageRating(DataSnapshot reviewsSnapshot) {
        int totalReviews = 0;
        float totalRating = 0;

        for (DataSnapshot reviewSnapshot : reviewsSnapshot.getChildren()) {
            Review review = reviewSnapshot.getValue(Review.class);
            if (review != null) {
                totalReviews++;
                totalRating += review.getRating();
            }
        }

        if (totalReviews > 0) {
            return totalRating / totalReviews;
        } else {
            return 0;
        }
    }
    private void CheckOrderState()
    {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderRef;
        orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("Users").child(currentUserId);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();
                    if(shippingState.equals("Shipped"))
                    {
                        state="Order Shipped";
                    }
                    else if(shippingState.equals("Not Shipped"))
                    {
                        state="Order Placed";
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


