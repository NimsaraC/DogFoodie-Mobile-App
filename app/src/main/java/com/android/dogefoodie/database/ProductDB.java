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

    private static ProductDB instance;
    private SQLiteDatabase db;

    public ProductDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static synchronized ProductDB getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDB(context.getApplicationContext());
        }
        return instance;
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
        insertDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private SQLiteDatabase getDatabase(boolean writable) {
        if (writable) {
            if (db == null || !db.isOpen()) {
                db = this.getWritableDatabase();
            }
        } else {
            if (db == null || !db.isOpen()) {
                db = this.getReadableDatabase();
            }
        }
        return db;
    }

    public long addProduct(String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        SQLiteDatabase db = getDatabase(true);
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(CATEGORY, category);
        values.put(PRICE, price);
        values.put(DESCRIPTION, description);
        values.put(QUANTITY, quantity);
        values.put(IMAGE_URL, imageUrl);
        values.put(RATING, rating);

        return db.insert(TABLE_NAME, null, values);
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = getDatabase(false);
        String query = "SELECT * FROM " + TABLE_NAME;
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
        return productList;
    }

    public int getProductQuantity(String productName) {
        SQLiteDatabase db = getDatabase(false);
        Cursor cursor = db.query(TABLE_NAME, new String[]{QUANTITY}, NAME + "=?", new String[]{productName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY));
            cursor.close();
            return quantity;
        } else {
            return 0;
        }
    }

    public void updateProductQuantity(String productName, int quantitySold) {
        SQLiteDatabase db = getDatabase(true);
        int currentQuantity = getProductQuantity(productName);
        int newQuantity = currentQuantity - quantitySold;

        ContentValues values = new ContentValues();
        values.put(QUANTITY, newQuantity);
        db.update(TABLE_NAME, values, NAME + "=?", new String[]{productName});
    }

    public List<Product> getProductByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = getDatabase(false);
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
        return productList;
    }

    public List<Product> searchProduct(String name) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = getDatabase(false);
        Cursor cursor = db.query(TABLE_NAME, null, NAME + " LIKE ?", new String[]{"%" + name + "%"}, null, null, null);

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
        return productList;
    }

    public Product getProductById(int productId) {
        SQLiteDatabase db = getDatabase(false);
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
            return product;
        } else {
            return null;
        }
    }

    public void editProduct(int productId, String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        SQLiteDatabase db = getDatabase(true);
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(CATEGORY, category);
        values.put(PRICE, price);
        values.put(DESCRIPTION, description);
        values.put(QUANTITY, quantity);
        values.put(IMAGE_URL, imageUrl);
        values.put(RATING, rating);
        db.update(TABLE_NAME, values, ID + "=?", new String[]{String.valueOf(productId)});
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = getDatabase(true);
        db.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(productId)});
    }

    private void insertDummyData(SQLiteDatabase db) {
        addProduct(db, "Organic Chicken Dog Food", "Dog Food", 29.99, "High-protein, grain-free dog food made with organic chicken.", 50, "drawable/organic.jpg", 4.5);
        addProduct(db, "Salmon & Sweet Potato Dog Food", "Dog Food", 34.99, "Premium dog food with salmon and sweet potatoes, rich in omega-3.", 30, "drawable/salmon", 4.7);
        addProduct(db, "Beef & Barley Dog Food", "Dog Food", 27.99, "Nutritious beef and barley dog food with essential vitamins.", 45, "drawable/beef", 4.4);
        addProduct(db, "Chicken & Brown Rice Dog Food", "Dog Food", 25.99, "Balanced dog food with chicken and brown rice, ideal for sensitive stomachs.", 60, "drawable/rice", 4.6);
        addProduct(db, "Lamb & Rice Dog Food", "Dog Food", 32.99, "Hypoallergenic lamb and rice dog food for all life stages.", 40, "drawable/lamb", 4.5);
        addProduct(db, "Multivitamin Chews", "Supplements", 19.99, "Daily multivitamin chews for dogs to support overall health.", 100, "drawable/cheew", 4.6);
        addProduct(db, "Joint Health Supplements", "Supplements", 24.99, "Supplements to support joint health and mobility in dogs.", 80, "drawable/joint", 4.4);
        addProduct(db, "Dental Care Sticks", "Treats", 12.99, "Dental care sticks to help clean teeth and freshen breath.", 200, "drawable/dental", 4.3);
        addProduct(db, "Beef Jerky Treats", "Treats", 15.99, "All-natural beef jerky treats for dogs.", 150, "drawable/jecky", 4.8);
        addProduct(db, "Chicken & Pumpkin Biscuits", "Treats", 10.99, "Tasty chicken and pumpkin biscuits for a healthy treat.", 180, "drawable/pumpkin", 4.7);
    }

    private long addProduct(SQLiteDatabase db, String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(CATEGORY, category);
        values.put(PRICE, price);
        values.put(DESCRIPTION, description);
        values.put(QUANTITY, quantity);
        values.put(IMAGE_URL, imageUrl);
        values.put(RATING, rating);

        return db.insert(TABLE_NAME, null, values);
    }
    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

}
