package com.example.simplefinance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SumMonthActivity extends AppCompatActivity {

    private TextView dayTD, monthTD, yearTD;
    private String strDay, strMonth, strYear;
    private ImageView goBackMonth, goNextMonth, goBackYear, goNextYear;
    private int strDayToInt, strMonthToInt, strYearToInt;
    private static String[] months = new String[] {"styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_month);

        strDay = getIntent().getExtras().getString("strDay");
        strMonth = getIntent().getExtras().getString("strMonth");
        strYear = getIntent().getExtras().getString("strYear");

        goBackMonth = (ImageView)findViewById(R.id.goBackMonth);
        goNextMonth = (ImageView)findViewById(R.id.goNextMonth);
        goBackYear = (ImageView)findViewById(R.id.goBackYear);
        goNextYear = (ImageView)findViewById(R.id.goNextYear);

        dayTD = (TextView)findViewById(R.id.dayTD);
        monthTD = (TextView)findViewById(R.id.monthTD);
        yearTD = (TextView)findViewById(R.id.yearTD);

        dayTD.setText(strDay);
        monthTD.setText(strMonth);
        yearTD.setText(strYear);

        strDayToInt = Integer.parseInt(strDay);
        strMonthToInt = Integer.parseInt(strMonth);
        strYearToInt = Integer.parseInt(strYear);

        goBackMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strMonthToInt == 1)
                {
                    monthTD.setText(months[11]);
                    yearTD.setText(strYearToInt - 1);
                }
                else
                {
                    strMonthToInt = strMonthToInt - 1;
                    monthTD.setText(months[strMonthToInt]);
                }
            }
        });

        goNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strMonthToInt == 11)
                {
                    monthTD.setText(months[0]);
                    yearTD.setText(months[strYearToInt + 1]);
                }
                else
                {
                    strMonthToInt = strMonthToInt + 1;
                    monthTD.setText(months[strMonthToInt]);
                }
            }
        });

        goBackYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strYearToInt =strYearToInt - 1;
                strYear = String.valueOf(strYearToInt);
                yearTD.setText(strYear);
            }
        });

        goNextYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strYearToInt = strYearToInt + 1;
                strYear = String.valueOf(strYearToInt);
                yearTD.setText(strYear);
            }
        });
    }
}
