package com.android.dogefoodie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.R;
import com.android.dogefoodie.database.CartDB;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class User_Checkout_Adapter extends RecyclerView.Adapter<User_Checkout_Adapter.ViewHolder>{
    private List<CartItem> cartItems;
    private CartDB cartDB;
    private Context context;

    public User_Checkout_Adapter(List<CartItem> cartItems, CartDB cartDB, Context context) {
        this.cartItems = cartItems;
        this.cartDB = cartDB;
        this.context = context;
    }

    @NonNull
    @Override
    public User_Checkout_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_Checkout_Adapter.ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.itemName.setText(item.getProductName());
        holder.itemPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));

        String imageUrl = item.getImageUrl();
        if (imageUrl != null && imageUrl.startsWith("/")) {
            Picasso.get()
                    .load(new File(imageUrl))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.itemImage);
        } else {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.orderImg);
            itemName = itemView.findViewById(R.id.orderName);
            itemPrice = itemView.findViewById(R.id.orderPrice);
            itemQuantity = itemView.findViewById(R.id.orderQuantity);
        }
    }
}
