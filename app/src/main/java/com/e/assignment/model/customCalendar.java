package com.e.assignment.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.assignment.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class customCalendar extends LinearLayout {
    private static final String GREY = "#a0a0a0";
    private static final String[] MONTH = {"Janurary", "Feburary", "March", "April", "May", "June", "July", "August", "Septemper", "October", "November", "December"};
    private TextView currentDate;
    private TextView currentMonth;
    private Button optionButton;
    private Button[] days;
    LinearLayout week1;
    LinearLayout week2;
    LinearLayout week3;
    LinearLayout week4;
    LinearLayout week5;
    LinearLayout week6;
    private LinearLayout[] weeks;
    private Drawable drawable;
    private Calendar calendar;
    int month;
    int year;
    int currentDay, chosenDay, currentCalendarMonth, chosenCalendarMonth, currentYear, chosenYear, pickedDay, pickedMonth, pickedYear;
    LinearLayout.LayoutParams originalButton;
    LinearLayout.LayoutParams userButton;
    DayCellsClickListener listener;

    public customCalendar(Context context) {
        super(context);
        init(context);
    }

    public customCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public customCalendar(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context);
    }

    public customCalendar(Context context, AttributeSet attrs, int styleAttr, int styleRes) {
        super(context, attrs, styleAttr, styleRes);
    }

    // Initialize the calendar
    private void init(Context context) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_view, this, true);
        calendar = Calendar.getInstance();
        week1 = view.findViewById(R.id.row1);
        week2 = view.findViewById(R.id.row2);
        week3 = view.findViewById(R.id.row3);
        week4 = view.findViewById(R.id.row4);
        week5 = view.findViewById(R.id.row5);
        week6 = view.findViewById(R.id.row6);
        currentDate = view.findViewById(R.id.display_date);
        currentMonth = view.findViewById(R.id.display_month);

        //Get the current date and month
        currentDay = chosenDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (month != 0 && year != 0) {
            currentCalendarMonth = chosenCalendarMonth = month;
            currentYear = chosenYear = year;
        } else {
            currentCalendarMonth = chosenCalendarMonth = calendar.get(Calendar.MONTH);
            currentYear = chosenYear = calendar.get(Calendar.YEAR);
        }

        //set text to display the date and year
        currentDate.setText("" + currentDay);
        currentMonth.setText(MONTH[currentCalendarMonth]);

        initWeeks();
        if (userButton != null) {
            originalButton = userButton;
        } else {
            originalButton = getDays();
        }
        addCells(originalButton, context, metrics);
        initCalendar(chosenYear, chosenCalendarMonth, chosenDay);
    }

    //days displays in the one layout
    private void initWeeks() {
        weeks = new LinearLayout[6];
        days = new Button[7 * 6];
        weeks[0] = week1;
        weeks[1] = week2;
        weeks[2] = week3;
        weeks[3] = week4;
        weeks[4] = week5;
        weeks[5] = week6;
    }

    private void initCalendar(int year, int month, int day) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);

        int daysOfCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        chosenYear = year;
        chosenCalendarMonth = month;
        chosenDay = day;
        calendar.set(year, month, 1);
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int dayNum = 1;
        int leftDayInWeek1 = 0;
        int dayAfterTheMonth = 0;

        if (firstDay != 1) {
            leftDayInWeek1 = firstDay;
            dayAfterTheMonth = leftDayInWeek1 + daysOfCurrentMonth;
            // Set current day gird color to blue and text color to white
            // Set other days text color to black and background color to transparent
            for (int i = firstDay; i < firstDay + daysOfCurrentMonth; ++i) {
                if (currentCalendarMonth == chosenCalendarMonth && currentYear == chosenYear && dayNum == currentDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.blue));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                // Load days to the layout and displays them based on the correct grid
                int[] dateArray = new int[3];
                dateArray[0] = dayNum;
                dateArray[1] = chosenCalendarMonth;
                dateArray[2] = chosenYear;
                days[i].setTag(dateArray);
                days[i].setText(String.valueOf(dayNum));
                days[i].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onDateDaysClick(v);
                    }
                });
                ++dayNum;
            }
        } else {
            leftDayInWeek1 = 8;
            dayAfterTheMonth = leftDayInWeek1 + daysOfCurrentMonth;
            for (int i = 8; i < 8 + daysOfCurrentMonth; ++i) {
                if (currentCalendarMonth == chosenCalendarMonth && currentYear == chosenYear && dayNum == currentDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.blue));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }
                int[] dateArray = new int[3];
                dateArray[0] = dayNum;
                dateArray[1] = chosenCalendarMonth;
                dateArray[2] = chosenYear;
                days[i].setTag(dateArray);
                days[i].setText(String.valueOf(dayNum));
                days[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDateDaysClick(v);
                    }
                });
                ++dayNum;
            }
        }
        if (month > 0) {
            calendar.set(year, month - 1, 1);
        } else {
            calendar.set(year - 1, 11, 1);
        }
        //calculate the days fill the space in current layout in transparent color and the days in next month to fill the space of the current layout
        int daysInPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = leftDayInWeek1 - 1; i >= 0; --i) {
            int[] dateArray = new int[3];
            if (chosenCalendarMonth > 0) {
                if (currentCalendarMonth == chosenCalendarMonth - 1 && currentYear == chosenYear && daysInPrevMonth == currentDay) {

                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }
                dateArray[0] = daysInPrevMonth;
                dateArray[1] = chosenCalendarMonth - 1;
                dateArray[2] = chosenYear;
            } else {
                if (currentCalendarMonth == 11 && currentYear == chosenYear - 1 && daysInPrevMonth == currentDay) {

                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }
                dateArray[0] = daysInPrevMonth;
                dateArray[1] = 11;
                dateArray[2] = chosenYear - 1;
            }
            days[i].setTag(dateArray);
            days[i].setText(String.valueOf(daysInPrevMonth--));
            days[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDateDaysClick(v);
                }
            });
        }
        int nextMonthDaysNum = 1;
        for (int i = dayAfterTheMonth; i < days.length; ++i) {
            int[] dateArray = new int[3];
            if (chosenCalendarMonth < 11) {
                if (currentCalendarMonth == chosenCalendarMonth + 1 && currentYear == chosenYear && nextMonthDaysNum == currentDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.blue));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }
                dateArray[0] = nextMonthDaysNum;
                dateArray[1] = chosenCalendarMonth + 1;
                dateArray[2] = chosenYear;
            } else {
                if (currentCalendarMonth == 0 && currentYear == currentYear + 1 && nextMonthDaysNum == currentDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.blue));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }
                dateArray[0] = nextMonthDaysNum;
                dateArray[1] = 0;
                dateArray[2] = chosenYear + 1;
            }
            days[i].setTag(dateArray);
            days[i].setTextColor(Color.parseColor(GREY));
            days[i].setText(String.valueOf(nextMonthDaysNum++));
            days[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onDateDaysClick(v);
                }
            });
        }
        calendar.set(chosenYear, chosenCalendarMonth, chosenDay);
    }

    public void setCallBack(DayCellsClickListener listener) {

        this.listener = listener;
    }

    public void onDateDaysClick(View view) {
        listener.onDateDaysClick(view);
        if (optionButton != null) {
            if (chosenYear == currentYear && chosenCalendarMonth == currentCalendarMonth && pickedDay == currentDay) {
                optionButton.setBackgroundColor(getResources().getColor(R.color.blue));
                optionButton.setTextColor(Color.WHITE);
            } else {
                optionButton.setBackgroundColor(Color.TRANSPARENT);
                if (optionButton.getCurrentTextColor() != Color.RED) {
                    optionButton.setTextColor(getResources().getColor(R.color.calendar_cell));
                }
            }
        }
        optionButton = (Button) view;
        if (optionButton.getTag() != null) {
            int[] dateArray = (int[]) optionButton.getTag();
            pickedDay = dateArray[0];
            pickedMonth = dateArray[1];
            pickedYear = dateArray[2];
        }
        if (pickedYear == currentYear && pickedMonth == currentCalendarMonth && pickedDay == currentDay) {
            optionButton.setBackgroundColor(getResources().getColor(R.color.blue));
            optionButton.setTextColor(Color.WHITE);
        } else {
            optionButton.setBackgroundColor(getResources().getColor(R.color.grey));
            if (optionButton.getCurrentTextColor() != Color.RED) {
                optionButton.setTextColor((Color.WHITE));
            }
        }
    }

    private void addCells(LayoutParams button, Context context, DisplayMetrics metrics) {
        int daysArrayNum = 0;
        for (int weekNum = 0; weekNum < 6; ++weekNum) {
            for (int daysInWeek = 0; daysInWeek < 7; ++daysInWeek) {
                final Button day = new Button(context);
                day.setTextColor(Color.parseColor(GREY));
                day.setBackgroundColor(Color.TRANSPARENT);
                day.setLayoutParams(button);
                day.setTextSize((int) metrics.density * 8);
                day.setSingleLine();
                days[daysArrayNum] = day;
                weeks[weekNum].addView(day);
                ++daysArrayNum;
            }
        }
    }

    private LayoutParams getDays() {
        LinearLayout.LayoutParams button = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        button.weight = 1;
        return button;
    }

    public void setDaysLayoutParams(LinearLayout.LayoutParams userButton){
        this.userButton = userButton;
    }

    public void setCurrentDate(int month, int year){
        this.month = month;
        this.year = year;
    }

    public void setBackGround(Drawable drawable){
        this.drawable = drawable;
    }
}
