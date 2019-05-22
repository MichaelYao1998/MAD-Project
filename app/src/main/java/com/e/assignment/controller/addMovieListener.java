package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.e.assignment.database.databaseHelper;


public class addMovieListener implements View.OnClickListener{
    private String movieID;
    private Context context;
    databaseHelper dbAdapter;
    /*
     *  @param context      context from calling
     *  @param movieID      MovieID, Pass from movieList Adapter for each item, when a movie is selected
     *                      the listener will pass the movie id back to the editPage activity
     */
    public addMovieListener(Context context,String movieID) {
        this.context = context;
        this.movieID = movieID;
    }
    @Override
    public void onClick(View v) {
        //REPLACE TO temp EVENT
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",movieID);
        ((Activity)context).setResult(Activity.RESULT_OK,returnIntent);
        ((Activity)context).finish();
    }
}
