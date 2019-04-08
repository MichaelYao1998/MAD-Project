package com.e.assignment.model;

import android.content.Context;
import android.util.Log;

import com.e.assignment.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EventsModelImpl implements EventsModel {
    private final String TAG = getClass().getName();

    private Map<String, Event> events = new HashMap<>();

    private static Context applicationContext;
    private EventsModelImpl() {
        loadEvents();
    }
    private static class LazyHolder {
        static final EventsModel INSTANCE = new EventsModelImpl();
    }
    @Override
    public Map<String, Event> getEventsList() {
        return events;
    }

    @Override
    public Event getEventById(String EventId) {
        return events.get(EventId);
    }
    public static EventsModel getSingletonInstance(Context appContext) {
        if(applicationContext == null) {
            applicationContext = appContext;
        }
        return LazyHolder.INSTANCE;
    }


    //TODO
    //Extract loadEvents() to fileLoader

    private void loadEvents() {
        events.clear();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Log.d(TAG, "load event");
        Scanner scanner = null;
        try {
            scanner = new Scanner(applicationContext.getResources().openRawResource(R.raw.events));
            while(scanner.hasNext()) {
                ;
                String line = scanner.nextLine();
                Log.d(TAG, "read file:"+line);
                String tempLine = line.replace("\"","");
                String[] components = tempLine.split(",");

                Date startDate = format.parse ( components[2] );
                Date endDate = format.parse ( components[3]);
                events.put(components[0], new EventImpl(components[0], components[1], startDate, endDate, components[4], components[5]));
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }
}
