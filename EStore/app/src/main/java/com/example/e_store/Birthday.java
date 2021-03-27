package com.example.e_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class Birthday extends AppCompatActivity {
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        date = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        CalendarView birthday = findViewById(R.id.calendarView);
        Button next = (Button) findViewById(R.id.next);

        birthday.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "-" + month + "-" + dayOfMonth;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!date.isEmpty()) {
                    Intent signUp = new Intent(Birthday.this, SignUp.class);
                    signUp.putExtra("date", date);
                    startActivity(signUp);
                }
            }
        });
    }
}