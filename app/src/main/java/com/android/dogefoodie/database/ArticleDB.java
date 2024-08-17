package com.android.dogefoodie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.dogefoodie.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ArticlesDB";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "articles";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String CATEGORY = "category";
    private static final String PUBLICATION_DATE = "publicationDate";

    public ArticleDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                AUTHOR + " TEXT, " +
                CONTENT + " TEXT, " +
                CATEGORY + " TEXT, " +
                PUBLICATION_DATE + " TEXT)";
        db.execSQL(createTableQuery);

        insertDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addArticle(String title, String author, String content, String category, String publicationDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(AUTHOR, author);
        values.put(CONTENT, content);
        values.put(CATEGORY, category);
        values.put(PUBLICATION_DATE, publicationDate);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PUBLICATION_DATE))
                );
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return articleList;
    }
    public List<Article> searchArticlesByCategory(String title, String category) {
        List<Article> articleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, TITLE + " LIKE ? AND " + CATEGORY + "=?", new String[]{"%" + title + "%", category}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PUBLICATION_DATE))
                );
                articleList.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return articleList;
    }


    public List<Article> getArticlesByCategory(String category) {
        List<Article> articleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, CATEGORY + "=?", new String[]{category}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PUBLICATION_DATE))
                );
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return articleList;
    }

    public List<Article> searchArticles(String title) {
        List<Article> articleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, TITLE + " LIKE ?", new String[]{"%" + title + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PUBLICATION_DATE))
                );
                articleList.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return articleList;
    }

    public void deleteArticle(int articleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{String.valueOf(articleId)});
        db.close();
    }
    public Article getArticleById(int articleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, ID + "=?", new String[]{String.valueOf(articleId)}, null, null, null);
        if (cursor.moveToFirst()) {
            Article article = new Article(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PUBLICATION_DATE))
            );
            cursor.close();
            db.close();
            return article;
        }
        cursor.close();
        db.close();
        return null;
    }


    private void insertDummyData(SQLiteDatabase db) {
        addArticle(db, "Nutrition Tips for Puppies", "John Doe",
                "Puppies have unique nutritional needs compared to adult dogs. It's essential to provide them with a balanced diet that supports their growth and development. Key components include high-quality protein, essential fatty acids, and appropriate calcium and phosphorus levels. Ensure the puppy food is specifically formulated for their age to avoid nutritional imbalances.",
                "Puppy Nutrition", "2024-01-01");

        addArticle(db, "Feeding Older Dogs", "Jane Smith",
                "As dogs age, their nutritional requirements change. Older dogs may need a diet that is lower in calories but higher in fiber to help maintain a healthy weight. Additionally, consider foods that support joint health and contain antioxidants to boost their immune system. Look for senior dog foods that are specifically formulated to meet their needs.",
                "Senior Dog Nutrition", "2024-02-15");

        addArticle(db, "Best Dog Foods for Allergies", "Alice Johnson",
                "Dogs with allergies often benefit from limited ingredient diets or hypoallergenic foods. Look for dog foods that contain novel protein sources, such as duck or venison, and avoid common allergens like beef, chicken, and grains. It's also important to choose a food with high-quality ingredients to reduce the risk of allergic reactions.",
                "Dog Food", "2024-03-10");

        addArticle(db, "Understanding Dog Food Labels", "Bob Brown",
                "Reading dog food labels can be confusing, but understanding the key components can help you make better choices for your pet. Look for high-quality protein sources listed at the top, and avoid foods with excessive fillers or artificial additives. The ingredient list should be clear and transparent, providing detailed information about the food's content.",
                "Dog Food", "2024-04-05");

        addArticle(db, "Raw Feeding 101", "Emily Davis",
                "Raw feeding involves providing your dog with uncooked, natural foods that mimic what they would eat in the wild. This includes raw meats, bones, organs, and vegetables. It's crucial to ensure a balanced diet and to handle raw food with proper hygiene to prevent contamination. Consult with a veterinarian or a pet nutritionist to create a well-rounded raw feeding plan.",
                "Raw Feeding", "2024-05-20");

        addArticle(db, "Homemade Dog Food Recipes", "Michael Lee",
                "Making homemade dog food can be a rewarding way to ensure your pet receives high-quality, fresh ingredients. Recipes can include cooked meats, vegetables, and grains like rice or oats. Be sure to include a variety of ingredients to meet all of your dog's nutritional needs. Consulting a vet for balanced recipes is recommended to avoid deficiencies.",
                "Homemade Dog Food", "2024-06-10");

        addArticle(db, "Grooming Essentials for Dogs", "Sarah Brown",
                "Regular grooming is essential for maintaining your dog's health and appearance. This includes brushing to remove loose fur and prevent matting, bathing with a dog-friendly shampoo, and trimming nails to prevent overgrowth. Additionally, check your dog's ears and teeth regularly to ensure overall cleanliness and health.",
                "Grooming", "2024-07-15");

        addArticle(db, "The Best Treats for Training", "Laura Wilson",
                "Training treats should be small, soft, and easy to break into pieces. They should also be high-value to motivate your dog. Consider using treats with natural ingredients and avoid those with excessive fillers or artificial additives. It's also important to account for treat calories in your dog's overall diet to prevent weight gain.",
                "Treats", "2024-08-25");

        addArticle(db, "Choosing the Right Dog Food", "James Anderson",
                "Selecting the right dog food involves considering your dog's age, size, activity level, and any specific health needs. Look for foods that are appropriate for your dog's life stage (puppy, adult, senior) and avoid those with excessive fillers or artificial additives. A balanced diet with high-quality protein, fats, and essential vitamins and minerals is key.",
                "Dog Food", "2024-09-30");
    }


    private long addArticle(SQLiteDatabase db, String title, String author, String content, String category, String publicationDate) {
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(AUTHOR, author);
        values.put(CONTENT, content);
        values.put(CATEGORY, category);
        values.put(PUBLICATION_DATE, publicationDate);

        return db.insert(TABLE_NAME, null, values);
    }
}
