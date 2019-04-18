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
    private String movieID;
    private Context context;

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
        //TODO
        //REPLACE TO temp EVENT
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",movieID);
        ((Activity)context).setResult(Activity.RESULT_OK,returnIntent);
        ((Activity)context).finish();
    }
}
