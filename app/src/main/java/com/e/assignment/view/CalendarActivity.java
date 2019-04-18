package com.e.assignment.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.e.assignment.R;
import com.e.assignment.controller.EditCalendarListener;

import java.util.Date;
import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {
    //Call the CustomCalendar to run the CalendarActivity
    private CustomCalendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        calendar = findViewById(R.id.calendar_view);
        //event handler
        calendar.setCalendarHandler(new EditCalendarListener(this));
    }
    @Override
    protected void onStart() {

        super.onStart();
        calendar.updateCalendar();
    }
}
