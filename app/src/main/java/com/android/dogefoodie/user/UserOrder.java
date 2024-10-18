package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.dogefoodie.Order;
import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.adapter.User_History_Adapter;
import com.android.dogefoodie.adapter.User_Order_Adapter;
import com.android.dogefoodie.database.OrderDB;

import java.util.List;

public class UserOrder extends AppCompatActivity {

    private TextView txtOrderTitle;
    private RecyclerView recyclerView;
    private OrderDB orderDB;
    private User_Order_Adapter userOrderAdapter;
    private List<Order> orderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        txtOrderTitle = findViewById(R.id.txtOrderViewTitle);
        recyclerView = findViewById(R.id.rev2);

        SharedPreference sharedPreference = new SharedPreference();
        String Uid = sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_ID);

        int id = Integer.parseInt(Uid);

        Intent intent = getIntent();
        String status = intent.getStringExtra("status");

        orderDB = new OrderDB(this);
        orderList = orderDB.getOrdersByUserIdAndStatus(id,status);

        if(status.equals("ToShip")){
            txtOrderTitle.setText("ToShip Orders");
        }else{
            txtOrderTitle.setText("Shipped Orders");
        }

        userOrderAdapter = new User_Order_Adapter(orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userOrderAdapter);

    }
}