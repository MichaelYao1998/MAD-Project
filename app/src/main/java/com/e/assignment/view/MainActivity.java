package com.e.assignment.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.adapter.ListViewAdapter;
import com.e.assignment.model.DayCellsClickListener;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventHandler;
import com.e.assignment.model.customCalendar;
import com.e.assignment.model.viewModel.EventListViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
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
                Toast.makeText(MainActivity.this,df.format(date), Toast.LENGTH_SHORT).show();
            }
        };

        //event handler
        calendar.setEventHandler(eventHandler);
    }
}
