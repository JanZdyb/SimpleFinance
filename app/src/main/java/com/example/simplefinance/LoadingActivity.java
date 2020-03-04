package com.example.simplefinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private String password, fingerprint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences FINGERPRINT = getSharedPreferences("BASIC", 0);
        fingerprint = FINGERPRINT.getString("fingerprintpreferences", "");

        SharedPreferences PASS = getSharedPreferences("BASIC", 0);
        password = PASS.getString("passwordpreferences", "");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(password.equals(""))
                {
                    Intent intent = new Intent(getApplicationContext(), FirstEnterActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(fingerprint.equals("yes"))
                {
                    Intent intent = new Intent(getApplicationContext(), BiometricActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
