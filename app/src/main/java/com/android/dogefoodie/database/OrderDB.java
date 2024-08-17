package com.android.dogefoodie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.dogefoodie.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "OrderDB";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "orders";
    private static final String ID = "id";
    private static final String PRODUCT = "product";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";
    private static final String TOTAL_PRICE = "total_price";
    private static final String USERNAME = "username";
    private static final String ADDRESS = "address";

    public OrderDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT + " TEXT, " +
                QUANTITY + " INTEGER, " +
                PRICE + " REAL, " +
                TOTAL_PRICE + " REAL, " +
                USERNAME + " TEXT, " +
                ADDRESS + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addOrder(String product, int quantity, double price, double totalPrice, String username, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT, product);
        values.put(QUANTITY, quantity);
        values.put(PRICE, price);
        values.put(TOTAL_PRICE, totalPrice);
        values.put(USERNAME, username);
        values.put(ADDRESS, address);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public List<Order> getOrdersByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, USERNAME + "=?", new String[]{username}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public void updateOrder(int id, String product, int quantity, double price, double totalPrice, String username, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT, product);
        values.put(QUANTITY, quantity);
        values.put(PRICE, price);
        values.put(TOTAL_PRICE, totalPrice);
        values.put(USERNAME, username);
        values.put(ADDRESS, address);

        db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
