package com.android.dogefoodie.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.dogefoodie.R;
import com.android.dogefoodie.database.ProductDB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Admin_Add_Item extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ProductDB productDB;
    private ImageView imageViewPreview;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_item);

        productDB = new ProductDB(this);

        EditText editTextName = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        EditText editTextQuantity = findViewById(R.id.editTextQuantity);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        imageViewPreview = findViewById(R.id.imageViewPreview);
        Button buttonUploadImage = findViewById(R.id.buttonUploadImage);
        Button buttonAddItem = findViewById(R.id.button6);

        setupCategorySpinner();

        buttonUploadImage.setOnClickListener(v -> openImageChooser());

        buttonAddItem.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();
            String priceText = editTextPrice.getText().toString();
            String quantityText = editTextQuantity.getText().toString();
            String description = editTextDescription.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(priceText) ||
                    TextUtils.isEmpty(quantityText) || TextUtils.isEmpty(description)) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            String imageUrl = imageUri != null ? saveImageToInternalStorage() : "";
            boolean success = addItemToDatabase(name, category, price, description, quantity, imageUrl, 0.0);

            if (success) {
                Toast.makeText(Admin_Add_Item.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Admin_Add_Item.this, "Failed to add item. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategorySpinner() {
        String[] categories = new String[]{
                "Dog Food",
                "Treats",
                "Toys",
                "Supplements",
                "Grooming"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_selected_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private String saveImageToInternalStorage() {
        if (imageUri == null) return "";

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            String filename = UUID.randomUUID().toString() + ".jpg";
            File file = new File(getFilesDir(), filename);

            try (OutputStream outputStream = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }

            return file.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return "";
        }
    }

    private boolean addItemToDatabase(String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        long result = productDB.addProduct(name, category, price, description, quantity, imageUrl, rating);
        return result != -1;
    }
}
