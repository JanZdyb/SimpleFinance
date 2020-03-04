package com.example.simplefinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class TransitionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitional);

        String choosenDate = getIntent().getExtras().getString("date");

        Intent intent = new Intent(getApplicationContext(), SpecificDay.class);
        intent.putExtra("date", choosenDate);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out);

    }
}
