package com.example.gem.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gem.Interface.ItemClickListener;
import com.example.gem.Model.Cart;
import com.example.gem.R;
import com.squareup.picasso.Picasso;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductTime,txtProductDate;
    public ImageView productImage;
    private ItemClickListener itemClickListener;



    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
        productImage=itemView.findViewById(R.id.cart_product_image);
        txtProductTime=itemView.findViewById(R.id.cart_product_time);
        txtProductDate=itemView.findViewById(R.id.cart_product_date);

    }
    @Override
    public void onClick(View v)
    {
    itemClickListener.onClick(v,getLayoutPosition(),false);

    }



}
