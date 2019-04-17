package com.e.assignment.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.fragment.DialogListDialogFragment;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventHandler;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        final EventsModel model = EventsModelImpl.getSingletonInstance(getApplicationContext());
        customCalendar calendar = findViewById(R.id.calendar_view);
        calendar.updateCalendar(events);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                DateFormat df = SimpleDateFormat.getDateInstance();
                if (model.eventsArrForDay(date).size() >= 2) {
                //TODO dialog

                } else if (model.eventsArrForDay(date).size() == 1) {
                    Intent intent = new Intent(CalendarActivity.this, EditEventActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, "");//TODO eventId
                    intent.setType("text/plain");
                    startActivity(intent);
                } else {
                    //do nothing
                    Toast.makeText(CalendarActivity.this, df.format(date)+"does not have any events", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //event handler
        calendar.setEventHandler(eventHandler);
    }
}
