package com.android.dogefoodie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.dogefoodie.R;
import com.android.dogefoodie.Review;

import java.util.List;

public class Admin_Review_Adapter extends RecyclerView.Adapter<Admin_Review_Adapter.ViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public Admin_Review_Adapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public Admin_Review_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Review_Adapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.txtUsername.setText(review.getUsername());
        holder.txtId.setText(String.format("Review Id: %s", review.getId()));
        holder.txtTime.setText(review.getTime());
        holder.txtRating.setText(String.valueOf(review.getRating()));
        holder.txtContent.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtTime, txtRating, txtId, txtContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.reviewAdminUsername);
            txtTime = itemView.findViewById(R.id.reviewAdminTime);
            txtRating = itemView.findViewById(R.id.reviewAdminRating);
            txtId = itemView.findViewById(R.id.reviewAdminId);
            txtContent = itemView.findViewById(R.id.reviewAdminContent);
        }
    }
}
