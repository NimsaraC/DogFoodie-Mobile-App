package com.android.dogefoodie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.dogefoodie.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CartDB";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "cart";
    private static final String USER_ID = "user_id";
    private static final String EMAIL = "email";
    private static final String PRODUCT_NAME = "product_name";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";
    private static final String TOTAL_PRICE = "total_price";
    private static final String IMAGE_URL = "image_url";

    public CartDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                USER_ID + " INTEGER, " +
                EMAIL + " TEXT, " +
                PRODUCT_NAME + " TEXT, " +
                QUANTITY + " INTEGER, " +
                PRICE + " REAL, " +
                TOTAL_PRICE + " REAL, " +
                IMAGE_URL + " TEXT, " +
                "PRIMARY KEY (" + USER_ID + ", " + PRODUCT_NAME + "))";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addCartItem(int userId, String email, String productName, int quantity, double price, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, userId);
        values.put(EMAIL, email);
        values.put(PRODUCT_NAME, productName);
        values.put(PRICE, price);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, quantity * price);
        values.put(IMAGE_URL, imageUrl);

        Cursor cursor = db.rawQuery("SELECT " + QUANTITY + " FROM " + TABLE_NAME + " WHERE " + USER_ID + " = ? AND " + PRODUCT_NAME + " = ?", new String[]{String.valueOf(userId), productName});
        long result;

        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY));
            int newQuantity = currentQuantity + quantity;
            values.put(QUANTITY, newQuantity);
            values.put(TOTAL_PRICE, newQuantity * price);
            result = db.update(TABLE_NAME, values, USER_ID + " = ? AND " + PRODUCT_NAME + " = ?", new String[]{String.valueOf(userId), productName});
        } else {
            result = db.insert(TABLE_NAME, null, values);
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL))
                );
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItemList;
    }

    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL))
                );
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItemList;
    }

    public void updateCartItemQuantity(int userId, String productName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        double price = getProductPrice(userId, productName);

        ContentValues values = new ContentValues();
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, quantity * price);

        db.update(TABLE_NAME, values, USER_ID + " = ? AND " + PRODUCT_NAME + " = ?", new String[]{String.valueOf(userId), productName});
        db.close();
    }

    public void deleteCartItem(int userId, String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, USER_ID + "=? AND " + PRODUCT_NAME + "=?", new String[]{String.valueOf(userId), productName});
        db.close();
    }

    public void deleteCartItemUid(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }


    public void deleteAllCartItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    private double getProductPrice(int userId, String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{PRICE}, USER_ID + "=? AND " + PRODUCT_NAME + "=?", new String[]{String.valueOf(userId), productName}, null, null, null);
        double price = 0;

        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE));
        }
        cursor.close();
        return price;
    }
}
