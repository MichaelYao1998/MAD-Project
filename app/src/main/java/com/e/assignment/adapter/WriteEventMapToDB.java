package com.e.assignment.adapter;

import android.content.Context;
import android.os.AsyncTask;

import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Event;

import java.util.Map;

/*
 * using AsyncTask class to process the writing events from database in other worker thread
 */
public class WriteEventMapToDB extends AsyncTask<Map<String, Event>, Void,Void> {
    private databaseHelper dbh;
    public WriteEventMapToDB(Context c){
        dbh = new databaseHelper(c);
    }

    @Override
    protected Void doInBackground(Map<String, Event>[] maps) {

        dbh.addEventFromMap(dbh.getWritableDatabase(),maps[0]);
        return null;
    }
}
