package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.adapter.User_Cart_Adapter;
import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.database.CartDB;

import java.util.List;

public class UserCart extends AppCompatActivity implements User_Cart_Adapter.TotalPriceUpdater {

    private RecyclerView recyclerView;
    private User_Cart_Adapter adapter;
    private CartDB cartDB;
    private int userId;
    private TextView textViewTotalPrice;
    private Button Checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        Checkout = findViewById(R.id.button5);
        cartDB = new CartDB(this);
        SharedPreference sharedPreference = new SharedPreference();

        recyclerView = findViewById(R.id.recycler_view_cart);
        textViewTotalPrice = findViewById(R.id.textView23);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userId = Integer.parseInt(sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_ID));

        List<CartItem> cartItems = cartDB.getCartItemsByUserId(userId);

        adapter = new User_Cart_Adapter(cartItems, cartDB, this, this);
        recyclerView.setAdapter(adapter);

        SharedPreference preference = new SharedPreference();

        updateTotalPrice(cartItems);

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalPriceText = textViewTotalPrice.getText().toString();
                String priceString = totalPriceText.replaceAll("[^\\d.]", "");

                try {
                    double totalPrice = Double.parseDouble(priceString);
                    preference.SaveString(getApplicationContext(), String.valueOf(totalPrice), SharedPreference.TOTAL);
                    Intent intent = new Intent(getApplicationContext(), UserOrderConfirm.class);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        List<CartItem> updatedCartItems = cartDB.getCartItemsByUserId(userId);
        adapter.updateCartItems(updatedCartItems);
        updateTotalPrice(updatedCartItems);
    }

    @Override
    public void onPriceUpdate(List<CartItem> updatedCartItems) {
        updateTotalPrice(updatedCartItems);
    }

    private void updateTotalPrice(List<CartItem> cartItems) {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        textViewTotalPrice.setText(String.format("Total Price: $%.2f", totalPrice));
    }
}

