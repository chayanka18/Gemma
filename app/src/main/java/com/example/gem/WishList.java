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
import com.example.gem.ViewHolder.WishListHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WishList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;
    private String currentUserId;
    ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        recyclerView = findViewById(R.id.wishlist_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar=findViewById(R.id.progress_bar);
    }
    private void Loading(Boolean isLoading)
    {
        if(isLoading)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
        @Override
        protected void onStart () {
            super.onStart();
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference WishlistRef = FirebaseDatabase.getInstance().getReference().child("Wish List");
            FirebaseRecyclerOptions<Cart> options =
                    new FirebaseRecyclerOptions.Builder<Cart>()
                            .setQuery(WishlistRef.child("User View")
                                    .child("Users")
                                    .child(currentUserId)
                                    .child("Products"), Cart.class)
                            .build();


            FirebaseRecyclerAdapter<Cart, WishListHolder> adapter = new FirebaseRecyclerAdapter<Cart, WishListHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull WishListHolder holder, int position, @NonNull Cart model) {
                    holder.txtProductQuantity.setText("Quantity :" + model.getQuantity());
                    holder.txtProductName.setText("Name :" + model.getPname());
                    holder.txtProductPrice.setText(model.getPrice());


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[] = new CharSequence[]
                                    {"Edit", "Delete"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(WishList.this);
                            builder.setTitle("WishList Options:");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        Intent i = new Intent(WishList.this, ProductDetails.class);
                                        i.putExtra("pid", model.getPid());
                                        startActivity(i);
                                    }
                                    if (which == 1) {
                                        WishlistRef.child("User View")
                                                .child("Users")
                                                .child(currentUserId)
                                                .child("Products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(WishList.this, "Item has been removed from the WishList", Toast.LENGTH_LONG).show();
                                                            Intent i = new Intent(WishList.this, WishList.class);
                                                            i.putExtra("pid", model.getPid());
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
                public WishListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_items_layout, parent, false);
                    WishListHolder holder = new WishListHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }

