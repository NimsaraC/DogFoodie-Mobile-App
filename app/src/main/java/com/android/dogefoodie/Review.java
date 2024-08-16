package com.android.dogefoodie;
import java.time.LocalDateTime;

public class Review {
    private int id;
    private int productId;
    private String username;
    private float rating;
    private String review;
    private String time;

    public Review(int id, int productId, String username, float rating, String review, String time) {
        this.id = id;
        this.productId = productId;
        this.username = username;
        this.rating = rating;
        this.review = review;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

