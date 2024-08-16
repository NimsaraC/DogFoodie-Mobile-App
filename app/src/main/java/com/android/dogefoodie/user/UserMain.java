package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.adapter.User_Category_Adapter;
import com.android.dogefoodie.adapter.User_Product_Adapter;
import com.android.dogefoodie.database.ProductDB;

import java.util.ArrayList;
import java.util.List;

public class UserMain extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private User_Product_Adapter productAdapter;
    private User_Category_Adapter categoryAdapter;
    private List<String> categoryList;
    private List<Product> productList;
    private ProductDB productDB;
    private TextView edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        edtSearch = findViewById(R.id.edtSearch);
        productDB = new ProductDB(this);

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(gridLayoutManager);

        productList = productDB.getAllProducts();
        productAdapter = new User_Product_Adapter(this, productList);
        recyclerView2.setAdapter(productAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                if (!searchQuery.isEmpty()) {
                    productList = productDB.searchProduct(searchQuery);
                } else {
                    productList = productDB.getAllProducts();
                }
                productAdapter.updateProductList(productList);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
