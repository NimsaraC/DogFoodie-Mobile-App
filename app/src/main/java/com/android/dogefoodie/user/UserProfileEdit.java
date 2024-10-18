package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.User;
import com.android.dogefoodie.database.UserDB;

public class UserProfileEdit extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtPhone;
    private LinearLayout btnSubmit;
    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        userDB = new UserDB(this);

        edtName = findViewById(R.id.txtEditName);
        edtEmail = findViewById(R.id.txtEditEmail);
        edtPassword = findViewById(R.id.txtEditPassword);
        edtPhone = findViewById(R.id.txtEditPhone);
        btnSubmit = findViewById(R.id.btnSubmit);

        SharedPreference sharedPreference = new SharedPreference();
        String Uid = sharedPreference.getString(getApplicationContext(), SharedPreference.KEY_ID);
        int id = Integer.parseInt(Uid);

        setupUserDetails(id);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String phone = edtPhone.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    EditUser(id, name, email, password, phone);
                }
            }
        });
    }

    private void setupUserDetails(int id) {
        User user = userDB.getUserById(id);

        if (user != null) {
            edtName.setText(user.getName());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            edtPassword.setText(user.getPassword());
        } else {
            Toast.makeText(getApplicationContext(), "User not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void EditUser(int id, String name, String email, String password, String phone) {
        userDB.editUser(id, name, email, password, phone);
        Toast.makeText(getApplicationContext(), "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();

        SharedPreference preference = new SharedPreference();
        preference.SaveString(getApplicationContext(), email, SharedPreference.KEY_EMAIL);
        preference.SaveString(getApplicationContext(), String.valueOf(id), SharedPreference.KEY_ID);
        preference.SaveString(getApplicationContext(), name, SharedPreference.KEY_NAME);

        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(intent);
        finish();
    }
}
