package com.android.dogefoodie.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.database.CartDB;
import com.android.dogefoodie.database.ProductDB;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class User_Cart_Adapter extends RecyclerView.Adapter<User_Cart_Adapter.ViewHolder> {

    private List<CartItem> cartItems;
    private CartDB cartDB;
    private Context context;
    private TotalPriceUpdater totalPriceUpdater;
    private ProductDB productDB;

    public User_Cart_Adapter(List<CartItem> cartItems, CartDB cartDB, Context context, TotalPriceUpdater totalPriceUpdater) {
        this.cartItems = cartItems;
        this.cartDB = cartDB;
        this.context = context;
        this.totalPriceUpdater = totalPriceUpdater;
        this.productDB = new ProductDB(context);
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

        String imageUrl = item.getImageUrl();
        loadImageWithPicasso(holder.itemImage, imageUrl);

        holder.btnIn.setOnClickListener(v -> {
            if (item.getQuantity() < getMaxQuantity(item)) {
                item.setQuantity(item.getQuantity() + 1);
                cartDB.updateCartItemQuantity(item.getUserId(), item.getProductName(), item.getQuantity());
                holder.itemQuantity.setText(String.valueOf(item.getQuantity()));

                if (totalPriceUpdater != null) {
                    totalPriceUpdater.onPriceUpdate(cartItems);
                }
            } else {
                Toast.makeText(context, "Maximum quantity reached", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDe.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartDB.updateCartItemQuantity(item.getUserId(), item.getProductName(), item.getQuantity());
                holder.itemQuantity.setText(String.valueOf(item.getQuantity()));

                if (totalPriceUpdater != null) {
                    totalPriceUpdater.onPriceUpdate(cartItems);
                }
            } else {
                Toast.makeText(context, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnRemove.setOnClickListener(v -> showConfirmDialog(position, item));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void showConfirmDialog(int position, CartItem item) {
        new AlertDialog.Builder(context)
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove " + item.getProductName() + " from the cart?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    cartDB.deleteCartItem(item.getUserId(), item.getProductName());
                    cartItems.remove(position);
                    notifyItemRemoved(position);

                    if (totalPriceUpdater != null) {
                        totalPriceUpdater.onPriceUpdate(cartItems);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadImageWithPicasso(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            if (imageUrl.startsWith("drawable/")) {
                String drawableName = imageUrl.replace("drawable/", "").replace(".jpg", "");
                int drawableResId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
                if (drawableResId != 0) {
                    imageView.setImageResource(drawableResId);
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else if (imageUrl.startsWith("/")) {
                Picasso.get()
                        .load(new File(imageUrl))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            } else {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private int getMaxQuantity(CartItem item) {
        return productDB.getProductQuantity(item.getProductName());
    }

    public interface TotalPriceUpdater {
        void onPriceUpdate(List<CartItem> updatedCartItems);
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
        LinearLayout btnRemove;
        Button btnIn, btnDe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartImg);
            itemName = itemView.findViewById(R.id.cartName);
            itemPrice = itemView.findViewById(R.id.cartPrice);
            itemQuantity = itemView.findViewById(R.id.cartQuantity);
            btnRemove = itemView.findViewById(R.id.remove);
            btnIn = itemView.findViewById(R.id.btnCartIn);
            btnDe = itemView.findViewById(R.id.btnCartDe);
        }
    }
}
