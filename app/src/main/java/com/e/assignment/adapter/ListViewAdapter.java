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

import java.util.Map;

public class ListViewAdapter extends ArrayAdapter<Event>{
    private final String TAG = getClass().getName();
    private Context context;
    private Map<String, Event> events;

    public ListViewAdapter(Context context, Map<String, Event> events) {
        super(context, 0, events.values().toArray(new Event[events.size()]));
        this.context = context;
        this.events = events;
    }
    @Override
    public View getView(int position, View eventItemView, ViewGroup parent) {

        String eventId = String.valueOf(position + 1);

        if(eventItemView == null) {
            eventItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        TextView eventTitle = eventItemView.findViewById(R.id.eventTitle);
        TextView eventDate = eventItemView.findViewById(R.id.eventDate);

        Button myButton1 = eventItemView.findViewById(R.id.eventButton);


        myButton1.setOnClickListener(new EditEventListener(context, eventId));

        Event item = events.get(eventId);
        Log.d(TAG, "set view");
        eventTitle.setText(item.getTitle());
        eventDate.setText(item.getEndDate().toString());

        return eventItemView;
    }
}
