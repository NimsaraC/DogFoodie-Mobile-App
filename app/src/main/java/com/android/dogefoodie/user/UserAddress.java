package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.admin.Admin_Add_Item;

public class UserAddress extends AppCompatActivity {

    TextView txtName, txtPhone, txtNo, txtStreet, txtCity, txtPostal;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        txtName = findViewById(R.id.etUserName);
        txtPhone = findViewById(R.id.etPhone);
        txtNo = findViewById(R.id.etNo);
        txtStreet = findViewById(R.id.etStreet);
        txtCity = findViewById(R.id.etCity);
        txtPostal = findViewById(R.id.etPostal);

        btnSubmit = findViewById(R.id.btnSubmit);

        SharedPreference preference = new SharedPreference();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preference.SaveString(getApplicationContext(),txtName.getText().toString(), SharedPreference.NAME);
                preference.SaveString(getApplicationContext(),txtPhone.getText().toString(), SharedPreference.PHONE);
                preference.SaveString(getApplicationContext(),txtNo.getText().toString(), SharedPreference.NO);
                preference.SaveString(getApplicationContext(),txtStreet.getText().toString(), SharedPreference.STREET);
                preference.SaveString(getApplicationContext(),txtCity.getText().toString(), SharedPreference.CITY);
                preference.SaveString(getApplicationContext(),txtPostal.getText().toString(), SharedPreference.ZIP);

                Toast.makeText(getApplicationContext(), "Address saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



    }
}