package com.e.assignment.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Event;

import java.util.Map;

/*
 * using AsyncTask class to process the reading events from database in other worker thread
 */
public class ReadEventFromDB extends AsyncTask<Void, Void,Map<String, Event>> {
    private databaseHelper dbh;
    public ReadEventFromDB(Context c){
        dbh = new databaseHelper(c);
    }
    @Override
    protected Map<String, Event> doInBackground(Void... voids) {
        SQLiteDatabase db = dbh.getReadableDatabase();
        Map<String, Event> me = dbh.readEvents(db);

        return me;
    }
}
