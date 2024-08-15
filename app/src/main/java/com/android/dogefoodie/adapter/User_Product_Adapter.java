package com.android.dogefoodie.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.user.UserProductView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class User_Product_Adapter extends RecyclerView.Adapter<User_Product_Adapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public User_Product_Adapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public User_Product_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull User_Product_Adapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());

        Picasso.get()
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProductView.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image_url", product.getImageUrl());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_category", product.getCategory());

            // Use the FLAG_ACTIVITY_NEW_TASK flag if the context is not an Activity context
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgViewProduct);
            productName = itemView.findViewById(R.id.ProductViewName);
            productPrice = itemView.findViewById(R.id.ProductViewPrice);
        }
    }
}

