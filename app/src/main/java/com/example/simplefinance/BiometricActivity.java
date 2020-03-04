package com.example.simplefinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class BiometricActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Button biometricLoginButton, backToPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        biometricLoginButton = (Button)findViewById(R.id.verifyFingerprint);
        backToPassword = (Button)findViewById(R.id.backToPassword);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(BiometricActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Zweryfikowano!", Toast.LENGTH_SHORT).show();

                createFingerprintPreference();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Weryfikacja nieudana",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Logowanie za pomocą odcisku palca")
                .setSubtitle("Zaloguj używając skanera")
                .setNegativeButtonText("Użyj tylko hasła")
                .build();

        biometricLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }

        });
        backToPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createFingerprintPreference()
    {
        SharedPreferences FINGERPRINT = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor FINGERPRINTeditor = FINGERPRINT.edit();

        FINGERPRINTeditor.putString("fingerprintpreferences", "yes");
        FINGERPRINTeditor.apply();

        Toast.makeText(BiometricActivity.this, "Odczytano!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainDesk.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Przerwano odczytywanie odcisku palca", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
        startActivity(intent);
        finish();
    }
}
