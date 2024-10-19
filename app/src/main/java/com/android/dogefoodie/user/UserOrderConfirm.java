package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.Order;
import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.adapter.User_Checkout_Adapter;
import com.android.dogefoodie.database.CartDB;
import com.android.dogefoodie.database.OrderDB;
import com.android.dogefoodie.database.ProductDB;

import java.util.List;

public class UserOrderConfirm extends AppCompatActivity {

    private RecyclerView recyclerView;
    private User_Checkout_Adapter adapter;
    private CartDB cartDB;
    private OrderDB orderDB;
    private int userId;
    private Button btnAddress, btnCard, payNow;
    private TextView txtName, txtNo, txtPhone, txtStreet, txtPostal, txtCity, txtCardNumber, txtTotal;
    private int Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_confirm);

        txtTotal = findViewById(R.id.textView36);

        recyclerView = findViewById(R.id.itemsRV);
        cartDB = new CartDB(this);
        orderDB = new OrderDB(this);
        SharedPreference sharedPreference = new SharedPreference();

        userId = Integer.parseInt(sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_ID));
        List<CartItem> cartItems = cartDB.getCartItemsByUserId(userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new User_Checkout_Adapter(cartItems, cartDB, this);
        recyclerView.setAdapter(adapter);

        calculateTotalPrice(cartItems);

        btnAddress = findViewById(R.id.button3);
        btnCard = findViewById(R.id.btnCard);
        payNow = findViewById(R.id.button7);

        setAddress();
        setCard();

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserAddress.class);
                startActivity(intent);
            }
        });

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCard.class);
                startActivity(intent);
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAddressAndCard()) {
                    processOrder(cartItems);
                } else {
                    Toast.makeText(UserOrderConfirm.this, "Please fill in both address and card details.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calculateTotalPrice(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        txtTotal.setText(String.format("$%.2f", total));
    }

    private boolean validateAddressAndCard() {
        return !txtStreet.getText().toString().isEmpty() &&
                !txtPostal.getText().toString().isEmpty() &&
                !txtCity.getText().toString().isEmpty() &&
                !txtCardNumber.getText().toString().isEmpty();
    }

    private void processOrder(List<CartItem> cartItems) {
        SharedPreference preference = new SharedPreference();
        String address = txtNo.getText().toString() + ", " + txtStreet.getText().toString() + ", " +
                txtPostal.getText().toString() + ", " +
                txtCity.getText().toString();
        String username = preference.getString(getApplicationContext(), SharedPreference.KEY_NAME);

        String Uid = preference.getString(getApplicationContext(), SharedPreference.KEY_ID);
        int id = Integer.parseInt(Uid);
        ProductDB productDB = ProductDB.getInstance(this);

        String status = "";
        for (CartItem item : cartItems) {
            double totalPrice = item.getPrice() * item.getQuantity();
            orderDB.addOrder(item.getProductName(), item.getQuantity(), item.getPrice(), totalPrice, username, address, id,status);

            productDB.updateProductQuantity(item.getProductName(), item.getQuantity());
        }

        userId = Integer.parseInt(preference.getString(getApplicationContext(), SharedPreference.KEY_ID));
        cartDB.deleteCartItemUid(userId);

        Intent intent = new Intent(UserOrderConfirm.this, OrderConfirmationActivity.class);
        startActivity(intent);
        finish();

        productDB.closeDatabase();
    }


    public void setAddress() {
        SharedPreference preference = new SharedPreference();
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtNo = findViewById(R.id.txtNo);
        txtStreet = findViewById(R.id.txtStreet);
        txtPostal = findViewById(R.id.txtPostal);
        txtCity = findViewById(R.id.txtCity);

        txtName.setText(preference.getString(getApplicationContext(), SharedPreference.NAME));
        txtPhone.setText(preference.getString(getApplicationContext(), SharedPreference.PHONE));
        txtNo.setText(preference.getString(getApplicationContext(), SharedPreference.NO));
        txtStreet.setText(preference.getString(getApplicationContext(), SharedPreference.STREET));
        txtPostal.setText(preference.getString(getApplicationContext(), SharedPreference.ZIP));
        txtCity.setText(preference.getString(getApplicationContext(), SharedPreference.CITY));
    }

    public void setCard() {
        SharedPreference preference = new SharedPreference();
        txtCardNumber = findViewById(R.id.textView33);
        txtCardNumber.setText(preference.getString(getApplicationContext(), SharedPreference.CARD_NUMBER));
    }
}
