package com.android.dogefoodie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.Article;
import com.android.dogefoodie.R;
import com.android.dogefoodie.user.UserArticleView;

import java.util.List;

public class User_Article_Adapter extends RecyclerView.Adapter<User_Article_Adapter.ViewHolder> {
    private List<Article> articleList;
    private Context context;

    public User_Article_Adapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.categoryTextView.setText(article.getCategory());
        holder.contentTextView.setText(article.getContent());
        holder.dateTextView.setText(article.getPublicationDate());
        holder.authorTextView.setText(article.getAuthor());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserArticleView.class);
            intent.putExtra("ARTICLE_ID", article.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void updateProductList(List<Article> newArticleList) {
        this.articleList = newArticleList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, categoryTextView, contentTextView, dateTextView, authorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView12);
            categoryTextView = itemView.findViewById(R.id.textView18);
            contentTextView = itemView.findViewById(R.id.textView26);
            dateTextView = itemView.findViewById(R.id.textView28);
            authorTextView = itemView.findViewById(R.id.textView29);
        }
    }
}
