package com.e.assignment.database;

import android.content.Context;
import android.os.AsyncTask;

import com.e.assignment.model.Movie;

import java.util.Map;

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
