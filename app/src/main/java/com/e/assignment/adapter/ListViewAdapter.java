package com.e.assignment.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.e.assignment.controller.EditEventListener;
import com.e.assignment.model.Event;
import com.e.assignment.R;
import com.e.assignment.model.EventImpl;

import java.util.Date;
import java.util.Map;

public class ListViewAdapter extends ArrayAdapter<Event>{
    private final String TAG = getClass().getName();
    private Context context;
    private Map<Date, Event> events;

    public ListViewAdapter(Context context, Map<Date, Event> events) {
        super(context, 0, events.values().toArray(new Event[events.size()]));
        this.context = context;
        this.events = events;
    }
    @Override
    public View getView(int position, View eventItemView, ViewGroup parent) {

        String eventId = getItem(position).getId();
        if(eventItemView == null) {
            eventItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        TextView eventTitle = eventItemView.findViewById(R.id.eventTitle);
        TextView eventDate = eventItemView.findViewById(R.id.eventDate);
        TextView eventAttendees = eventItemView.findViewById(R.id.eventAttend);
        TextView eventMovie = eventItemView.findViewById(R.id.eventMovie);
        Button myButton1 = eventItemView.findViewById(R.id.eventButton);


        myButton1.setOnClickListener(new EditEventListener(context, eventId));

        Event item = getItem(position);
        if (item.getMovie()!=null){
            eventMovie.setText(item.getMovie().getTitle());

        }

        Log.d(TAG, item.getId()+"set view m:"+eventMovie.getText());
        eventTitle.setText(item.getTitle());
        eventDate.setText(item.getStartDate().toString());
        if (!item.getAttendees().isEmpty())
        {
            eventAttendees.setText(item.getAttendees().size()+"");
        }
        return eventItemView;
    }
}
