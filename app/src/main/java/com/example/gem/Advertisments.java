package com.example.gem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gem.Model.Products;
import com.example.gem.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Advertisments extends AppCompatActivity {
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_home);
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

    }

    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                /*.setQuery(ProductRef, Products.class)
                .build();*/
                .setQuery(ProductRef.orderByChild("category").startAt("nose pins").endAt("earrings"), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                if (model.getCategory().equals("nose pins")) {
                    holder.txtProductName.setText(model.getPname());
                    holder.txtProductDescription.setText(model.getDescription());
                    holder.txtProductPrice.setText("Price = " + model.getPrice());
                    Picasso.get().load(model.getImage()).into(holder.imageView);
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout, parent, false);
                return new ProductViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
       /* FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef.orderByChild("category").equalTo("rings"), Products.class)
                // .setQuery(ProductRef.orderByChild("category").equalTo("earrings"),Products.class)
                .build();
                /*.setQuery(ProductRef,Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
            {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price =" + model.getPrice() + "Rs.");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Advertisments.this,ProductDetails.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

      /*  Query query1 = ProductRef.orderByChild("category").equalTo("sets");
        Query query2 = ProductRef.orderByChild("category").equalTo("earrings");

        FirebaseRecyclerOptions<Products> options1 = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query1, Products.class)
                .build();

        FirebaseRecyclerOptions<Products> options2 = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query2, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter1 = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                // Set the data to the views for category "rings"
                holder.txtProductName.setText(model.getPname());
               // holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + "Rs");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Advertisments.this, ProductDetails.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout, parent, false);
                return new ProductViewHolder(view);
            }
        };

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter2 = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                // Set the data to the views for category "earrings"
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + " Rs.");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Advertisments.this, ProductDetails.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item_layout, parent, false);
                return new ProductViewHolder(view);
            }
        };

        FirebaseRecyclerAdapter<Products, ProductViewHolder> mergedAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(
                options1) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                if (position < adapter1.getItemCount()) {
                    adapter1.onBindViewHolder(holder, position, (List<Object>) model);
                } else {
                    adapter2.onBindViewHolder(holder, position - adapter1.getItemCount(), (List<Object>) model);
                }
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return viewType == adapter1.getItemViewType(viewType) ?
                        adapter1.onCreateViewHolder(parent, viewType) :
                        adapter2.onCreateViewHolder(parent, viewType - adapter1.getItemCount());
            }
        };

        recyclerView.setAdapter(mergedAdapter);

        adapter1.startListening();
        adapter2.startListening();



    }
}*/