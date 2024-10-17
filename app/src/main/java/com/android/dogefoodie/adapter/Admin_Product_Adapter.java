package com.android.dogefoodie.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.admin.Admin_Edit_Item;
import com.android.dogefoodie.admin.Admin_Product_List;
import com.android.dogefoodie.admin.Admin_Product_View;
import com.android.dogefoodie.database.ProductDB;
import com.android.dogefoodie.user.UserProductView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class Admin_Product_Adapter  extends RecyclerView.Adapter<Admin_Product_Adapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private ProductDB productDB;

    public Admin_Product_Adapter(Context context, List<Product> productList){
        this.context = context;
        this.productList = productList;
        this.productDB = new ProductDB(context);
    }
    @NonNull
    @Override
    public Admin_Product_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Product_Adapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$ %s", product.getPrice()));
        holder.productCategory.setText(product.getCategory());
        holder.productStock.setText(String.format("Available: %s", product.getQuantity()));

        holder.productEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Admin_Edit_Item.class);
                intent.putExtra("product_id", String.valueOf(product.getId()));

                if (!(context instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
        });

        holder.productView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Admin_Product_View.class);
                intent.putExtra("product_id", String.valueOf(product.getId()));

                if (!(context instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
        });

        holder.productDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int id = product.getId();
                                productDB.deleteProduct(id);

                                Intent intent = new Intent(context, Admin_Product_List.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);

                                Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        String imageUrl = product.getImageUrl();

        if (imageUrl != null) {
            if (imageUrl.startsWith("drawable/")) {
                String drawableName = imageUrl.replace("drawable/", "").replace(".jpg", "");
                int drawableResId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

                if (drawableResId != 0) {
                    holder.productImage.setImageResource(drawableResId);
                } else {
                    holder.productImage.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else if (imageUrl.startsWith("/")) {
                Picasso.get()
                        .load(new File(imageUrl))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.productImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Error loading image", e);
                            }
                        });
            } else {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.productImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Error loading image", e);
                            }
                        });
            }
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }



    @Override
    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productCategory, productPrice, productStock;
        LinearLayout layout;
        Button productView, productEdit, productDelete;
        ImageView productImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.ProductName);
            layout = itemView.findViewById(R.id.AdminProduct);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStock = itemView.findViewById(R.id.productStock);
            productView = itemView.findViewById(R.id.productView);
            productEdit = itemView.findViewById(R.id.productEdit);
            productDelete = itemView.findViewById(R.id.productRemove);
            productImage = itemView.findViewById(R.id.productImg);
        }
    }


}
