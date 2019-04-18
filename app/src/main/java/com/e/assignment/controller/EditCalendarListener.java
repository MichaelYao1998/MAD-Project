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

    public EditCalendarListener(Context context) {
        this.context = context;

    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void onDayLongPress(Date date) {
        final EventsModel model = EventsModelImpl.getSingletonInstance(context);
        DateFormat df = SimpleDateFormat.getDateInstance();
        if (model.eventsArrForDay(date).size() >= 2) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(R.string.title);
            dialog.setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();
        } else if (model.eventsArrForDay(date).size() == 1) {
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, model.eventsArrForDay(date).get(0).getId());
            intent.setType("text/plain");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ((Activity)context).startActivityForResult(intent, Activity.RESULT_OK);
            } else {
                Log.i(TAG, "Cannot open activity for this intent");
            }
        } else {
            //do nothing
            Toast.makeText(context, df.format(date) + " does not have any events", Toast.LENGTH_SHORT).show();
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
