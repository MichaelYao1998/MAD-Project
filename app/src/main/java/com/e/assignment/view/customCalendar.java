package com.e.assignment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.e.assignment.R;
import com.e.assignment.adapter.CalendarAdapter;
import com.e.assignment.model.CalendarHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomCalendar extends LinearLayout {
    LinearLayout header;
    ImageView prev;
    ImageView next;
    TextView current_Date;
    GridView gridView;
    Context context;
    private static final int DAYS_COUNT = 40;
    Calendar currentDate = Calendar.getInstance();
    private CalendarHandler calendarHandler = null;

    public CustomCalendar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        initControl(context, attrs);
    }

    public void setCalendarHandler(CalendarHandler calendarHandler){
        this.calendarHandler = calendarHandler;
    }

    //map the variables to the local components
    private void UIDesign() {
        header = findViewById(R.id.calendar_header);
        prev = findViewById(R.id.previous_button);
        next = findViewById(R.id.next_button);
        current_Date = findViewById(R.id.current_date);
        gridView = findViewById(R.id.day_grid);
    }

    //initialize the calendar
    public void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this);
        UIDesign();
        handleClickListener();
        updateCalendar();
    }

    //calculate the dates for previous and next month
    public void updateCalendar() {
        ArrayList<Date> date = new ArrayList<>();

        Calendar calendar = (Calendar) currentDate.clone();

        //determine the value for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayValue = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -firstDayValue);

        //fill value to the layout page
        while (date.size() < DAYS_COUNT) {
            date.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), date, currentDate.get(Calendar.MONTH)));

        //update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        current_Date.setText(sdf.format(currentDate.getTime()));
    }

    /*
        If click on the right arrow image add one month and refresh UI
        If click on the left arrow image then backwards one month and refresh UI
        If click on the date, implement the corresponding action that declared in the EditCalendarListener
     */
    private void handleClickListener() {

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //handle long-press
                if (calendarHandler == null) {
                    return false;
                }
                calendarHandler.onDayLongPress((Date) parent.getItemAtPosition(position));
                return true;
            }
        });
    }
}
