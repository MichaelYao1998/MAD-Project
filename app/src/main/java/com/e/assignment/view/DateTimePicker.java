package com.e.assignment.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.e.assignment.model.Event;

import static com.e.assignment.view.EditEventActivity.calendar;
import static com.e.assignment.view.EditEventActivity.calendarEnd;

public class DateTimePicker {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public void newTimePickerDialog(Context context, final EditText editTime, final boolean isStartDate, final Event selectedEvent){
        final Calendar temp;
        if (isStartDate){
            temp=calendar;
        }else {
            temp=calendarEnd;
        }

        hour = temp.get(Calendar.HOUR_OF_DAY);
        minute = temp.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        temp.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        temp.set(Calendar.MINUTE, minuteOfHour);
                        hour = hourOfDay;
                        minute = minuteOfHour;
                        String am_pm = "";
                        int hourIn12;
                        if (temp.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else
                            am_pm = "PM";
                        if (hourOfDay>12)
                            hourIn12=hourOfDay-12;
                        else if (hourOfDay%12==0)
                            hourIn12=12;
                        else
                            hourIn12=hourOfDay;

                        editTime.setText(String.format("%d:%02d%s",hourIn12,minuteOfHour,am_pm));
                        if (isStartDate)
                            selectedEvent.setStartDate(calendar.getTime());
                        else
                            selectedEvent.setEndDate(calendarEnd.getTime());

                    }
                }, hour,minute , false);

        editTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                timePickerDialog.show();
                return false;
            }

        });

    }

    @SuppressLint("ClickableViewAccessibility")
    public void newDatePickerDialog(Context context, final EditText eventDate, final boolean isStartDate, final Event selectedEvent){


        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int yearSinceJesus, int monthOfYear, int dayOfMonth)
            {

                year=yearSinceJesus;
                month=monthOfYear;
                day=dayOfMonth;
                eventDate.setText(String.format("%02d-%02d-%d",dayOfMonth,month+1,year));
                if (isStartDate){
                    calendar.set(year,month,day);
                    selectedEvent.setStartDate(calendar.getTime());
                    Log.v("!!!!!",calendar.toString());
                }
                else
                {
                    calendarEnd.set(year,month,day);
                    selectedEvent.setEndDate(calendarEnd.getTime());
                }
            }
        }, year, month, day);

        eventDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                if (isStartDate==false){

                    Log.v("!!!!!",calendar.toString());
                    datePickerDialog.getDatePicker().setMinDate(0);
                    datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                }
                datePickerDialog.show();
                return false;
            }

        });
    }

}
