package com.e.assignment.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventHandler;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.view.EditEventActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCalendarListener implements EventHandler, AdapterView.OnItemLongClickListener {
    private Context context;
    private String TAG = getClass().getName();
    private EventHandler eventHandler = null;

    public EditCalendarListener(Context context) {
        this.context = context;

    }
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void onDayLongPress(final Date date) {
        final EventsModel model = EventsModelImpl.getSingletonInstance(context);
        final Intent intent = new Intent(context, EditEventActivity.class);
        DateFormat df = SimpleDateFormat.getDateInstance();
        if (model.eventsArrForDay(date).size() >= 2) {
            Event[] eventsOnDay;
            eventsOnDay = model.eventsArrForDay(date).toArray(new Event[0]);
            String[] eventsTitleOnDay = new String[eventsOnDay.length];
            for (int i = 0;i<eventsOnDay.length;i++)
                eventsTitleOnDay[i] = eventsOnDay[i].getTitle();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(R.string.title);
            dialog.setItems(eventsTitleOnDay, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent.putExtra(Intent.EXTRA_TEXT, model.eventsArrForDay(date).get(which).getId());
                    context.startActivity(intent);
                }
            });
            dialog.setPositiveButton("Add New Event", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intent.putExtra("date", date.getTime());
                            context.startActivity(intent);
                        }
                    }
            );
            dialog.setNegativeButton("CANCEL", null);
            final AlertDialog alert = dialog.create();
            alert.show();
        } else if (model.eventsArrForDay(date).size() == 1) {
            intent.putExtra(Intent.EXTRA_TEXT, model.eventsArrForDay(date).get(0).getId());
            intent.setType("text/plain");
            context.startActivity(intent);
        } else {
            intent.putExtra("date", date.getTime());
            context.startActivity(intent);
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
