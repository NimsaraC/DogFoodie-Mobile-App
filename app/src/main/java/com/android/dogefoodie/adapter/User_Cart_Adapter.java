package com.android.dogefoodie.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.database.CartDB;
import com.squareup.picasso.Picasso;

import java.util.List;

public class User_Cart_Adapter extends RecyclerView.Adapter<User_Cart_Adapter.ViewHolder> {

    private List<CartItem> cartItems;
    private CartDB cartDB;
    private Context context;

    public User_Cart_Adapter(List<CartItem> cartItems, CartDB cartDB, Context context) {
        this.cartItems = cartItems;
        this.cartDB = cartDB;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.itemName.setText(item.getProductName());
        holder.itemPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        Picasso.get().load(item.getImageUrl()).into(holder.itemImage);

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(position, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void showConfirmDialog(int position, CartItem item) {
        new AlertDialog.Builder(context)
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove " + item.getProductName() + " from the cart?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove from database
                        cartDB.deleteCartItem(item.getUserId(), item.getProductName());

                        // Remove from list and notify adapter
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;
        Button btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartImg);
            itemName = itemView.findViewById(R.id.cartName);
            itemPrice = itemView.findViewById(R.id.cartPrice);
            itemQuantity = itemView.findViewById(R.id.cartQuantity);
            btnRemove = itemView.findViewById(R.id.remove);
        }
    }
}
