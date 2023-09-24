package com.example.gem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gem.Model.Cart;
import com.example.gem.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn,continueBtn;
    private TextView txtTotalAmount,txtMsg1;
    private int  overTotalPrice=0;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn=(Button)findViewById(R.id.next_process_btn);
        txtTotalAmount=(TextView)findViewById(R.id.total_price);
        txtMsg1=findViewById(R.id.msg1);
        continueBtn=findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(CartActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        });
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (overTotalPrice > 0) {
                    txtTotalAmount.setText("Total Price : " + String.valueOf(overTotalPrice));
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(CartActivity.this);
                    alert.setTitle("PAYMENT");
                    alert.setIcon(R.drawable.baseline_payment_24);
                    alert.setMessage("How do you like to make the payment?")
                            .setPositiveButton("COD", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("Online", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOnlineOrder.class);
                                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    androidx.appcompat.app.AlertDialog alert1 = alert.create();
                    alert1.show();
                }
                else
                {
                    Toast.makeText(CartActivity.this, "OOPS!! Seems like your Cart is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void CheckOrderState()
    {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderRef;
        orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("Users View").child("Users").child(currentUserId);
        orderRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();
                    String username = snapshot.child("pname").getValue().toString();

                    if(shippingState.equals("Shipped"))
                    {
                        txtTotalAmount.setText("Dear" + username + "\n Order has been placed Successfully");
                        NextProcessBtn.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setText("Congratulations,Order has been Shipped Successfully.Soon you will receive your order");
                        txtMsg1.setVisibility(View.VISIBLE);
                        // Toast.makeText(CartActivity.this, "You can purchase more products\n once your final order has been recieved", Toast.LENGTH_SHORT).show();

                    }
                    else if(shippingState.equals("Not Shipped"))
                    {
                        txtTotalAmount.setText("Shipping State : "+"Not Shipped");
                        boolean b = false;
                        NextProcessBtn.setEnabled(b);
                        NextProcessBtn.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        Toast.makeText(CartActivity.this, " Your Order is in review ", Toast.LENGTH_LONG).show();
                       /* Intent intent=new Intent(CartActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child("Users")
                                .child(currentUserId).child("Products"),Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText("Quantity :"+model.getQuantity());
                holder.txtProductName.setText("Name :"+model.getPname());
                holder.txtProductPrice.setText(model.getPrice());
                //holder.setImage(model.getImage());

                Picasso.get().load(model.getImage()).into(holder.productImage);

                String priceString = model.getPrice().replaceAll("[^\\d]", "");
                int oneTypeProductPrice = Integer.parseInt(priceString) * Integer.parseInt(model.getQuantity());

                //int oneTypeProductPrice=((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getQuantity());
                overTotalPrice=overTotalPrice+oneTypeProductPrice;


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[]=new CharSequence[]
                                {"Edit","Delete"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0)
                                {
                                    Intent i=new Intent(CartActivity.this,ProductDetails.class);
                                    i.putExtra("pid",model.getPid());
                                    startActivity(i);
                                }
                                if(which==1)
                                {
                                    cartListRef.child("User View")
                                            .child("Users")
                                            .child(currentUserId)
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {
                                                        /*cartListRef.child("Admin View")
                                                                .child("Users")
                                                                .child(currentUserId)// Replace with the actual user ID of the current user
                                                                .child("Products")
                                                                .child(model.getPid())
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                    {
                                                                        // Toast.makeText(UserRecentSum.this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });*/
                                                        Toast.makeText(CartActivity.this, "Item has been removed from the cart", Toast.LENGTH_LONG).show();
                                                        Intent i=new Intent(CartActivity.this,CartActivity.class);
                                                        i.putExtra("pid",model.getPid());
                                                        startActivity(i);
                                                       finish();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void onBackPressed()
    {
        Intent i=new Intent(CartActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}