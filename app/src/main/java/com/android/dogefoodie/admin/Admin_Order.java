package com.android.dogefoodie.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.dogefoodie.Order;
import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.adapter.AdminPanel_Order_Adapter;
import com.android.dogefoodie.adapter.Admin_Order_Adapter;
import com.android.dogefoodie.adapter.Admin_Product_Adapter;
import com.android.dogefoodie.database.OrderDB;

import java.util.List;

public class Admin_Order extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Admin_Order_Adapter adminOrderAdapter;
    private List<Order> orderList;
    private OrderDB orderDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        orderDB = new OrderDB(this);
        orderList = orderDB.getAllOrders();

        recyclerView = findViewById(R.id.adminOrder);

        adminOrderAdapter = new Admin_Order_Adapter(orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adminOrderAdapter);
    }
}