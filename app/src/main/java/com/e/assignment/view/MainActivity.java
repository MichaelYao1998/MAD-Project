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
import android.widget.ListView;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.adapter.ListViewAdapter;
import com.e.assignment.model.Event;
import com.e.assignment.model.customCalendar;
import com.e.assignment.model.viewModel.EventListViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String LOG_TAG = getClass().getName();

    Context context;
    AttributeSet ars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        HashSet<Date> events = new HashSet<>();
        ArrayList<Date> days = new ArrayList<Date>();
        events.add(new Date());
        days.add(new Date());
//        RelativeLayout relativeLayout = findViewById(R.id.calendar_id);
//        GridView gridView = findViewById(R.id.day_grid);
//        CalendarAdapter calendar = new CalendarAdapter(this,days,events);
//        gridView.setAdapter(calendar);
        customCalendar calendar2 = (customCalendar) findViewById(R.id.square_day);
//        calendar2.updateCalendar(events);
//        calendar2.initControl(this, ars);
    }


}
