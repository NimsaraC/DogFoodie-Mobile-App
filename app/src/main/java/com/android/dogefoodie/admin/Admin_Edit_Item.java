package com.android.dogefoodie.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.dogefoodie.Product;
import com.android.dogefoodie.R;
import com.android.dogefoodie.database.ProductDB;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Admin_Edit_Item extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ProductDB productDB;
    private Uri imageUri;
    private EditText name, price, quantity, description;
    private Button imgButton, saveButton;
    private ImageView productImg;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_item);

        productDB = new ProductDB(this);

        name = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        price = findViewById(R.id.editTextPrice);
        quantity = findViewById(R.id.editTextQuantity);
        description = findViewById(R.id.editTextDescription);
        productImg = findViewById(R.id.imageViewPreview);
        imgButton = findViewById(R.id.buttonUploadImage);
        saveButton = findViewById(R.id.button6);

        setupCategorySpinner();
        setupProductDetails();

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String productId = intent.getStringExtra("product_id");
                Product product= productDB.getProductById(Integer.parseInt(productId));
                String productName = name.getText().toString();
                String productCategory = spinnerCategory.getSelectedItem().toString();
                String productPrice = price.getText().toString();
                String productQuantity = quantity.getText().toString();
                String productDescription = description.getText().toString();

                if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productCategory) || TextUtils.isEmpty(productPrice) ||
                        TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(productDescription)) {
                    Toast.makeText(Admin_Edit_Item.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(productPrice);
                int quantity = Integer.parseInt(productQuantity);

                String imageUrl = imageUri != null ? saveImageToInternalStorage() : product.getImageUrl();
                addItemToDatabase(productName, productCategory, price, productDescription, quantity, imageUrl, 0.0);

                Toast.makeText(Admin_Edit_Item.this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
                //finish();
                Intent intent1 = new Intent(Admin_Edit_Item.this, Admin_Product_List.class);
                startActivity(intent1);
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

    private void setupProductDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            String productId = intent.getStringExtra("product_id");
             Product product= productDB.getProductById(Integer.parseInt(productId));

            name.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));
            description.setText(product.getDescription());
            loadImageWithPicasso(product.getImageUrl());

            quantity.setText(String.valueOf(product.getQuantity()));

            String productCategory = product.getCategory();
            if (productCategory != null) {
                int spinnerPosition = ((ArrayAdapter<String>)spinnerCategory.getAdapter()).getPosition(productCategory);
                spinnerCategory.setSelection(spinnerPosition);
            }
        }
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
                productImg.setImageBitmap(bitmap);
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

    private void loadImageWithPicasso(String productImageUrl) {
        if (productImageUrl != null) {
            if (productImageUrl.startsWith("drawable/")) {
                String drawableName = productImageUrl.replace("drawable/", "").replace(".jpg", "");
                int drawableResId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
                if (drawableResId != 0) {
                    productImg.setImageResource(drawableResId);
                } else {
                    productImg.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else if (productImageUrl.startsWith("/")) {
                Picasso.get()
                        .load(new File(productImageUrl))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(productImg, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Error loading image", e);
                            }
                        });
            } else {
                Picasso.get()
                        .load(productImageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(productImg, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Error loading image", e);
                            }
                        });
            }
        } else {
            productImg.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private boolean addItemToDatabase(String name, String category, double price, String description, int quantity, String imageUrl, double rating) {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("product_id");
        int id = Integer.parseInt(productId);
        productDB.editProduct(id,name, category, price, description, quantity, imageUrl, rating);
        return true;
    }
}