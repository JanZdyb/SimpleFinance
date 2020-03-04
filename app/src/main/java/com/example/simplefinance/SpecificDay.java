package com.example.simplefinance;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SpecificDay extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    SQLiteDatabase db;
    private Button refresh, deleteDay, addOrEdit;
    private ListView listView;
    private String choosenDate;
    private TextView sumOfTheDay, daysFromDB;
    private boolean isToday = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_day);
        listView = (ListView)findViewById(R.id.listView);
        refresh = (Button)findViewById(R.id.refresh);
        deleteDay = (Button)findViewById(R.id.deleteDay);
        addOrEdit = (Button)findViewById(R.id.addOrEdit);
        sumOfTheDay = (TextView)findViewById(R.id.sumOfTheDay);
        daysFromDB = (TextView)findViewById(R.id.daysFromDB);
        choosenDate = getIntent().getExtras().getString("date");
        daysFromDB.setText("Wybrano "+choosenDate);

        if(choosenDate.equals(getCurrentDate()))
        {
            addOrEdit.setText("Dodaj");
            isToday = true;
        }
        else
        {
            //pozostaw Edytuj
        }

        if(!doesDatabaseExist(getApplicationContext(), "MY_TEST_DB"))
        {

        }
        else {
            getFromDatabase();
            updateMoneySpent();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doesDatabaseExist(getApplicationContext(), "MY_TEST_DB"))
                {

                }
                else
                {
                    if(getIfDBChange() == 1) {
                        getFromDatabase();
                        updateIfDBChange(0);
                        updateMoneySpent();
                    }
                    else
                    {
                        //jezeli baza nie byla zmieniana nie rob nic klikajac odswiez
                    }
                }
            }
        });

        deleteDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDayAlert();
            }
        });

        addOrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isToday)
                {
                    onBackPressed();
                }
                else
                {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(SpecificDay.this);

                    View mView = getLayoutInflater().inflate(R.layout.activity_add_day, null);

                    final TextInputEditText pName = (TextInputEditText) mView.findViewById(R.id.productNameInput);
                    final TextInputEditText pPrice = (TextInputEditText) mView.findViewById(R.id.productPriceInput);
                    ImageView pAdd = (ImageView) mView.findViewById(R.id.addToSpecificDay);

                    mBuilder.setView(mView);

                    final AlertDialog addDialog = mBuilder.create();

                    pAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                           final String name = pName.getText().toString();
                           final String price = pPrice.getText().toString();

                            if(name.equals("") || price.equals(""))
                            {
                                Toast.makeText(SpecificDay.this, "Wypełnij oba pola!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                insertIntoDatabase(name, price, choosenDate, getCurrentTime());
                                pName.setText("");
                                pPrice.setText("");
                                pName.requestFocus();
                                updateIfDBChange(1);
                                Toast.makeText(SpecificDay.this, name + " " + price + " " + choosenDate + " " + getCurrentTime(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    addDialog.show();
                }

            }
        });
    }

    private void insertIntoDatabase(String ProductName, String ProductPrice, String Date, String Time)
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String insertFULL = ("insert into TEST_TABLE (ProductName, ProductPrice, Date, Time) values (\"" + ProductName + "\"" + ", " + "\"" + ProductPrice + "\"" + ", " + "\"" + Date + "\"" + ", " + "\"" + Time + "\")");
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        db.execSQL(insertFULL);

    }

    private String getCurrentTime()
    {
        Calendar time = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String ntime = timeFormat.format(time.getTime());
        return ntime;
    }

    private String getCurrentDate()
    {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat wholeFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = wholeFormat.format(date.getTime());
        return currentDate;
    }

    private int getActivityPreference()
    {
        SharedPreferences ACTIVITY = getSharedPreferences("BASIC", 0);
        int number = ACTIVITY.getInt("activitypreference", 0);
        return number;
    }

    private void updateMoneySpent()
    {
        float sumOfTheDayInt = getSumFromDatabase();
        String sumOfTheDayString = String.valueOf(sumOfTheDayInt);
        sumOfTheDay.setText(sumOfTheDayString + " " + getCurrencyPreference());
    }

    private void updateIfDBChange(int dbChanged)
    {
        SharedPreferences DBCHANGE = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor DBCHANGEeditor = DBCHANGE.edit();

        DBCHANGEeditor.putInt("ifdbpreference", dbChanged);
        DBCHANGEeditor.apply();
    }

    private int getIfDBChange()
    {
        SharedPreferences DBCHANGE = getSharedPreferences("BASIC", 0);
        final int dbChanged = DBCHANGE.getInt("ifdbpreference", 0);
        return dbChanged;
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath("MY_TEST_DB");
        return dbFile.exists();
    }

    private void deleteDayAlert()
    {
        final android.app.AlertDialog.Builder deleteDatabase = new android.app.AlertDialog.Builder(this);
        deleteDatabase.setMessage("Czy na pewno chcesz usunąć dany dzień wydatków?");

        deleteDatabase.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFromDatabase(choosenDate);
                deletedFromList();
            }
        });
        deleteDatabase.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        deleteDatabase.show();
    }

    private void deleteFromDatabase(String choosenDate) {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "delete from TEST_TABLE where Date like \"" + choosenDate + "\"";
        Cursor c = db.rawQuery(sqlquery, null);
        if (c != null) {
            while (c.moveToNext()) {
            }
        }
    }

    private void getFromDatabase() {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select ID, ProductName, ProductPrice from TEST_TABLE where Date = \"" + choosenDate + "\"";
        Cursor c = db.rawQuery(sqlquery, null);
        if (c != null) {
            String msg = "";
            final ArrayList<String> stringArrayList = new ArrayList<String>();
            while (c.moveToNext()) {
                String pNAME = c.getString(c.getColumnIndex("ProductName"));
                String pPrice = c.getString(c.getColumnIndex("ProductPrice"));

                msg = pNAME + " " + pPrice;
                stringArrayList.add(msg + " " + getCurrencyPreference());
            }

        adapter = new ArrayAdapter<String>(this, R.layout.element, R.id.Row, stringArrayList);

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                long itemID = getProductID(position);

                deleteFromListAlert(itemID, position);
                return false;
            }
        });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Toast.makeText(SpecificDay.this, "ID: "+arg2+", " +"Dodano: "+ getTime(arg2) +"    " + getDate(arg2), Toast.LENGTH_SHORT).show();
                }
            });
//getSpecifics(stringArrayList.get(arg2), arg2)
        }
        else
            {

            }

        c.close();
    }

    private String getSpecifics(String dateAndTime, int arg2) {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select * from TEST_TABLE where Date  = \"" + choosenDate + "\"";
        Cursor c = db.rawQuery(sqlquery, null);
        dateAndTime = "";

        while (c.moveToPosition(arg2)) {
            String DATE = c.getString(c.getColumnIndex("Date"));
            String TIME = c.getString(c.getColumnIndex("Time"));

            dateAndTime = " " + DATE + " " + TIME + " ";
            c.close();
            return dateAndTime;

        }
        c.close();
        return dateAndTime;

    }

    private String getTime(int arg2)
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select * from TEST_TABLE where Date  = \"" + choosenDate + "\"";
        String time = "";
        Cursor c = db.rawQuery(sqlquery, null);

        while (c.moveToPosition(arg2)) {
            time = c.getString(c.getColumnIndex("Time"));

            c.close();
            return time;

        }
        c.close();
        return time;
    }

    private String getDate(int arg2)
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select * from TEST_TABLE where Date  = \"" + choosenDate + "\"";
        String date = "";
        Cursor c = db.rawQuery(sqlquery, null);

        while (c.moveToPosition(arg2)) {
            date = c.getString(c.getColumnIndex("Date"));

            c.close();
            return date;

        }
        c.close();
        return date;
    }

    private void deleteFromListAlert (final long itemID, final int position)
    {
        final android.app.AlertDialog.Builder accountAlert = new android.app.AlertDialog.Builder(this);
        accountAlert.setMessage("Usunąć?");

        accountAlert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFromDatabase(itemID, position);
                deletedFromList();

            }
        });
        accountAlert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        accountAlert.show();

    }

    private void deletedFromList()
    {
        Intent intent = new Intent(getApplicationContext(), TransitionalActivity.class);
        intent.putExtra("date", choosenDate);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out);
    }

    private long getProductID(int pos)
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select * from TEST_TABLE";
        Cursor c = db.rawQuery(sqlquery, null);
        long itemID = 0L;
        while (c.moveToPosition(pos)) {
            long ID = c.getLong(c.getColumnIndex("ID"));

            itemID = ID;
            c.close();
            return itemID;
        }
        c.close();
        return itemID;
    }

    private String getCurrencyPreference() {
        SharedPreferences CURRENCY = getSharedPreferences("BASIC", 0);
        String currency = CURRENCY.getString("currencypreference", "PLN");
        return currency;
    }

    private void createDatabase()
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sql = "create table if not exists TEST_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, ProductName VARCHAR, ProductPrice VARCHAR, Date VARCHAR, Time, VARCHAR)";
        db.execSQL(sql);
    }

    private float getSumFromDatabase()
    {
        float sumMoneySpent = 0;
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sqlquery = "select ProductName, ProductPrice from TEST_TABLE where Date = \"" + choosenDate + "\"";
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


    private void deleteFromDatabase(long itemID, int position)
    {
        db = openOrCreateDatabase("MY_TEST_DB", MODE_PRIVATE, null);
        String sql = "delete from TEST_TABLE where Time like \"" + getDate(position) + "\"";
        Cursor c = db.rawQuery(sql, null);
        if (c != null) {
            while (c.moveToNext()) {

            }
        }
        updateIfDBChange(1);
    }



    @Override
    public void finish() {

        if(!doesDatabaseExist(getApplicationContext(), "MY_TEST_DB"))
        {
           createDatabase();
            updateIfDBChange(1);
        }
        else
        {

        }
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getActivityPreference() == 0)
        {
            updateIfDBChange(1);
            Intent intent = new Intent(this, MainDesk.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
        else {
            updateIfDBChange(1);
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }
}
