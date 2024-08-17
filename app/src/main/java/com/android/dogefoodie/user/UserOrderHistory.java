package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.Order;
import com.android.dogefoodie.adapter.User_History_Adapter;
import com.android.dogefoodie.database.OrderDB;
import com.android.dogefoodie.SharedPreference;

import java.util.List;

public class UserOrderHistory extends AppCompatActivity {

    private ImageView home;
    private RecyclerView list;
    private User_History_Adapter historyAdapter;
    private OrderDB orderDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);

        home = findViewById(R.id.Home);
        list = findViewById(R.id.list1);

        SharedPreference sharedPreference = new SharedPreference();
        String username = sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_NAME);

        orderDB = new OrderDB(this);
        List<Order> orderList = orderDB.getOrdersByUsername(username);

        historyAdapter = new User_History_Adapter(orderList, this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(historyAdapter);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserMain.class);
                startActivity(intent);
            }
        });
    }
}
