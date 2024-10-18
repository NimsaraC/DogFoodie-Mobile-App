package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dogefoodie.MainActivity;
import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;

public class UserProfile extends AppCompatActivity {

    private TextView txtUsername, txtEmail;
    private LinearLayout btnToShip, btnShipped, btnHistory, btnSetAddress, btnAddArticle, btnLogout;
    private ImageView btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        btnToShip = findViewById(R.id.btnToShip);
        btnShipped = findViewById(R.id.btnShipped);
        btnHistory = findViewById(R.id.btnHistory);
        btnSetAddress = findViewById(R.id.btnSetAddress);
        btnAddArticle = findViewById(R.id.btnAddArticle);
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);

        setUserData();

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrderHistory.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfileEdit.class);
                startActivity(intent);
                finish();
            }
        });

        btnAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserAddArticle.class);
                startActivity(intent);
            }
        });

        btnSetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Soon!!!", Toast.LENGTH_SHORT).show();
            }
        });
        btnToShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrder.class);
                intent.putExtra("status", "ToShip");
                startActivity(intent);
            }
        });
        btnShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrder.class);
                intent.putExtra("status", "Shipped");
                startActivity(intent);
            }
        });
    }

    private void setUserData(){
        SharedPreference preference = new SharedPreference();
        txtUsername.setText(preference.getString(getApplicationContext(), SharedPreference.KEY_NAME));
        txtEmail.setText(preference.getString(getApplicationContext(), SharedPreference.KEY_EMAIL));
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("your_preference_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(SharedPreference.KEY_NAME);
        editor.remove(SharedPreference.KEY_ID);
        editor.remove(SharedPreference.KEY_EMAIL);
        editor.remove(SharedPreference.KEY_STATUS);
        editor.remove(SharedPreference.PHONE);
        editor.remove(SharedPreference.NAME);
        editor.remove(SharedPreference.NO);
        editor.remove(SharedPreference.STREET);
        editor.remove(SharedPreference.CITY);
        editor.remove(SharedPreference.ZIP);
        editor.remove(SharedPreference.TOTAL);
        editor.remove(SharedPreference.CARD_HOLDER);
        editor.remove(SharedPreference.CVV);
        editor.remove(SharedPreference.CARD_NUMBER);
        editor.remove(SharedPreference.EXPIRE);

        editor.apply();

        Intent intent = new Intent(this, UserLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}