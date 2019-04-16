package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.e.assignment.model.Event;
import com.e.assignment.view.ListMovieActivity;

public class editMovieListener implements View.OnClickListener{
    private String TAG = getClass().getName();

    private Event selectedEvent;
    private Context context;

    public editMovieListener(Event selectedEvent, Context context) {
        this.selectedEvent = selectedEvent;
        this.context = context;
    }


    @Override
    public void onClick(View v) {
//        Toast.makeText(context, "Edit item " + itemId, Toast.LENGTH_SHORT).show();
//        Log.i("Hello", "Edit item " + itemId);
        Intent editMovieIntent = new Intent(context, ListMovieActivity.class);
        ((Activity)context).startActivityForResult(editMovieIntent,1);
    }
}

