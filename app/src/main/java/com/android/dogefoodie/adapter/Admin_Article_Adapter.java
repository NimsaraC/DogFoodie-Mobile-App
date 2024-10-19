package com.android.dogefoodie.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.Article;
import com.android.dogefoodie.R;
import com.android.dogefoodie.admin.Admin_Product_List;
import com.android.dogefoodie.database.ArticleDB;
import com.android.dogefoodie.user.UserArticleView;

import java.util.List;

public class Admin_Article_Adapter extends RecyclerView.Adapter<Admin_Article_Adapter.ViewHolder> {
    private List<Article> articleList;
    private Context context;

    public Admin_Article_Adapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_article_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);
        ArticleDB articleDB = new ArticleDB(context);
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

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(context)
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete this Article?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int id = article.getId();
                                articleDB.deleteArticle(id);
                                ((Activity) context).recreate();

                                Toast.makeText(context, "Article deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
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
        LinearLayout btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView12);
            categoryTextView = itemView.findViewById(R.id.textView18);
            contentTextView = itemView.findViewById(R.id.textView26);
            dateTextView = itemView.findViewById(R.id.textView28);
            authorTextView = itemView.findViewById(R.id.textView29);
            btnDelete = itemView.findViewById(R.id.btnArticleDelete);
        }
    }
}
