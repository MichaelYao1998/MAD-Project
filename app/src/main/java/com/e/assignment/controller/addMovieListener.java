package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.arch.lifecycle.AndroidViewModel;

import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.view.ListMovieActivity;

public class addMovieListener implements View.OnClickListener{
    private String TAG = getClass().getName();

    private String eventID;
    private String movieID;
    private Context context;


    public addMovieListener(Context context,String movieID, String eventID) {
        this.context = context;
        this.eventID = eventID;
        this.movieID = movieID;
    }
    @Override
    public void onClick(View v) {
//        Toast.makeText(context, "Edit item " + itemId, Toast.LENGTH_SHORT).show();
//        Log.i("Hello", "Edit item " + itemId);

        EventsModel events = EventsModelImpl.getSingletonInstance(context);
        events.setMovieToEvent(eventID,movieID);
        ((Activity)context).finish();

    }
}
