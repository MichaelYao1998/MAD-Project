package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.e.assignment.view.EditEventActivity;

//TODO
public class EditEventListener implements View.OnClickListener {
    private String TAG = getClass().getName();

    private String eventID;
    private Context context;

    public EditEventListener(Context context, String eventID) {
        this.context = context;
        this.eventID = eventID;
    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(context, "Edit item " + itemId, Toast.LENGTH_SHORT).show();
//        Log.i("Hello", "Edit item " + itemId);
        Intent editItemIntent = new Intent(context, EditEventActivity.class);
        editItemIntent.putExtra(Intent.EXTRA_TEXT, eventID);
        editItemIntent.setType("text/plain");

        if(editItemIntent.resolveActivity(context.getPackageManager()) != null) {
            ((Activity)context).startActivityForResult(editItemIntent,Activity.RESULT_OK);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
    }
}
