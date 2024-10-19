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
import com.android.dogefoodie.User;
import com.android.dogefoodie.admin.AdminPanel;
import com.android.dogefoodie.admin.Admin_Add_Item;
import com.android.dogefoodie.admin.Admin_Product_List;
import com.android.dogefoodie.database.UserDB;

import java.util.List;

public class UserLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signupButton;
    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.btn3);
        signupButton = findViewById(R.id.btnsignup);

        userDB = new UserDB(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();


                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UserLogin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if(email.equals("dogfoodie@gmail.com") && password.equals("1234")){
                    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
                    startActivity(intent);
                }else{
                    boolean isAuthenticated = authenticateUser(email, password);
                    if (isAuthenticated) {
                        Toast.makeText(UserLogin.this, "Login successful", Toast.LENGTH_SHORT).show();

                        GetInfo(email);
                        Intent intent = new Intent(getApplicationContext(), UserMain.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserLogin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegistration.class);
                startActivity(intent);
            }
        });
    }

    private boolean authenticateUser(String email, String password) {
        List<User> userList = userDB.getAllUsers();
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void GetInfo(String email){
        SharedPreference preference = new SharedPreference();

        User user = userDB.getUserByEmail(email);

        preference.SaveString(getApplicationContext(),email,SharedPreference.KEY_EMAIL);
        preference.SaveString(getApplicationContext(), String.valueOf(user.getId()),SharedPreference.KEY_ID);
        preference.SaveString(getApplicationContext(),user.getName(),SharedPreference.KEY_NAME);
    }
}
