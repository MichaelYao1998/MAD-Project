package com.e.assignment.adapter;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
    private LayoutInflater inflater;
    private Context context;
    private int currMonth;

    /*
     *  @param context      context from calling
     *  @param days         HashMap for the editing event
     *  @param currMonth    the current month from Calendar
     */
    public CalendarAdapter(Context context, ArrayList<Date> days, int currMonth){
        super(context, R.layout.calendar_view,days);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.currMonth = currMonth;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        EventsModel model = EventsModelImpl.getSingletonInstance(context);
        Calendar calendar = Calendar.getInstance();
        ArrayList<Event> eventsListForDay;
        Date date = getItem(position);
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        eventsListForDay=model.eventsArrForDay(calendar.getTime());
        //today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        //inflate item if it does not exist yet
        if(view == null){
            view = inflater.inflate(R.layout.calendar_item,parent,false);
        }

        Log.v("!?Month",month+",,,"+currMonth);Log.v("!?Day",month+",,,");
        //clean styling
        ((TextView)view).setTypeface(null, Typeface.NORMAL);
        ((TextView)view).setTextColor(Color.BLACK);

        /*
            if the month does not equal to the user's month, set it color to gray
            if the date, month and year all match user's date and time, set it color to the bold blue
         */
        if(month!=currMonth){
            ((TextView)view).setTextColor(Color.parseColor("#E0E0E0"));
        }else if(day == calendarToday.get(Calendar.DATE) && month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR)){
            //set it to blud/bold
            ((TextView)view).setTypeface(null, Typeface.BOLD);
            ((TextView)view).setTextColor(Color.BLUE);
        }
        //if the date has events, then set it color to red
        if (!eventsListForDay.isEmpty())
        {
            if (month==currMonth){
                ((TextView)view).setTextColor(Color.RED);
            }
            ((TextView)view).setTypeface(null, Typeface.BOLD);
            ((TextView)view).setPaintFlags(((TextView)view).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        // set text
        ((TextView)view).setText(String.valueOf(calendar.get(Calendar.DATE)));
        return view;
    }
}
