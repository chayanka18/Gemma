package com.example.gem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gem.Model.Cart;
import com.example.gem.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
public class AdminUsersProducts extends AppCompatActivity {
    private RecyclerView productsList;
    private RecyclerView.LayoutManager layoutManager;
    private String currentUserId;
    private String userId = "";
    private String orderId = "";

    private DatabaseReference cartListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_products);

        // Get the user ID and order ID from the intent
        userId = getIntent().getStringExtra("uid");
        orderId = getIntent().getStringExtra("orderID");

        //currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        // Update the cartListRef to point to the specific user's products
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child("Users")
                .child(userId)
                .child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Quantity: " + model.getQuantity());
                holder.txtProductName.setText("Name: " + model.getPname());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.productImage);
                holder.txtProductDate.setText(model.getDate());
                holder.txtProductTime.setText(model.getTime());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_history_layout, parent, false);
                return new CartViewHolder(view);
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}

/*public class AdminUsersProducts extends AppCompatActivity
{
private RecyclerView productsList;
RecyclerView.LayoutManager layoutManager;
String currentUserId;
String userId="";
    String orderId="";

private DatabaseReference cartListRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_products);
//        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderId = getIntent().getStringExtra("orderID");
        userId=getIntent().getStringExtra("uid");
        productsList=findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        cartListRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child("Users")
                .child(userId)
                .child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef,Cart.class)
                                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {
                holder.txtProductQuantity.setText("Quantity :"+model.getQuantity());
                holder.txtProductName.setText("Name :"+model.getPname());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.productImage);
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };
        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}*/