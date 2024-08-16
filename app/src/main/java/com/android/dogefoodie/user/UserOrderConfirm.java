package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.dogefoodie.CartItem;
import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.adapter.User_Cart_Adapter;
import com.android.dogefoodie.adapter.User_Checkout_Adapter;
import com.android.dogefoodie.database.CartDB;

import java.util.List;

public class UserOrderConfirm extends AppCompatActivity {

    private RecyclerView recyclerView;
    private User_Checkout_Adapter adapter;
    private CartDB cartDB;
    private int userId;
    private Button btnAddress, btnCard;
    private TextView txtName, txtNo, txtPhone, txtStreet, txtPostal, txtCity, txtCardNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_confirm);

        recyclerView = findViewById(R.id.itemsRV);
        cartDB = new CartDB(this);
        SharedPreference sharedPreference = new SharedPreference();

        userId = Integer.parseInt(sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_ID));
        List<CartItem> cartItems = cartDB.getCartItemsByUserId(userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new User_Checkout_Adapter(cartItems, cartDB,this);
        recyclerView.setAdapter(adapter);

        btnAddress = findViewById(R.id.button3);
        btnCard = findViewById(R.id.btnCard);

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



    }
    public void setAddress(){

        SharedPreference preference = new SharedPreference();
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtNo = findViewById(R.id.txtNo);
        txtStreet = findViewById(R.id.txtStreet);
        txtPostal = findViewById(R.id.txtPostal);
        txtCity = findViewById(R.id.txtCity);

        txtName.setText(preference.getString(getApplicationContext(),SharedPreference.NAME));
        txtPhone.setText(preference.getString(getApplicationContext(),SharedPreference.PHONE));
        txtNo.setText(preference.getString(getApplicationContext(),SharedPreference.NO));
        txtStreet.setText(preference.getString(getApplicationContext(),SharedPreference.STREET));
        txtPostal.setText(preference.getString(getApplicationContext(),SharedPreference.ZIP));
        txtCity.setText(preference.getString(getApplicationContext(),SharedPreference.CITY));
    }
    public void setCard(){

        SharedPreference preference = new SharedPreference();
        txtCardNumber = findViewById(R.id.textView33);

        txtCardNumber.setText(preference.getString(getApplicationContext(),SharedPreference.CARD_NUMBER));
    }
}