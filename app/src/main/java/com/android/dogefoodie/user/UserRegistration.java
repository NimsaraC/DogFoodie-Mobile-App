package com.android.dogefoodie.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.dogefoodie.R;
import com.android.dogefoodie.SharedPreference;
import com.android.dogefoodie.database.UserDB;

public class UserRegistration extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, phoneEditText;
    private Button signupButton;
    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        signupButton = findViewById(R.id.btn2);

        userDB = new UserDB(this);
        SharedPreference preference = new SharedPreference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();


                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(UserRegistration.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {

                    long result = userDB.userRegistration(username, email, password, phone);
                    if (result != -1) {
                        Toast.makeText(UserRegistration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserRegistration.this, "Registration failed, try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
