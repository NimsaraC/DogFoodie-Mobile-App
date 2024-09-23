package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dogefoodie.MainActivity;
import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.adapter.User_Category_Adapter;
import com.android.dogefoodie.adapter.User_Product_Adapter;
import com.android.dogefoodie.database.ProductDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserMain extends AppCompatActivity implements User_Category_Adapter.OnCategoryClickListener {

    private RecyclerView recyclerView1, recyclerView2;
    private User_Product_Adapter productAdapter;
    private User_Category_Adapter categoryAdapter;
    private List<String> categoryList;
    private List<Product> productList;
    private ProductDB productDB;
    private TextView edtSearch;
    private Button btnArticle;
    private FloatingActionButton floatingActionButton;
    private LinearLayout logOut, history, cart, article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        edtSearch = findViewById(R.id.edtSearch);
        productDB = new ProductDB(this);

        btnArticle = findViewById(R.id.button);
        floatingActionButton = findViewById(R.id.floatingActionButton2);

        logOut = findViewById(R.id.logout);
        history = findViewById(R.id.history);
        cart = findViewById(R.id.cart);
        article = findViewById(R.id.article);

        recyclerView1 = findViewById(R.id.recyclerView1);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(categoryLayoutManager);

        categoryList = new ArrayList<>();
        categoryList.add("Dog Food");
        categoryList.add("Treats");
        categoryList.add("Supplements");
        categoryList.add("Grooming");

        categoryAdapter = new User_Category_Adapter(this, categoryList, this);
        recyclerView1.setAdapter(categoryAdapter);

        recyclerView2 = findViewById(R.id.recyclerView2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(gridLayoutManager);

        productList = productDB.getAllProducts();
        productAdapter = new User_Product_Adapter(this, productList);
        recyclerView2.setAdapter(productAdapter);

        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserArticles.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCart.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrderHistory.class);
                startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCart.class);
                startActivity(intent);
            }
        });
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

        btnArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserArticles.class);
                startActivity(intent);
            }
        });
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("your_preference_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(SharedPreference.KEY_NAME);
        editor.remove(SharedPreference.KEY_ID);
        editor.remove(SharedPreference.KEY_EMAIL);
        editor.remove(SharedPreference.KEY_STATUS);
        editor.remove(SharedPreference.PHONE);
        editor.remove(SharedPreference.NAME);
        editor.remove(SharedPreference.NO);
        editor.remove(SharedPreference.STREET);
        editor.remove(SharedPreference.CITY);
        editor.remove(SharedPreference.ZIP);
        editor.remove(SharedPreference.TOTAL);
        editor.remove(SharedPreference.CARD_HOLDER);
        editor.remove(SharedPreference.CVV);
        editor.remove(SharedPreference.CARD_NUMBER);
        editor.remove(SharedPreference.EXPIRE);

        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCategoryClick(String category) {
        productList = productDB.getProductByCategory(category);
        productAdapter.updateProductList(productList);
    }
}
