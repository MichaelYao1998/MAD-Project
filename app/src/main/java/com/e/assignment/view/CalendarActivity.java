package com.e.assignment.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.model.EventHandler;
import com.e.assignment.model.customCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        customCalendar calendar = findViewById(R.id.calendar_view);
        calendar.updateCalendar(events);

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(CalendarActivity.this,df.format(date), Toast.LENGTH_SHORT).show();
            }
        };

        //event handler
        calendar.setEventHandler(eventHandler);
    }
}
