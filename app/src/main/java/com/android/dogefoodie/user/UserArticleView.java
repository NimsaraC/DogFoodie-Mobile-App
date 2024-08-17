package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.dogefoodie.Article;
import com.android.dogefoodie.R;
import com.android.dogefoodie.database.ArticleDB;

public class UserArticleView extends AppCompatActivity {

    private ArticleDB articleDB;
    private TextView titleTextView, authorTextView, dateTextView, contentTextView, categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_article_view);

        articleDB = new ArticleDB(this);

        titleTextView = findViewById(R.id.textViewTitle);
        authorTextView = findViewById(R.id.textViewAuthor);
        dateTextView = findViewById(R.id.textViewDate);
        contentTextView = findViewById(R.id.textViewContent);
        categoryTextView = findViewById(R.id.textViewCategory);

        int articleId = getIntent().getIntExtra("ARTICLE_ID", -1);
        if (articleId != -1) {
            Article article = articleDB.getArticleById(articleId);
            if (article != null) {
                titleTextView.setText(article.getTitle());
                authorTextView.setText(article.getAuthor());
                dateTextView.setText(article.getPublicationDate());
                contentTextView.setText(article.getContent());
                categoryTextView.setText(article.getCategory());
            }
        }
    }
}
