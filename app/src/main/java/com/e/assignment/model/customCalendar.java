package com.e.assignment.model;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.e.assignment.R;
import com.e.assignment.adapter.CalendarAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class customCalendar extends LinearLayout {
    LinearLayout header;
    Button today;
    ImageView prev;
    ImageView next;
    TextView DateDay;
    TextView DisplayDate;
    TextView DateYear;
    GridView gridView;
    HashSet<Date> events = new HashSet<>();
    private static final int DAYS_COUNT = 42;
    public customCalendar(Context context, AttributeSet attrs){
        super(context,attrs);
        initControl(context,attrs);
    }

    private void assignUI(){
        //assign local variables to components
        header = findViewById(R.id.calendar_header);
        prev = findViewById(R.id.previous_button);
        next = findViewById(R.id.next_button);
        DateDay = findViewById(R.id.day);
        DisplayDate = findViewById(R.id.date);
        DateYear = findViewById(R.id.year);
        today = findViewById(R.id.display_today);
        gridView = findViewById(R.id.day_grid);
    }

    public void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view,this);
        assignUI();
        events.add(new Date());
        updateCalendar(events);
//        updateCalendar();
    }
//    public void updateCalendar(){
//        updateCalendar(null);
//    }
    public void updateCalendar(HashSet<Date> events){
        ArrayList<Date> cells = new ArrayList<>();
        //TODO ma
        Calendar currentDate= Calendar.getInstance();
        Calendar calendar = (Calendar)currentDate.clone();

        //determine the value for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH,1);
        int monthBeginningValue = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningValue);

        //fill value
        while (cells.size() < DAYS_COUNT){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        //update grid
        gridView.setAdapter(new CalendarAdapter(getContext(),cells,events));

        //update title
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,D MMM,yyyy");
        String[] dateToday = sdf.format(currentDate.getTime()).split(",");
        DateDay.setText(dateToday[0]);
        DisplayDate.setText(dateToday[1]);
        DateYear.setText(dateToday[2]);
    }
}
