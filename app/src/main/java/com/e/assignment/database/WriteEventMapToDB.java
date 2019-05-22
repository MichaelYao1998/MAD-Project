package com.e.assignment.database;

import android.content.Context;
import android.os.AsyncTask;

import com.e.assignment.model.Event;

import java.util.Map;

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
