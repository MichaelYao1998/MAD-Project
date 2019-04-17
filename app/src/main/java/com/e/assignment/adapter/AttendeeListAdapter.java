package com.e.assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.e.assignment.R;

import java.util.Map;

/*
 *  ArrayAdapter for Attendees' ListView
 */
public class AttendeeListAdapter extends ArrayAdapter<String> {
    private Map<String,String> attendee;
    private String[] mKeys;

    /*
     *  @param context      context from calling
     *  @param attendee     HashMap for the editing event
     */
    public AttendeeListAdapter(Context context, Map<String,String> attendee) {
        super(context, 0, attendee.values().toArray(new String[attendee.size()]));
        this.attendee=attendee;
        mKeys = attendee.keySet().toArray(new String[attendee.size()]);
    }

    @Override
    public View getView(int position, View attendeeItemView, final ViewGroup parent){
        final String emailOfContact = mKeys[position];
        final String nameOfContact = getItem(position) ;
        if(attendeeItemView == null) {
            attendeeItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendee_list_item, parent, false);
        }
        TextView attendeeName = attendeeItemView.findViewById(R.id.attendeeName);
        final TextView attendeeEmail = attendeeItemView.findViewById(R.id.attendeeEmail);

        Button deleteAttendee= attendeeItemView.findViewById(R.id.deleteAttendee);

        attendeeName.setText(nameOfContact);
        attendeeEmail.setText(emailOfContact);
        final View finalAttendeeItemView = attendeeItemView;

        deleteAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendee.remove(emailOfContact,nameOfContact);
                finalAttendeeItemView.setVisibility(View.GONE);
            }
        });
        return attendeeItemView;
    }
}
