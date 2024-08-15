package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.adapter.User_Category_Adapter;
import com.android.dogefoodie.adapter.User_Product_Adapter;
import com.android.dogefoodie.database.ProductDB;

import java.util.ArrayList;
import java.util.List;

public class UserMain extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private User_Category_Adapter categoryAdapter;
    private User_Product_Adapter productAdapter;
    private List<String> categoryList;
    private List<Product> productList;
    private ProductDB productDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        productDB = new ProductDB(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        recyclerView1 = findViewById(R.id.recyclerView1);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(categoryLayoutManager);

        categoryList = new ArrayList<>();
        categoryList.add("Dog Food");
        categoryList.add("Treats");
        categoryList.add("Toys");
        categoryList.add("Supplements");
        categoryList.add("Grooming");

        categoryAdapter = new User_Category_Adapter(this, categoryList);
        recyclerView1.setAdapter(categoryAdapter);

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(gridLayoutManager);

        productList = productDB.getAllProducts();

        productAdapter = new User_Product_Adapter(this, productList);
        recyclerView2.setAdapter(productAdapter);
    }
}
