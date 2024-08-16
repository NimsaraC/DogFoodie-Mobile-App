package com.android.dogefoodie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.Review;
import com.android.dogefoodie.database.ReviewDB;

import java.util.List;

public class User_Review_Adapter extends RecyclerView.Adapter<User_Review_Adapter.ViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public User_Review_Adapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.usernameTextView.setText(review.getUsername());
        holder.reviewTextView.setText(review.getReview());
        holder.ratingBar.setRating((float) review.getRating());
        holder.timeTextView.setText(review.getTime());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView reviewTextView;
        RatingBar ratingBar;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.reviewUsername);
            reviewTextView = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            timeTextView = itemView.findViewById(R.id.reviewTime);
        }
    }
}

