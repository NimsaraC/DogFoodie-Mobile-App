package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.database.CartDB;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UserProductView extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productCategoryTextView, QntTextView, TotalTextView;
    private Button btnIn, btnDe, btnAddCart, btnCartTest;
    private int quantity = 1;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_view);

        initializeViews();

        setupProductDetails();

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
        btnCartTest = findViewById(R.id.btnCartTest);
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

            // Load the product image using Picasso
            loadImageWithPicasso(productImageUrl);

            QntTextView.setText(String.valueOf(quantity));
            total = productPrice * quantity;
            TotalTextView.setText(String.format("%.2f", total));
        }
    }

    private void loadImageWithPicasso(String productImageUrl) {
        if (productImageUrl != null && productImageUrl.startsWith("/")) {
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
    }


    private void handleButtonActions() {
        btnIn.setOnClickListener(v -> {
            quantity++;
            updateTotal();
        });

        btnDe.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateTotal();
            }
        });

        btnAddCart.setOnClickListener(v -> addToCart());

        btnCartTest.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserCart.class);
            startActivity(intent);
        });
    }

    private void updateTotal() {
        double productPrice = getIntent().getDoubleExtra("product_price", 0.0);
        QntTextView.setText(String.valueOf(quantity));
        total = productPrice * quantity;
        TotalTextView.setText(String.format("%.2f", total));
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
}
