package com.e.assignment.adapter;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.e.assignment.R;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarAdapter extends ArrayAdapter<Date> {
    private final HashSet<Date> eventDays;
    private LayoutInflater inflater;
    private Context context;
    public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date>eventDays){
        super(context, R.layout.calendar_view,days);
        this.eventDays = eventDays;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EventsModel model = EventsModelImpl.getSingletonInstance(context);
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        //today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        //inflate item if it does not exist yet
        if(view == null){
            view = inflater.inflate(R.layout.calendar_item,parent,false);
        }
        //clean styling
        ((TextView)view).setTypeface(null, Typeface.NORMAL);
        ((TextView)view).setTextColor(Color.BLACK);
        if(month!=calendarToday.get(Calendar.MONTH) || year != calendarToday.get(Calendar.YEAR)){
            ((TextView)view).setTextColor(Color.parseColor("#E0E0E0"));
        }else if(day == calendarToday.get(Calendar.DATE)){
            //set it to blud/bold
            ((TextView)view).setTypeface(null, Typeface.BOLD);
            ((TextView)view).setTextColor(Color.BLUE);
        }
        if (!model.eventsArrForDay(calendar.getTime()).isEmpty())
        {
            ((TextView)view).setTypeface(null, Typeface.BOLD);
            ((TextView)view).setBackgroundColor(Color.RED);
            ((TextView)view).setTextColor(Color.WHITE);
        }
        // set text
        ((TextView)view).setText(String.valueOf(calendar.get(Calendar.DATE)));
        return view;
    }
}