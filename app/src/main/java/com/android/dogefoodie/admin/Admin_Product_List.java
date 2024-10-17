package com.android.dogefoodie.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.adapter.Admin_Product_Adapter;
import com.android.dogefoodie.database.ProductDB;

import java.util.List;

public class Admin_Product_List extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Admin_Product_Adapter adminProductAdapter;
    private List<Product> productList;
    private ProductDB productDB;
    private CardView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDB = new ProductDB(this);
        setContentView(R.layout.activity_admin_product_list);
        productList = productDB.getAllProducts();

        recyclerView = findViewById(R.id.adminRc);
        addButton = findViewById(R.id.addButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adminProductAdapter = new Admin_Product_Adapter(this, productList);
        recyclerView.setAdapter(adminProductAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Product_List.this, Admin_Add_Item.class);
                startActivity(intent);
            }
        });

    }
}