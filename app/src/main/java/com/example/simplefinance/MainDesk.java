package com.example.simplefinance;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainDesk extends AppCompatActivity {

    private TextView moneySpent, todayAdded, temporaryTextview;
    private ImageView addAnDay;
    private int news, todayAdd;
    private String strDate, strTime, strOnlyMonth, strWholeDate;
    private Button lookDays, settings, specificDay, tdayAdded;
    private TextInputEditText inputName, inputPrice;
    private String productName, productPrice;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_desk);
        settings = (Button) findViewById(R.id.settings);
        addAnDay = (ImageView) findViewById(R.id.addAnDay);
        lookDays = (Button) findViewById(R.id.lookDays);
        tdayAdded = (Button)findViewById(R.id.tdayAdded);

        inputName = (TextInputEditText) findViewById(R.id.inputName);
        inputPrice = (TextInputEditText) findViewById(R.id.inputPrice);
        moneySpent = (TextView)findViewById(R.id.moneySpent);
        todayAdded = (TextView)findViewById(R.id.todayAdded);
        temporaryTextview = (TextView)findViewById(R.id.temporaryTextview);
        temporaryTextview.setText("Zacznij od dodania pierwszej kupionej dziś rzeczy masz nad nią później pełną kontrolę. ");
        getDateAndTime();
        createDatabase();
        updateMoneySpent();


        checkNewsPreferences();
        getDatabasePreference();
        inputName.requestFocus();
        todayAdd = getAddCounterFromDatabase();
        todayAdded.setText("Dodanych rzeczy: "+todayAdd);

        addAnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = inputName.getText().toString();
                productPrice = inputPrice.getText().toString();

                if(productName.equals("") || productPrice.equals(""))
                {
                    Toast.makeText(MainDesk.this, "Wypełnij oba pola!", Toast.LENGTH_SHORT).show();
                }
                else {
                    temporaryTextview.setVisibility(View.GONE);
                    getDateAndTime();
                    insertIntoDatabase(productName, productPrice, strWholeDate, strTime);

                    updateMoneySpent();
                    getFromDatabase();
                    inputName.setText("");
                    inputPrice.setText("");
                    inputName.requestFocus();
                    getAddCounterFromDatabase();
                    updateTodayAdded();
                    updateIfDBChange(1);

                }
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewsPreferences();

                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_out_up);
            }
        });

        lookDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewsPreferences();
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        tdayAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateAndTime();
                updateActivityPreference(0);
                Intent intent = new Intent(getApplicationContext(), SpecificDay.class);
                intent.putExtra("date", strWholeDate);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    private void updateActivityPreference(int number)
    {
        SharedPreferences ACTIVITY = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor ACTIVITYEeditor = ACTIVITY.edit();

        ACTIVITYEeditor.putInt("activitypreference", number);
        ACTIVITYEeditor.apply();
    }

    private void siema()
    {

    }

    private int getAddCounterFromDatabase()
    {
        int addCounter = 0;
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select Date from TEST_TABLE where Date = \"" + strWholeDate + "\"";
        Cursor c = db.rawQuery(sqlquery, null);
        if (c != null) {
            while (c.moveToNext()) {
                addCounter = c.getCount();
            }
        }
        c.close();
        return addCounter;
    }


    private void updateTodayAdded()
    {
            todayAdded.setText("Dodanych rzeczy: "+getAddCounterFromDatabase());
    }

    private void updateMoneySpent()
    {
        moneySpent.setText("Wydałeś dziś: "+ getSumFromDatabase(strWholeDate) + " " +getCurrencyPreference());
    }

    private float getSumFromDatabase(String choosenDate)
    {
        float sumMoneySpent = 0;
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select ProductPrice from TEST_TABLE where Date = \"" + choosenDate + "\"";
        Cursor c = db.rawQuery(sqlquery, null);
        if (c != null) {
            while (c.moveToNext()) {
                String pPrice = c.getString(c.getColumnIndex("ProductPrice"));

                sumMoneySpent += Float.valueOf(pPrice);
            }
        }
        c.close();
        return sumMoneySpent;
    }

    private void updateIfDBChange(int dbChanged)
    {
        SharedPreferences DBCHANGE = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor DBCHANGEeditor = DBCHANGE.edit();

        DBCHANGEeditor.putInt("ifdbpreference", dbChanged);
        DBCHANGEeditor.apply();
    }


    private void getFromDatabase()
    {
        String sqlquery = "select * from TEST_TABLE";
        Cursor c = db.rawQuery(sqlquery, null);
        if(c != null) {
            String msg = "";
            while (c.moveToNext()) {
                String pNAME = c.getString(c.getColumnIndex("ProductName"));
                String pPrice = c.getString(c.getColumnIndex("ProductPrice"));
                String DATE = c.getString(c.getColumnIndex("Date"));
                String TIME = c.getString(c.getColumnIndex("Time"));

                msg = pNAME + " " + pPrice + " " + TIME + " " + DATE + " ";

            }
            Toast.makeText(MainDesk.this, "Pomyślnie dodano do bazy", Toast.LENGTH_SHORT).show();
        }
        else
        {

        }
        c.close();
    }

    private void insertIntoDatabase(String ProductName, String ProductPrice, String Date, String Time)
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String insertFULL = ("insert into TEST_TABLE (ProductName, ProductPrice, Date, Time) values (\"" + ProductName + "\"" + ", " + "\"" + ProductPrice + "\"" + ", " + "\"" + Date + "\"" + ", " + "\"" + Time + "\")");
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        db.execSQL(insertFULL);

    }

    private void createDatabase()
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sql = "create table if not exists TEST_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, ProductName VARCHAR, ProductPrice VARCHAR, Date VARCHAR, Time, VARCHAR)";
        db.execSQL(sql);
    }

    private void getDatabasePreference()
    {
        SharedPreferences DATABASE = getSharedPreferences("BASIC", 0);
        String database = DATABASE.getString("databasepreferences", "");

        if(database.equals("yes"))
        {
            temporaryTextview.setVisibility(View.GONE);
        }
        else
        {
            createDatabase();
        }
    }
    private void checkNewsPreferences()
    {
        SharedPreferences NEWS = getSharedPreferences("BASIC", 0);
        news = NEWS.getInt("newspreferences", 0);

        if(news == 1)
        {
            temporaryTextview.setVisibility(View.GONE);
        }
        else
        {
            createNewsPreferences();
        }
    }

    private void createNewsPreferences()
    {
        SharedPreferences NEWS = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor NEWSeditor = NEWS.edit();
        NEWSeditor.putInt("newspreferences", 1);
        NEWSeditor.apply();
    }

    private void getDateAndTime()
    {
        Calendar date = Calendar.getInstance();
        Calendar time = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat monthOnlyFormat = new SimpleDateFormat("MM");
        SimpleDateFormat wholeFormat = new SimpleDateFormat("dd.MM.yyyy");

        strDate = dateFormat.format(date.getTime());
        strTime = timeFormat.format(time.getTime());
        strOnlyMonth = monthOnlyFormat.format(date.getTime());
        strWholeDate = wholeFormat.format(date.getTime());
        strWholeDate = wholeFormat.format(date.getTime());
    }


    private String getCurrencyPreference() {
        SharedPreferences CURRENCY = getSharedPreferences("BASIC", 0);
        String currency = CURRENCY.getString("currencypreference", "PLN");
        return currency;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
