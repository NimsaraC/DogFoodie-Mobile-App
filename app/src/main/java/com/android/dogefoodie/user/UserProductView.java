package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.R;
import com.android.dogefoodie.Review;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.adapter.User_Review_Adapter;
import com.android.dogefoodie.database.CartDB;
import com.android.dogefoodie.database.ProductDB;
import com.android.dogefoodie.database.ReviewDB;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class UserProductView extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView,txtQty, productPriceTextView, productDescriptionTextView, productCategoryTextView, QntTextView, TotalTextView;
    private Button btnIn, btnDe, btnAddCart, btnSubmitReview;
    private EditText reviewEditText;
    private RatingBar ratingBar;
    private int quantity = 1;
    private double total;
    private LinearLayout cart;
    private RecyclerView recyclerView3;
    private User_Review_Adapter reviewAdapter;
    private ReviewDB reviewDB;
    private ProductDB productDB;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_view);

        productDB = new ProductDB(this);
        initializeViews();
        setupProductDetails();
        setupReviews();
        handleButtonActions();
    }

    private void initializeViews() {
        productImageView = findViewById(R.id.imageView2);
        productNameTextView = findViewById(R.id.textView7);
        productPriceTextView = findViewById(R.id.textView9);
        productDescriptionTextView = findViewById(R.id.textView11);
        productCategoryTextView = findViewById(R.id.textView13);
        QntTextView = findViewById(R.id.textView14);
        TotalTextView = findViewById(R.id.textView16);
        btnIn = findViewById(R.id.buttonIn);
        btnDe = findViewById(R.id.buttonDe);
        btnAddCart = findViewById(R.id.button2);
        cart = findViewById(R.id.cart);
        recyclerView3 = findViewById(R.id.recyclerView3);
        btnSubmitReview = findViewById(R.id.submitReviewButton);
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        txtQty = findViewById(R.id.txtQty);

        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupProductDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("product_name");
            double productPrice = intent.getDoubleExtra("product_price", 0.0);
            String productImageUrl = intent.getStringExtra("product_image_url");
            String productDescription = intent.getStringExtra("product_description");
            String productCategory = intent.getStringExtra("product_category");

            productNameTextView.setText(productName);
            productPriceTextView.setText("$" + productPrice);
            productDescriptionTextView.setText(productDescription);
            productCategoryTextView.setText(productCategory);

            loadImageWithPicasso(productImageUrl);

            QntTextView.setText(String.valueOf(quantity));
            total = productPrice * quantity;
            TotalTextView.setText(String.format("%.2f", total));
        }
    }

    private void loadImageWithPicasso(String productImageUrl) {
        if (productImageUrl != null) {
            if (productImageUrl.startsWith("drawable/")) {
                String drawableName = productImageUrl.replace("drawable/", "").replace(".jpg", "");
                int drawableResId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
                if (drawableResId != 0) {
                    productImageView.setImageResource(drawableResId);
                } else {
                    productImageView.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else if (productImageUrl.startsWith("/")) {
                Picasso.get()
                        .load(new File(productImageUrl))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(productImageView, new com.squareup.picasso.Callback() {
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
                        .into(productImageView, new com.squareup.picasso.Callback() {
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
            productImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void setupReviews() {
        reviewDB = new ReviewDB(this);
        Intent intent = getIntent();
        String productIdStr = intent.getStringExtra("product_id");

        if (productIdStr != null) {
            int productId = Integer.parseInt(productIdStr);
            reviewList = reviewDB.getReviewsByProductId(productId);
            reviewAdapter = new User_Review_Adapter(this, reviewList);
            recyclerView3.setAdapter(reviewAdapter);
        }
    }

    private int getMaxQuantity(String item) {
        return productDB.getProductQuantity(item);
    }
    private void handleButtonActions() {
        String productName = getIntent().getStringExtra("product_name");
        txtQty.setText(String.valueOf(getMaxQuantity(productName)));

        if (productName != null) {
            btnIn.setOnClickListener(v -> {
                if (quantity < getMaxQuantity(productName)) {
                    quantity++;
                    updateTotal();
                } else {
                    Toast.makeText(UserProductView.this, "Maximum quantity reached", Toast.LENGTH_SHORT).show();
                }
            });

            btnDe.setOnClickListener(v -> {
                if (quantity > 1) {
                    quantity--;
                    updateTotal();
                } else {
                    Toast.makeText(UserProductView.this, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("UserProductView", "Product name is null");
        }

        btnAddCart.setOnClickListener(v -> addToCart());

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserCart.class);
            startActivity(intent);
        });

        btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void updateTotal() {
        double productPrice = getIntent().getDoubleExtra("product_price", 0.0);
        QntTextView.setText(String.valueOf(quantity));
        total = productPrice * quantity;
        TotalTextView.setText(String.format("$%.2f", total));
    }


    private void addToCart() {
        SharedPreference sharedPreference = new SharedPreference();
        String id = sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_ID);
        String email = sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_EMAIL);
        int userId = Integer.parseInt(id);

        String productName = productNameTextView.getText().toString();
        double productPrice = getIntent().getDoubleExtra("product_price", 0.0);
        String productImageUrl = getIntent().getStringExtra("product_image_url");

        CartDB cartDB = new CartDB(UserProductView.this);
        cartDB.addCartItem(userId, email, productName, quantity, productPrice, productImageUrl);

        Toast.makeText(UserProductView.this, "Item added to cart", Toast.LENGTH_SHORT).show();
    }

    private void submitReview() {
        SharedPreference preference = new SharedPreference();
        int productId = Integer.parseInt(getIntent().getStringExtra("product_id"));
        String username = preference.getString(getApplicationContext(), SharedPreference.KEY_NAME);
        float rating = ratingBar.getRating();
        String reviewText = reviewEditText.getText().toString();
        String time = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        Review review = new Review(0, productId, username, rating, reviewText, time);

        reviewDB.addReview(review);
        Toast.makeText(UserProductView.this, "Review submitted", Toast.LENGTH_SHORT).show();
        reviewEditText.setText("");
        ratingBar.setRating(0);
        updateReviews();
    }

    private void updateReviews() {
        int productId = Integer.parseInt(getIntent().getStringExtra("product_id"));
        reviewList.clear();
        reviewList.addAll(reviewDB.getReviewsByProductId(productId));
        reviewAdapter.notifyDataSetChanged();
    }
}
