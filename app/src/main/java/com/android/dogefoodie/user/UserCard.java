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

public class UserCard extends AppCompatActivity {

    private EditText etCardNumber, etCardName, etExpiryDate, etCvv;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);

        etCardNumber = findViewById(R.id.etCardNumber);
        etCardName = findViewById(R.id.etCardName);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCvv = findViewById(R.id.etCvv);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = etCardNumber.getText().toString().trim();
                String cardName = etCardName.getText().toString().trim();
                String expiryDate = etExpiryDate.getText().toString().trim();
                String cvv = etCvv.getText().toString().trim();

                SharedPreference preference =  new SharedPreference();

                if(cardNumber.isEmpty() || cardNumber.length() != 16) {
                    Toast.makeText(UserCard.this, "Enter a valid 16-digit card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cardName.isEmpty()) {
                    Toast.makeText(UserCard.this, "Enter the cardholder name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(expiryDate.isEmpty() || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                    Toast.makeText(UserCard.this, "Enter a valid expiry date in MM/YY format", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cvv.isEmpty() || cvv.length() != 3) {
                    Toast.makeText(UserCard.this, "Enter a valid 3-digit CVV", Toast.LENGTH_SHORT).show();
                    return;
                }

                preference.SaveString(getApplicationContext(),cardName,SharedPreference.CARD_HOLDER);
                preference.SaveString(getApplicationContext(),cardNumber,SharedPreference.CARD_NUMBER);
                preference.SaveString(getApplicationContext(),expiryDate,SharedPreference.EXPIRE);
                preference.SaveString(getApplicationContext(),cvv,SharedPreference.CVV);

                Intent intent = new Intent(getApplicationContext(), UserOrderConfirm.class);
                startActivity(intent);

                Toast.makeText(UserCard.this, "Card Details Submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
