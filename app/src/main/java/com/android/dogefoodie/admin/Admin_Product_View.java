package com.android.dogefoodie.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.Review;
import com.android.dogefoodie.adapter.Admin_Review_Adapter;
import com.android.dogefoodie.adapter.User_Review_Adapter;
import com.android.dogefoodie.database.ProductDB;
import com.android.dogefoodie.database.ReviewDB;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class Admin_Product_View extends AppCompatActivity {

    private TextView txtName, txtCategory, txtDescription, txtPrice, txtStock, txtReview;
    private RecyclerView recyclerView;
    private ImageView productImg;
    private ProductDB productDB;
    private List<Review> reviewList;
    private ReviewDB reviewDB;
    private Admin_Review_Adapter adminReviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_view);

        productDB = new ProductDB(this);

        txtName = findViewById(R.id.adminViewName);
        txtCategory = findViewById(R.id.adminViewCategory);
        txtPrice = findViewById(R.id.adminViewPrice);
        txtDescription = findViewById(R.id.adminViewDescription);
        txtStock = findViewById(R.id.adminViewStock);
        txtReview = findViewById(R.id.textView41);
        recyclerView = findViewById(R.id.adminViewReview);
        productImg = findViewById(R.id.adminViewImg);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupProductDetails();
        setupReviews();
    }

    private void setupProductDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            String productId = intent.getStringExtra("product_id");
            assert productId != null;
            Product product= productDB.getProductById(Integer.parseInt(productId));

            txtName.setText(product.getName());
            txtPrice.setText(String.format("$ %s", product.getPrice()));
            txtDescription.setText(product.getDescription());
            loadImageWithPicasso(product.getImageUrl());
            txtStock.setText(String.format("Available: %s", product.getQuantity()));
            txtCategory.setText(product.getCategory());
        }
    }

    private void loadImageWithPicasso(String productImageUrl) {
        if (productImageUrl != null) {
            if (productImageUrl.startsWith("drawable/")) {
                String drawableName = productImageUrl.replace("drawable/", "").replace(".jpg", "");
                int drawableResId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
                if (drawableResId != 0) {
                    productImg.setImageResource(drawableResId);
                } else {
                    productImg.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else if (productImageUrl.startsWith("/")) {
                Picasso.get()
                        .load(new File(productImageUrl))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(productImg, new com.squareup.picasso.Callback() {
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
                        .load(productImageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(productImg, new com.squareup.picasso.Callback() {
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
            productImg.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void setupReviews() {
        reviewDB = new ReviewDB(this);
        Intent intent = getIntent();
        String productIdStr = intent.getStringExtra("product_id");

        if (productIdStr != null) {
            int productId = Integer.parseInt(productIdStr);
            reviewList = reviewDB.getReviewsByProductId(productId);

            if(reviewList.size()==0){
                txtReview.setText("No Reviews Found");
            }else{
                txtReview.setText("Reviews");
            };
            adminReviewAdapter = new Admin_Review_Adapter(this, reviewList);
            recyclerView.setAdapter(adminReviewAdapter);
        }
    }
}