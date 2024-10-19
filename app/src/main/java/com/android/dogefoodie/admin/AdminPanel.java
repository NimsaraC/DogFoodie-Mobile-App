package com.android.dogefoodie.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dogefoodie.Article;
import com.android.dogefoodie.Order;
import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.User;
import com.android.dogefoodie.adapter.AdminPanel_Order_Adapter;
import com.android.dogefoodie.adapter.User_Order_Adapter;
import com.android.dogefoodie.database.ArticleDB;
import com.android.dogefoodie.database.OrderDB;
import com.android.dogefoodie.database.ProductDB;
import com.android.dogefoodie.database.UserDB;

import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends AppCompatActivity {

    private TextView productsCount, ordersCount, salesCount, usersCount, articlesCount, title;
    private LinearLayout btnProduct, btnOrders, btnArticles;
    private RecyclerView recyclerView;
    UserDB userDB;
    ProductDB productDB;
    ArticleDB articleDB;
    OrderDB orderDB;
    private AdminPanel_Order_Adapter adminPanelOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        productsCount = findViewById(R.id.ProductCount);
        ordersCount = findViewById(R.id.OrderCount);
        salesCount = findViewById(R.id.SalesCount);
        usersCount = findViewById(R.id.UserCount);
        articlesCount = findViewById(R.id.ArticleCount);
        title = findViewById(R.id.txtOrderTitle);
        btnProduct = findViewById(R.id.btnProduct);
        btnOrders = findViewById(R.id.btnOrders);
        btnArticles = findViewById(R.id.btnArticles);
        recyclerView = findViewById(R.id.rcvToShip);

        productDB = new ProductDB(this);
        userDB = new UserDB(this);
        articleDB = new ArticleDB(this);
        orderDB = new OrderDB(this);

        List<Product> productList = productDB.getAllProducts();
        List<User> users = userDB.getAllUsers();
        List<Article> articles = articleDB.getAllArticles();
        List<Order> orderList = orderDB.getAllOrders();

        double total = 0 ;

        for (Order order : orderList) {
            total += order.getTotalPrice();
        }

        productsCount.setText(String.format("Available %d products", productList.size()));
        usersCount.setText(String.valueOf(users.size()));
        articlesCount.setText(String.valueOf(articles.size()));
        ordersCount.setText(String.valueOf(orderList.size()));
        if(total>1000){
            total = (total/2)*100;
            salesCount.setText(String.format("$ %.2f k", total));
        }else{
            salesCount.setText(String.format("$ %.2f ", total));
        }

        getOrders();

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin_Product_List.class);
                startActivity(intent);
            }
        });

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin_Order.class);
                startActivity(intent);
            }
        });

        btnArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Admin_Article.class);
                startActivity(intent);
            }
        });
    }
    private void getOrders(){
        OrderDB db = new OrderDB(this);
        List<Order> toShipOrders = new ArrayList<>();
        List<Order> orders ;

        orders = db.getAllOrders();

        for (Order order : orders) {
            if (order.getOrderStatus().equals("ToShip")) {
                toShipOrders.add(order);
            }
        }
        int toShipOrdersCount = toShipOrders.size();
        title.setText(String.format("Orders to Ship (%s)", toShipOrdersCount));

        adminPanelOrderAdapter = new AdminPanel_Order_Adapter(toShipOrders, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adminPanelOrderAdapter);
    }
}