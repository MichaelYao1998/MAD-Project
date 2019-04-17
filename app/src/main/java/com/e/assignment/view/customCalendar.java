package com.e.assignment.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.assignment.R;
import com.e.assignment.adapter.CalendarAdapter;
import com.e.assignment.controller.EditCalendarListener;
import com.e.assignment.model.EventHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class customCalendar extends LinearLayout {
    LinearLayout header;
    ImageView prev;
    ImageView next;
    TextView current_Date;
    GridView gridView;
    Context context;
    HashSet<Date> events = new HashSet<>();
    private static final int DAYS_COUNT = 42;
    Calendar currentDate = Calendar.getInstance();
    private EventHandler eventHandler = null;
    String eventId;
    public customCalendar(Context context, AttributeSet attrs, String eventId) {
        super(context, attrs);
        this.context = context;
        this.eventId = eventId;
        initControl(context, attrs);
    }

    public customCalendar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        initControl(context, attrs);
    }

    public void setEventHandler(EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }

    private void assignUI() {
        //assign local variables to components
        header = findViewById(R.id.calendar_header);
        prev = findViewById(R.id.previous_button);
        next = findViewById(R.id.next_button);
        current_Date = findViewById(R.id.current_date);
        gridView = findViewById(R.id.day_grid);
    }

    public void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);
        assignUI();
        handleClickListener();
        updateCalendar(null);
    }

    public void updateCalendar(HashSet<Date> events) {
        ArrayList<Date> cells = new ArrayList<>();

        Calendar calendar = (Calendar) currentDate.clone();

        //determine the value for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningValue = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningValue);

        //fill value
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), cells, events));

        //update title
        SimpleDateFormat sdf = new SimpleDateFormat(" dd MMMM yyyy");
        current_Date.setText(sdf.format(currentDate.getTime()));
    }

    private void handleClickListener() {
        //add one month and refresh UI
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar(null);
            }
        });
        // backwards one month and refresh UI
        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar(null);
            }
        });
        // long-pressing a day
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //handle long-press
                if (eventHandler == null) {
                    return false;
                }
                eventHandler.onDayLongPress((Date) parent.getItemAtPosition(position));
                return true;
            }
        });
//        gridView.setOnItemLongClickListener( new EditCalendarListener(context, eventId));
    }
}