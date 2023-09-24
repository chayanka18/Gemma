package com.example.gem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gem.Model.Products;
import com.example.gem.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class All extends AppCompatActivity
{
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;

    private double minPrice = 0;
    private double maxPrice = Double.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_home);
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.progress_bar);
        Button clearfilterButton=findViewById(R.id.clearfilterButton);
        clearfilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilters();
            }
        });
        Button filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });
    }
    private void clearFilters() {
        minPrice = 0;
        maxPrice = Double.MAX_VALUE;
        onStart();
    }
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    protected void onStart() {
        super.onStart();
        setLoading(true);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                setLoading(false);

                double productPrice = Double.parseDouble(model.getPrice());

                if (productPrice >= minPrice && productPrice <= maxPrice) {
                    holder.txtProductName.setText(model.getPname());
                    holder.txtProductPrice.setText("Price Rs. " + model.getPrice());
                    Picasso.get().load(model.getImage()).into(holder.imageView);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
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

    private void showFilterDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Price Range");

        View dialogView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText minPriceEditText = dialogView.findViewById(R.id.minPriceEditText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText maxPriceEditText = dialogView.findViewById(R.id.maxPriceEditText);

        builder.setView(dialogView);
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String minPrice = minPriceEditText.getText().toString().trim();
                String maxPrice = maxPriceEditText.getText().toString().trim();
                if (minPrice.isEmpty() && maxPrice.isEmpty()) {
                    Toast.makeText(All.this, "Please enter a price range", Toast.LENGTH_SHORT).show();
                } else {
                    double min = minPrice.isEmpty() ? 0 : Double.parseDouble(minPrice);
                    double max = maxPrice.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPrice);
                    // Update the price range filter
                    //filterProductsByPriceRange(Double.parseDouble(minPrice), Double.parseDouble(maxPrice));
                    filterProductsByPriceRange(min, max);
                }
                // Update the price range filter
                //filterProductsByPriceRange(Double.parseDouble(minPrice), Double.parseDouble(maxPrice));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void filterProductsByPriceRange(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

        onStart(); // Restart the query with the new price range
    }
}

