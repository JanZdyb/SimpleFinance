package com.example.simplefinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPassword extends AppCompatActivity {

    private EditText passwordTV;
    private String _password, sharedpassword;
    private Button cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        passwordTV = (EditText) findViewById(R.id.passwordTV);
        cont = (Button) findViewById(R.id.login);

        final SharedPreferences password = getSharedPreferences("BASIC", 0);
        sharedpassword = password.getString("passwordpreferences", "");

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _password = passwordTV.getText().toString();

                if (_password.equals(sharedpassword)) {
                    Intent intent = new Intent(getApplicationContext(), MainDesk.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPassword.this, "Nieprawidłowe hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

