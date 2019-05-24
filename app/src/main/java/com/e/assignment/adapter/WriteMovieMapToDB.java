package com.e.assignment.adapter;

import android.content.Context;
import android.os.AsyncTask;

import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Movie;

import java.util.Map;
/*
 * using AsyncTask class to process the writing movies from database in other worker thread
 */
public class WriteMovieMapToDB extends AsyncTask<Map<String, Movie>, Void,Void> {
    private databaseHelper dbh;
    public WriteMovieMapToDB(Context c){
        dbh = new databaseHelper(c);
    }


    @Override
    protected Void doInBackground(Map<String, Movie>[] maps) {

        dbh.addMovieFromMap(dbh.getWritableDatabase(),maps[0]);
        return null;
    }
}
