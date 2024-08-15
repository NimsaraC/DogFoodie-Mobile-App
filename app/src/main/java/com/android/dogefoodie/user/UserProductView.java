package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.database.CartDB;
import com.squareup.picasso.Picasso;

public class UserProductView extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productCategoryTextView, QntTextView, TotalTextView;
    private Button btnIn, btnDe, btnAddCart;
    int Quantity ;
    double Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_view);

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

        SharedPreference sharedPreference = new SharedPreference();


        if (getIntent() != null) {
            String productName = getIntent().getStringExtra("product_name");
            double productPrice = getIntent().getDoubleExtra("product_price", 0.0);
            String productImageUrl = getIntent().getStringExtra("product_image_url");
            String productDescription = getIntent().getStringExtra("product_description");
            String productCategory = getIntent().getStringExtra("product_category");

            productNameTextView.setText(productName);
            productPriceTextView.setText("$" + productPrice);
            productDescriptionTextView.setText(productDescription);
            productCategoryTextView.setText(productCategory);

            Picasso.get()
                    .load(productImageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(productImageView);
        }


        Quantity = Integer.parseInt(QntTextView.getText().toString());
        Total = Double.parseDouble(TotalTextView.getText().toString());
        double productPrice = getIntent().getDoubleExtra("product_price", 0.0);

        Quantity = 1;
        QntTextView.setText(String.valueOf(Quantity));
        TotalTextView.setText(String.format("%.2f", productPrice));

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quantity++;
                QntTextView.setText(String.valueOf(Quantity));
                Total = (Quantity*productPrice);
                TotalTextView.setText(String.format("%.2f", Total));
            }
        });

        btnDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Quantity > 0) {
                    Quantity--;
                    QntTextView.setText(String.valueOf(Quantity));
                    Total = (Quantity*productPrice);
                    TotalTextView.setText(String.format("%.2f", Total));
                }
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = sharedPreference.getString(getApplicationContext(),SharedPreference.KEY_ID);
                String email = sharedPreference.getString(getApplicationContext(),SharedPreference.KEY_EMAIL);
                int userId = Integer.parseInt(id);

                String productName = productNameTextView.getText().toString();
                double productPrice = Double.parseDouble(productPriceTextView.getText().toString().replace("$", ""));
                int quantity = Integer.parseInt(QntTextView.getText().toString());
                double totalPrice = Double.parseDouble(TotalTextView.getText().toString());
                String productImageUrl = getIntent().getStringExtra("product_image_url");

                CartDB cartDB = new CartDB(UserProductView.this);

                cartDB.addCartItem(userId, email, productName, quantity, productPrice, productImageUrl);

                Toast.makeText(UserProductView.this, "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
