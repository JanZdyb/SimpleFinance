package com.example.simplefinance;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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


public class SettingsActivity extends AppCompatActivity {

    private Button changePasswordButton, fingButton, deleteAccount, changeCurrency, deleteDatabase, darkTheme;
    private String newpass;
    private String fingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changeCurrency = (Button)findViewById(R.id.changeCurrency);
        changePasswordButton = (Button) findViewById(R.id.changepassword);
        fingButton = (Button)findViewById(R.id.fingButton);
        deleteAccount = (Button)findViewById(R.id.deleteAccount);
        deleteDatabase = (Button)findViewById(R.id.deleteDatabase);
        darkTheme = (Button)findViewById(R.id.darkTheme);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);

                View mView = getLayoutInflater().inflate(R.layout.change_password, null);

                final EditText newPassword = (EditText) mView.findViewById(R.id.editText2);

                Button accept = (Button) mView.findViewById(R.id.accept);
                Button cancel = (Button) mView.findViewById(R.id.cancel);

                mBuilder.setView(mView);

                final AlertDialog passwordDialog = mBuilder.create();

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        newpass = newPassword.getText().toString();
                        if (newpass.length() < 3) {
                            Toast.makeText(SettingsActivity.this, "Hasło powinno mieć minimum 4 znaki.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (updatePassword(newpass)) {
                                Toast.makeText(SettingsActivity.this, "Hasło zmienione.", Toast.LENGTH_SHORT).show();
                                passwordDialog.dismiss();
                            } else {
                                Toast.makeText(SettingsActivity.this, "Natpotkano błąd", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordDialog.dismiss();
                    }
                });

                passwordDialog.show();
            }
        });

        fingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFingerprintPreference();

            }
        });

        changeCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);

                View mView = getLayoutInflater().inflate(R.layout.change_currency, null);

                TextView euro = (TextView) mView.findViewById(R.id.euro);
                TextView dolars = (TextView) mView.findViewById(R.id.dolars);
                TextView pln = (TextView) mView.findViewById(R.id.PLN);

                mBuilder.setView(mView);

                final AlertDialog currencyDialog = mBuilder.create();

                euro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        changeCurrencyPreferences("€");
                        currencyDialog.dismiss();
                    }
                });
                dolars.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        changeCurrencyPreferences("$");
                        currencyDialog.dismiss();
                    }
                });
                pln.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        changeCurrencyPreferences("PLN");
                        currencyDialog.dismiss();
                    }
                });

                currencyDialog.show();
            }
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountAlert();
            }
        });

        deleteDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDatabaseAlert();
            }
        });

        darkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkThemeAlert();
            }
        });

    }

    private void darkThemeAlert()
    {
        final android.app.AlertDialog.Builder accountAlert = new android.app.AlertDialog.Builder(this);
        accountAlert.setMessage("Chciałbyś włączyć tryb ciemny?");

        accountAlert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //tryb ciemny
            }
        });
        accountAlert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        accountAlert.show();
    }
    private void updateValueToPreferences(float value)
    {
        SharedPreferences VALUE = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor VALUEeditor = VALUE.edit();

        VALUEeditor.putFloat("valuepreference", value);
        VALUEeditor.apply();
    }

    private void deleteDatabaseAlert()
    {
        final android.app.AlertDialog.Builder accountAlert = new android.app.AlertDialog.Builder(this);
        accountAlert.setMessage("Czy na pewno chcesz usunąć bazę danych?");

        accountAlert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateValueToPreferences(0);
                deleteDatabase();

            }
        });
        accountAlert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        accountAlert.show();
    }

    private void deleteAccountAlert()
    {
        final android.app.AlertDialog.Builder accountAlert = new android.app.AlertDialog.Builder(this);
        accountAlert.setMessage("Czy na pewno chcesz usunąć konto i wszystkie powiązane z nim rzeczy?");

        accountAlert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                });
        accountAlert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
        });
        accountAlert.show();


    }

    private void changeCurrencyPreferences(String preference)
    {
        SharedPreferences CURRENCY = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor CURRENCYeditor = CURRENCY.edit();

        CURRENCYeditor.putString("currencypreference", preference);
        CURRENCYeditor.apply();
    }

    private void deleteDatabase()
    {
        this.deleteDatabase("MY_TEST_DB");
    }

    private void getFingerprintPreference()
    {
        SharedPreferences FINGERPRINT = getSharedPreferences("BASIC", 0);
        fingerprint = FINGERPRINT.getString("fingerprintpreferences", "");

        if(fingerprint.equals("yes"))
        {
            biometricAlert("1");
        }
        else
        {
            biometricAlert("0");
        }
    }

    private boolean updatePassword(String password) {
        try {

            SharedPreferences PASS = getSharedPreferences("BASIC", 0);
            SharedPreferences.Editor editor = PASS.edit();

            editor.putString("passwordpreferences", password);
            editor.apply();
            return true;

        } catch (Exception e) {
            Toast.makeText(this, "exception" + e, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void biometricAlert(String state) {
        final android.app.AlertDialog.Builder biometric = new android.app.AlertDialog.Builder(this);
        if(state.equals("0")) {
            biometric.setMessage("Chciałbyś logować się za pomocą odcisku palca?");

            if (canAuthenticate()) {
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

                    }
                });
                android.app.AlertDialog biometricDialog = biometric.create();
                biometricDialog.show();
            }
            else
            {
                Toast.makeText(this, "Twój telefon nie wspiera technologii linii papilarnych.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            biometric.setMessage("Posiadasz już odcisk palca, czy chciałbyś go usunąć?");

            if (canAuthenticate()) {
                biometric.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFingerprintPreferences();
                    }
                });
                biometric.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                android.app.AlertDialog biometricDialog = biometric.create();
                biometricDialog.show();
            }
            else
            {
                Toast.makeText(this, "Twój telefon nie wspiera technologii linii papilarnych.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainDesk.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out, R.anim.slide_out_down);
        finish();
    }

    private void deleteFingerprintPreferences()
    {

        SharedPreferences preferences = getSharedPreferences("BASIC", 0);
        preferences.edit().remove("fingerprintpreferences").apply();


        Toast.makeText(this, "Usunięto odcisk palca.", Toast.LENGTH_SHORT).show();
    }

    private void deleteAccount()
    {
        SharedPreferences preferences = getSharedPreferences("BASIC", 0);
        preferences.edit().clear().apply();

        deleteDatabase();

        Intent intent = new Intent(getApplicationContext(), FirstEnterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
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
}
