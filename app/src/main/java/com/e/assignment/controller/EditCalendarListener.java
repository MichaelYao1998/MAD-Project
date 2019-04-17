package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.e.assignment.model.EventHandler;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.view.EditEventActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCalendarListener implements EventHandler, AdapterView.OnItemLongClickListener {
    private String eventID;
    private Context context;
    private String TAG = getClass().getName();
    private EventHandler eventHandler = null;

    public EditCalendarListener(Context context, String eventId) {
        this.context = context;
        eventID = eventId;

    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override

    public void onDayLongPress(Date date) {
        final EventsModel model = EventsModelImpl.getSingletonInstance(context);
        DateFormat df = SimpleDateFormat.getDateInstance();
        if (model.eventsArrForDay(date).size() >= 2) {

        } else if (model.eventsArrForDay(date).size() == 1) {
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, eventID);
            intent.setType("text/plain");
//            startActivity(intent);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ((Activity) context).startActivityForResult(intent, Activity.RESULT_OK);
            } else {
                Log.i(TAG, "Cannot open activity for this intent");
            }
        } else {
            //do nothing
            Toast.makeText(context, df.format(date), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        handle long-press
        if (eventHandler == null) {
            return false;
        }
        eventHandler.onDayLongPress((Date) parent.getItemAtPosition(position));

        return true;
    }
    
}
