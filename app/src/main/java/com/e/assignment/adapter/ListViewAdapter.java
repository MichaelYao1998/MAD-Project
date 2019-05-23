package com.e.assignment.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.e.assignment.R;
import com.e.assignment.controller.EditEventListener;
import com.e.assignment.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ListViewAdapter extends ArrayAdapter<Event>{
    private Context context;

    /*
     *  @param context      context from calling
     *  @param events       Event List from Activity
     */
    public ListViewAdapter(Context context, Map<Date, Event> events) {
        super(context, 0, events.values().toArray(new Event[events.size()]));
        this.context = context;
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
        TextView eventEndDate = eventItemView.findViewById(R.id.eventEndDate);
        TextView eventVenue = eventItemView.findViewById(R.id.eventVenue);
        String pattern = "dd MMM yyyy  h:mm a";
        DateFormat df = new SimpleDateFormat(pattern);
        Button myButton1 = eventItemView.findViewById(R.id.eventButton);


        myButton1.setOnClickListener(new EditEventListener(context, eventId));

        Event item = getItem(position);
        if (item.getMovie()!=null){
            eventMovie.setText(item.getMovie().getTitle());
        }
        else{
            eventMovie.setText(R.string.NoMoive);
        }

        eventTitle.setText(item.getTitle());
        eventVenue.setText(item.getVenue());
        eventEndDate.setText(df.format(item.getEndDate()));
        eventDate.setText(df.format(item.getStartDate()));
        if (!item.getAttendees().isEmpty())
        {
            Log.i("attendeesN", "getView: "+item.getAttendees().size());
            eventAttendees.setText(item.getAttendees().size()+"");
        }
        else {
            eventAttendees.setText(R.string.NoAttendees);
        }
        return eventItemView;
    }
}
