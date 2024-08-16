package com.android.dogefoodie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.dogefoodie.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ReviewsDB";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "reviews";
    private static final String ID = "id";
    private static final String PRODUCT_ID = "productId";
    private static final String USERNAME = "username";
    private static final String RATING = "rating";
    private static final String REVIEW_TEXT = "review";
    private static final String TIME = "time";

    public ReviewDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_ID + " INTEGER, " +
                USERNAME + " TEXT, " +
                RATING + " FLOAT, " +
                REVIEW_TEXT + " TEXT, " +
                TIME + " TEXT)";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addReview(Review review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_ID, review.getProductId());
        values.put(USERNAME, review.getUsername());
        values.put(RATING, review.getRating());
        values.put(REVIEW_TEXT, review.getReview());
        values.put(TIME, review.getTime());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Review> getAllReviews() {
        List<Review> reviewList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(REVIEW_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME))
                );
                reviewList.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reviewList;
    }

    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviewList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, PRODUCT_ID + "=?", new String[]{String.valueOf(productId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Review review = new Review(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(REVIEW_TEXT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME))
                );
                reviewList.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reviewList;
    }

    public Review getReviewById(int reviewId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, ID + "=?", new String[]{String.valueOf(reviewId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Review review = new Review(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(REVIEW_TEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TIME))
            );
            cursor.close();
            db.close();
            return review;
        } else {
            return null;
        }
    }

    public void deleteReview(int reviewId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(reviewId)});
        db.close();
    }
}
