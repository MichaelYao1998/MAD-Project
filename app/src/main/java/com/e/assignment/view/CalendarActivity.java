package com.e.assignment.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.e.assignment.R;
import com.e.assignment.controller.EditCalendarListener;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import java.util.Date;
import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {
    private customCalendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        final EventsModel model = EventsModelImpl.getSingletonInstance(getApplicationContext());
        calendar = findViewById(R.id.calendar_view);



        //event handler
        calendar.setEventHandler(new EditCalendarListener(this));
    }
    @Override
    protected void onStart() {

        super.onStart();
        calendar.updateCalendar();
    }
}
