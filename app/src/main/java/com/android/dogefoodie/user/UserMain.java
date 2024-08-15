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

import java.util.ArrayList;
import java.util.List;

public class UserMain extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private User_Category_Adapter categoryAdapter;
    private User_Product_Adapter productAdapter;
    private List<String> categoryList;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

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


        productList = new ArrayList<>();
        productList.add(new Product(1, "Product 1", "Category 1", 29.99, "Description 1", 10, "https://example.com/image1.jpg", 4.5));
        productList.add(new Product(2, "Product 2", "Category 2", 19.99, "Description 2", 20, "https://example.com/image2.jpg", 4.0));

        productAdapter = new User_Product_Adapter(this, productList);
        recyclerView2.setAdapter(productAdapter);
    }
}
