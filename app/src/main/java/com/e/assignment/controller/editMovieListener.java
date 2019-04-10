package com.e.assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.e.assignment.view.ListMovieActivity;

public class editMovieListener implements View.OnClickListener{
    private String TAG = getClass().getName();

    private String eventID;
    private Context context;

    public editMovieListener(String eventID, Context context) {
        this.eventID = eventID;
        this.context = context;
    }


    @Override
    public void onClick(View v) {
//        Toast.makeText(context, "Edit item " + itemId, Toast.LENGTH_SHORT).show();
//        Log.i("Hello", "Edit item " + itemId);
        Intent editMovieIntent = new Intent(context, ListMovieActivity.class);
        editMovieIntent.putExtra("id", eventID);
        editMovieIntent.setType("text/plain");

        if(editMovieIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(editMovieIntent);
        } else {
            Log.i(TAG, "Cannot open activity for this intent");
        }
    }
}

