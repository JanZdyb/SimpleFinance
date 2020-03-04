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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener  {

    SQLiteDatabase db;
    String strDay, strMonth, strYear, strWhole, yearCounterToString, strToPrint, fullDate, valOfDays, valOfMonth;
    ImageView monthBack, monthNext, yearBack, yearNext;
    Button goToSpecificDay, goToSumOfTheMonth;
    TextView monthName, yearCount, todaysDate, clickedDay, moneySpent;
    TextView cal1, cal2,cal3,cal4,cal5,cal6,cal7,cal8,cal9,cal10,cal11,cal12,cal13,cal14,cal15,cal16,cal17,cal18,cal19,cal20,cal21,cal22,cal23,cal24,cal25,cal26,cal27,cal28,cal29,cal30,cal31;
    int[] textViewsID;
    static String[] months = new String[] {"styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"};
    static int dayCounter, monthCounter, yearCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        initializeTextViews();

        goToSpecificDay = (Button)findViewById(R.id.goToSpecificDay);
        goToSumOfTheMonth = (Button)findViewById(R.id.goToSumOfTheMonth);
        goToSpecificDay.setVisibility(View.GONE);


        monthBack = (ImageView)findViewById(R.id.monthBack);
        monthNext = (ImageView)findViewById(R.id.monthNext);
        yearBack = (ImageView)findViewById(R.id.yearBack);
        yearNext = (ImageView)findViewById(R.id.yearNext);

        monthName = (TextView)findViewById(R.id.monthName);
        yearCount = (TextView)findViewById(R.id.yearCount);
        todaysDate = (TextView)findViewById(R.id.todaysDate);
        clickedDay = (TextView)findViewById(R.id.clickedDay);
        moneySpent = (TextView)findViewById(R.id.moneySpent);

        cal1 = (TextView)findViewById(R.id.cal1);
        cal2 = (TextView)findViewById(R.id.cal2);
        cal3 = (TextView)findViewById(R.id.cal3);
        cal4 = (TextView)findViewById(R.id.cal4);
        cal5 = (TextView)findViewById(R.id.cal5);
        cal6 = (TextView)findViewById(R.id.cal6);
        cal7 = (TextView)findViewById(R.id.cal7);
        cal8 = (TextView)findViewById(R.id.cal8);
        cal9 = (TextView)findViewById(R.id.cal9);
        cal10 = (TextView)findViewById(R.id.cal10);
        cal11 = (TextView)findViewById(R.id.cal11);
        cal12 = (TextView)findViewById(R.id.cal12);
        cal13 = (TextView)findViewById(R.id.cal13);
        cal14 = (TextView)findViewById(R.id.cal14);
        cal15 = (TextView)findViewById(R.id.cal15);
        cal16 = (TextView)findViewById(R.id.cal16);
        cal17 = (TextView)findViewById(R.id.cal17);
        cal18 = (TextView)findViewById(R.id.cal18);
        cal19 = (TextView)findViewById(R.id.cal19);
        cal20 = (TextView)findViewById(R.id.cal20);
        cal21 = (TextView)findViewById(R.id.cal21);
        cal22 = (TextView)findViewById(R.id.cal22);
        cal23 = (TextView)findViewById(R.id.cal23);
        cal24 = (TextView)findViewById(R.id.cal24);
        cal25 = (TextView)findViewById(R.id.cal25);
        cal26 = (TextView)findViewById(R.id.cal26);
        cal27 = (TextView)findViewById(R.id.cal27);
        cal28 = (TextView)findViewById(R.id.cal28);
        cal29 = (TextView)findViewById(R.id.cal29);
        cal30 = (TextView)findViewById(R.id.cal30);
        cal31 = (TextView)findViewById(R.id.cal31);

        getCurrentDate();
        setValues();
        printDetails();


          yearCounterToString = String.valueOf(yearCounter);

          monthName.setText(months[monthCounter-1]);
          yearCount.setText(yearCounterToString);


        monthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthCounter == 1)
                {
                    monthCounter = 12;
                    yearCounter -= 1;
                    monthName.setText(months[monthCounter-1]);
                    changeTextViewsVisibilityDependingOnMonth();
                    printDetails();
                    skipTheDayProblem();
                }
                else
                {
                    monthCounter -= 1;
                    monthName.setText(months[monthCounter-1]);
                    changeTextViewsVisibilityDependingOnMonth();
                    printDetails();
                    skipTheDayProblem();
                }
            }
        });


        monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthCounter == 12)
                {
                    monthCounter = 1;
                    yearCounter += 1;
                    monthName.setText(months[monthCounter-1]);
                    yearCounterToString = String.valueOf(yearCounter);
                    yearCount.setText(yearCounterToString);
                    changeTextViewsVisibilityDependingOnMonth();
                    printDetails();
                    skipTheDayProblem();
            }
                else
                {
                    monthCounter += 1;
                    monthName.setText(months[monthCounter-1]);
                    changeTextViewsVisibilityDependingOnMonth();
                    printDetails();
                    skipTheDayProblem();
                }
            }
        });

        yearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearCounter -= 1;
                yearCounterToString = String.valueOf(yearCounter);
                yearCount.setText(yearCounterToString);
                printDetails();
                skipTheDayProblem();
            }
        });

        yearNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearCounter += 1;
                yearCounterToString = String.valueOf(yearCounter);
                yearCount.setText(yearCounterToString);
                printDetails();
                skipTheDayProblem();

            }
        });

        goToSpecificDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateActivityPreference(1);
                fixTheFormatAndReturn();
                goToSpecificDay();

            }
        });

        goToSumOfTheMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SumMonthActivity.class);
                getCurrentDate();
                intent.putExtra("strDay", strDay);
                intent.putExtra("strMonth", strMonth);
                intent.putExtra("strYear", strYear);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });


    }

    private void changeTextViewsVisibilityDependingOnMonth() {
        if (monthCounter == 1 || monthCounter == 3 || monthCounter == 5 || monthCounter == 7 || monthCounter == 8 || monthCounter == 10 || monthCounter == 12) {
            cal30.setVisibility(View.VISIBLE);
            cal31.setVisibility(View.VISIBLE);
        } else if (monthCounter == 2) {
            cal30.setVisibility(View.GONE);
            cal31.setVisibility(View.GONE);
        } else {
            cal30.setVisibility(View.VISIBLE);
            cal31.setVisibility(View.GONE);
        }
    }

    private void initializeTextViews()
    {
        textViewsID = new int[]{R.id.cal1, R.id.cal2, R.id.cal3, R.id.cal4, R.id.cal5, R.id.cal6, R.id.cal7, R.id.cal8, R.id.cal9, R.id.cal10, R.id.cal11, R.id.cal12,
                R.id.cal13, R.id.cal14, R.id.cal15, R.id.cal16, R.id.cal17, R.id.cal18, R.id.cal19, R.id.cal20, R.id.cal21, R.id.cal22, R.id.cal23,
                R.id.cal24, R.id.cal25, R.id.cal26, R.id.cal27, R.id.cal28, R.id.cal29, R.id.cal30, R.id.cal31};

        for (int i = 0; i < 31; i++) {
            TextView td = (TextView) findViewById(textViewsID[i]);
            td.setTag(i);
            td.setOnClickListener(this);
        }
    }

    private void setValues()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

        dayCounter = Integer.parseInt(strDay);
        monthCounter = Integer.parseInt(strMonth);
        yearCounter = Integer.parseInt(strYear);


        yearCounterToString = String.valueOf(yearCounter);

        monthName.setText(months[monthCounter-1]);
        yearCount.setText(yearCounterToString);
        todaysDate.setText("DZIŚ MAMY:\n" +strToPrint);

        if(monthCounter == 1 || monthCounter == 3 || monthCounter == 5 || monthCounter == 7 || monthCounter == 8 || monthCounter == 10 || monthCounter == 12)
        {
            cal30.setVisibility(View.VISIBLE);
            cal31.setVisibility(View.VISIBLE);
        }
        else if(monthCounter == 2)
        {
            cal30.setVisibility(View.GONE);
            cal31.setVisibility(View.GONE);
        }
        else
        {
            cal30.setVisibility(View.VISIBLE);
            cal31.setVisibility(View.GONE);
        }
            }
        });

    }

    @Override
    public void onClick(View v) {

        String tag = v.getTag().toString();

        switch(tag)
        {
            case "0":
                dayCounter = 1;
                printDetails();
                break;
            case "1":
                dayCounter = 2;
                printDetails();
                break;
            case "2":
                dayCounter = 3;
                printDetails();
                break;
            case "3":
                dayCounter = 4;
                printDetails();
                break;
            case "4":
                dayCounter = 5;
                printDetails();
                break;
            case "5":
                dayCounter = 6;
                printDetails();
                break;
            case "6":
                dayCounter = 7;
                printDetails();
                break;
            case "7":
                dayCounter = 8;
                printDetails();
                break;
            case "8":
                dayCounter = 9;
                printDetails();
                break;
            case "9":
                dayCounter = 10;
                printDetails();
                break;
            case "10":
                dayCounter = 11;
                printDetails();
                break;
            case "11":
                dayCounter = 12;
                printDetails();
                break;
            case "12":
                dayCounter = 13;
                printDetails();
                break;
            case "13":
                dayCounter = 14;
                printDetails();
                break;
            case "14":
                dayCounter = 15;
                printDetails();
                break;
            case "15":
                dayCounter = 16;
                printDetails();
                break;
            case "16":
                dayCounter = 17;
                printDetails();
                break;
            case "17":
                dayCounter = 18;
                printDetails();
                break;
            case "18":
                dayCounter = 19;
                printDetails();
                break;
            case "19":
                dayCounter = 20;
                printDetails();
                break;
            case "20":
                dayCounter = 21;
                printDetails();
                break;
            case "21":
                dayCounter = 22;
                printDetails();
                break;
            case "22":
                dayCounter = 23;
                printDetails();
                break;
            case "23":
                dayCounter = 24;
                printDetails();
                break;
            case "24":
                dayCounter = 25;
                printDetails();
                break;
            case "25":
                dayCounter = 26;
                printDetails();
                break;
            case "26":
                dayCounter = 27;
                printDetails();
                break;
            case "27":
                dayCounter = 28;
                printDetails();
                break;
            case "28":
                dayCounter = 29;
                printDetails();
                break;
            case "29":
                dayCounter = 30;
                printDetails();
                break;
            case "30":
                dayCounter = 31;
                printDetails();
                break;

            default:
                break;
        }

    }

    private String fixTheFormatAndReturn() {
        valOfDays = "";
        valOfMonth = "";

        if (dayCounter > 9) {
            valOfDays = String.valueOf(dayCounter);
        } else {
            valOfDays = "0";
            valOfDays += String.valueOf(dayCounter);
        }

        if (monthCounter > 9) {
            valOfMonth = String.valueOf(monthCounter);
        } else {
            valOfMonth = "0";
            valOfMonth += String.valueOf(monthCounter);
        }
        fullDate = valOfDays + "." + valOfMonth + "." + yearCounter;

        return fullDate;
    }

    private void goToSpecificDay()
    {
        fixTheFormatAndReturn();

        if(fullDate.equals(createDefaultDay()))
        {
            Intent intent = new Intent(this, SpecificDay.class);
            intent.putExtra("date", createDefaultDay());
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        else {
            Intent intent = new Intent(this, SpecificDay.class);
            intent.putExtra("date", fullDate);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }
    private String createDefaultDay()
    {
        String defaultDay = "01" + "." + valOfMonth + "." + yearCounter;
        return defaultDay;
    }

    private void skipTheDayProblem()
    {
        clickedDay.setText(createDefaultDay());
        fixTheFormatAndReturn();
        getSumFromDatabase(fixTheFormatAndReturn());

        float sum = getSumFromDatabase(createDefaultDay());
        String sSum = String.valueOf(sum);
        moneySpent.setText(sSum + " " + getCurrencyPreference());
    }

    private void printDetails()
    {
        goToSpecificDay.setVisibility(View.VISIBLE);

        float sum = getSumFromDatabase(fixTheFormatAndReturn());
        String sSum = String.valueOf(sum);

        clickedDay.setText(fixTheFormatAndReturn());
        moneySpent.setText(sSum + " " + getCurrencyPreference());
    }

    private String getCurrencyPreference() {
        SharedPreferences CURRENCY = getSharedPreferences("BASIC", 0);
        String currency = CURRENCY.getString("currencypreference", "PLN");
        return currency;
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

    private void getCurrentDate()
    {
        Calendar date = Calendar.getInstance();

        SimpleDateFormat dayOnlyFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthOnlyFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearOnlyFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat wholeFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat printedFormat = new SimpleDateFormat("dd MMMM yyyy");

        strDay = dayOnlyFormat.format(date.getTime());
        strMonth = monthOnlyFormat.format(date.getTime());
        strYear = yearOnlyFormat.format(date.getTime());
        strWhole = wholeFormat.format(date.getTime());
        strToPrint = printedFormat.format(date.getTime());
    }

    private void updateActivityPreference(int number)
    {
        SharedPreferences ACTIVITY = getSharedPreferences("BASIC", 0);
        SharedPreferences.Editor ACTIVITYEeditor = ACTIVITY.edit();

        ACTIVITYEeditor.putInt("activitypreference", number);
        ACTIVITYEeditor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainDesk.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
