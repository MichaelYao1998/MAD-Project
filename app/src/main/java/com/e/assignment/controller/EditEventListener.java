package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.e.assignment.view.EditEventActivity;

public class EditEventListener implements View.OnClickListener {
    private String eventID;
    private Context context;

    /*
     *  @param context  context from ListView
     *  @param eventID  Pass from ListViewAdapter for each item
     */
    public EditEventListener(Context context, String eventID) {
        this.context = context;
        this.eventID = eventID;
    }

    @Override
    public void onClick(View v) {
        Intent editItemIntent = new Intent(context, EditEventActivity.class);
        editItemIntent.putExtra(Intent.EXTRA_TEXT, eventID);
        editItemIntent.setType("text/plain");
        ((Activity)context).startActivityForResult(editItemIntent,Activity.RESULT_OK);
    }
}
