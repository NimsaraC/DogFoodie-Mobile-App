package com.android.dogefoodie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.dogefoodie.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProductsDB";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "products";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";
    private static final String QUANTITY = "quantity";
    private static final String IMAGE_URL = "imageUrl";
    private static final String RATING = "rating";

    public ProductDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                CATEGORY + " TEXT, " +
                PRICE + " REAL, " +
                DESCRIPTION + " TEXT, " +
                QUANTITY + " INTEGER, " +
                IMAGE_URL + " TEXT, " +
                RATING + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addProduct(String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(CATEGORY, category);
        values.put(PRICE, price);
        values.put(DESCRIPTION, description);
        values.put(QUANTITY, quantity);
        values.put(IMAGE_URL, imageUrl);
        values.put(RATING, rating);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(RATING))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public Product getProductByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(RATING))
            );
            cursor.close();
            db.close();
            return product;
        } else {
            return null;
        }
    }

    public List<Product> getProductByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, CATEGORY + "=?", new String[]{category}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(RATING))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, ID + "=?", new String[]{String.valueOf(productId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(RATING))
            );
            cursor.close();
            db.close();
            return product;
        } else {
            return null;
        }
    }

    public void editProduct(int productId, String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(CATEGORY, category);
        values.put(PRICE, price);
        values.put(DESCRIPTION, description);
        values.put(QUANTITY, quantity);
        values.put(IMAGE_URL, imageUrl);
        values.put(RATING, rating);
        db.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
    }
}

