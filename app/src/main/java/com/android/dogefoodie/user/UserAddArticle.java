package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.dogefoodie.R;
import com.android.dogefoodie.admin.Admin_Add_Item;
import com.android.dogefoodie.admin.Admin_Product_List;
import com.android.dogefoodie.database.ArticleDB;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserAddArticle extends AppCompatActivity {

    private EditText edtTitle, edtAuthor, edtContent;
    private Spinner spinnerCategory;
    private LinearLayout btnSubmit;
    private ArticleDB articleDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_article);

        articleDB = new ArticleDB(this);

        edtTitle = findViewById(R.id.edtArticleTitle);
        edtAuthor = findViewById(R.id.edtAuthorName);
        edtContent = findViewById(R.id.edtArticleContent);
        spinnerCategory = findViewById(R.id.spinner2);
        btnSubmit = findViewById(R.id.btnAddArticle);

        setupCategorySpinner();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String author = edtAuthor.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String content = edtContent.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(category) ||
                        TextUtils.isEmpty(author) || TextUtils.isEmpty(category) || TextUtils.isEmpty(content))  {
                    Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                }

                boolean success = addItems(title,author,content,category,date);

                if (success) {
                    Toast.makeText(getApplicationContext(), "Article added successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                    //startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add item. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean addItems(String title, String author, String content, String category, String date){

        long result = articleDB.addArticle(title, author, content, category, date);
        return result != -1;
    }

    private void setupCategorySpinner() {
        String[] categories = new String[]{
                "Dog Food",
                "Treats",
                "Raw Feeding",
                "Homemade Dog Food",
                "Grooming"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_selected_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
}