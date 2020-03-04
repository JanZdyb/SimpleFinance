package com.example.simplefinance;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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


public class FirstEnterActivity extends AppCompatActivity {

    private TextView politics;
    private EditText passwordTV;
    private Button cont;
    private String _password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_enter);

        passwordTV = (EditText)findViewById(R.id.passwordTV);

        politics = (TextView) findViewById(R.id.politics);
        politics.setText("Chciałbyś jak najłatwiej zapisywać swoje codzienne wydatki " +
                "bez rozdrabniania i myślenia czy deska do " +
                "krojenia zalicza się do produktów spożywczych? \n\nZacznij korzystać już dziś");

        cont = (Button)findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _password = passwordTV.getText().toString();
                if(!_password.isEmpty() && _password.length() >= 4)
                {
                    cont.setVisibility(View.GONE);

                    createPasswordPreferences(_password);
                    createDaysPreferences();
                    createDatabasePreference();
                    createCurrencyPrference();
                    createAcitivtyPreference();
                    createIfDBChange();
                    biometricAlert();
                }
                else
                {
                    cont.setVisibility(View.GONE);
                    Toast.makeText(FirstEnterActivity.this, "Hasło musi mieć minimum 4 znaki", Toast.LENGTH_SHORT).show();
                    cont.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void biometricAlert()
    {
        final AlertDialog.Builder biometric = new AlertDialog.Builder(this);
        biometric.setMessage("Chciałbyś logować się za pomocą odcisku palca?");

        if(canAuthenticate())
        {
            biometric.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), BiometricActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            biometric.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), MainDesk.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog biometricDialog = biometric.create();
            biometricDialog.show();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), MainDesk.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean canAuthenticate()
    {
        if (android.os.Build.VERSION.SDK_INT >= 22) {
            androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                    return true;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.e("MY_APP_TAG", "No biometric features available on this device.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Log.e("MY_APP_TAG", "The user hasn't associated " +
                            "any biometric credentials with their account.");
                    break;
            }
        }
        return false;
    }

    private void createAcitivtyPreference()
    {
        SharedPreferences ACTIVITY = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor ACTIVITYeditor = ACTIVITY.edit();
        ACTIVITYeditor.putInt("activitypreference", 0);
        ACTIVITYeditor.apply();
    }

    private void createDatabasePreference()
    {
        SharedPreferences DATABASE = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor DATABASEeditor = DATABASE.edit();
        DATABASEeditor.putString("databasepreference", "yes");
        DATABASEeditor.apply();
    }

    private void createCurrencyPrference()
    {
        SharedPreferences CURRENCY = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor CURRENCYeditor = CURRENCY.edit();
        CURRENCYeditor.putString("currencypreference", "PLN");
        CURRENCYeditor.apply();
    }
    private void createPasswordPreferences(String password)
    {
        SharedPreferences PASS = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor PASSeditor = PASS.edit();
        PASSeditor.putString("passwordpreferences", password);
        PASSeditor.apply();
    }
    private void createDaysPreferences()
    {
        SharedPreferences INTEGER = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor INTEGEReditor = INTEGER.edit();
        INTEGEReditor.putInt("daypreferences", 1);
        INTEGEReditor.apply();
    }

    private void createIfDBChange()
    {
        SharedPreferences DBCHANGE = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor DBCHANGEeditor = DBCHANGE.edit();
        DBCHANGEeditor.putInt("ifdbpreference", 0);
        DBCHANGEeditor.apply();
    }
}
