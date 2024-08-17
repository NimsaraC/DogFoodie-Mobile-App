package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.Article;
import com.android.dogefoodie.adapter.User_Article_Adapter;
import com.android.dogefoodie.database.ArticleDB;

import java.util.ArrayList;
import java.util.List;

public class UserArticles extends AppCompatActivity {

    private RecyclerView recyclerView;
    private User_Article_Adapter adapter;
    private ArticleDB articleDB;
    private List<String> categoryList;
    private List<Article> articleList;
    private EditText searchBar;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_articles);

        spinnerCategory = findViewById(R.id.spinner);
        searchBar = findViewById(R.id.editTextText);

        categoryList = new ArrayList<>();
        categoryList.add("All Categories");
        categoryList.add("Dog Food");
        categoryList.add("Treats");
        categoryList.add("Raw Feeding");
        categoryList.add("Homemade Dog Food");
        categoryList.add("Grooming");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryList
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        articleDB = new ArticleDB(this);
        articleList = articleDB.getAllArticles();

        recyclerView = findViewById(R.id.recyclerView5);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new User_Article_Adapter(articleList, this);
        recyclerView.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCategory = categoryList.get(position);

                if ("All Categories".equals(selectedCategory)) {
                    articleList = articleDB.getAllArticles();
                } else {
                    articleList = articleDB.getArticlesByCategory(selectedCategory);
                }

                adapter.updateProductList(articleList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                String selectedCategory = (String) spinnerCategory.getSelectedItem();

                if (!searchQuery.isEmpty()) {
                    if ("All Categories".equals(selectedCategory)) {
                        articleList = articleDB.searchArticles(searchQuery);
                    } else {
                        articleList = articleDB.searchArticlesByCategory(searchQuery, selectedCategory);
                    }
                } else {
                    if ("All Categories".equals(selectedCategory)) {
                        articleList = articleDB.getAllArticles();
                    } else {
                        articleList = articleDB.getArticlesByCategory(selectedCategory);
                    }
                }
                adapter.updateProductList(articleList);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
