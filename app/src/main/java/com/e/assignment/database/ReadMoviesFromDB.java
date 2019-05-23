package com.e.assignment.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.e.assignment.model.Movie;

import java.util.Map;

public class ReadMoviesFromDB extends AsyncTask<Void, Void,Map<String, Movie>> {
    private databaseHelper dbh;
    public ReadMoviesFromDB(Context c){
        dbh = new databaseHelper(c);
    }
    @Override
    protected Map<String, Movie> doInBackground(Void... voids) {
        SQLiteDatabase db = dbh.getReadableDatabase();
        Map<String, Movie> me = dbh.readMovies(db);
        return me;
    }
}
