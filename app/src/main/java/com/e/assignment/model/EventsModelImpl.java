package com.e.assignment.model;

import android.content.Context;
import android.util.Log;

import com.e.assignment.adapter.ReadEventFromDB;
import com.e.assignment.adapter.ReadMoviesFromDB;
import com.e.assignment.adapter.WriteEventMapToDB;
import com.e.assignment.adapter.WriteMovieMapToDB;
import com.e.assignment.database.databaseHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class EventsModelImpl implements EventsModel {

    private Map<String, Event> events = new HashMap<>();
    private Map<String, Movie> movies = new HashMap<>();
    private Fileloader loader = new Fileloader();

    private boolean isReverse = false;
    private static Context applicationContext;
    public EventsModelImpl() {
        if (doesDatabaseExist(applicationContext, databaseHelper.DATABASE_NAME)){
            ReadEventFromDB re = new ReadEventFromDB(applicationContext);
            ReadMoviesFromDB rm = new ReadMoviesFromDB(applicationContext);
            try {
                events = re.execute().get();
                movies = rm.execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            new databaseHelper(applicationContext);
            loader.loadEvents(events,applicationContext);
            loader.loadMovies(movies,applicationContext);
            WriteEventMapToDB we = new WriteEventMapToDB(applicationContext);
            WriteMovieMapToDB wm = new WriteMovieMapToDB(applicationContext);
            we.execute(events);
            wm.execute(movies);
        }

    }
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }

    private static class LazyHolder {
        static final EventsModel INSTANCE = new EventsModelImpl();
    }
    @Override
    public Map<String, Event> getEventsList() {
        return events;
    }
    @Override
    public Map<String, Movie> getMovieList() {
        return movies;
    }
    @Override
    public Movie getMovieById(String MovieId) {
        return movies.get(MovieId);
    }
    @Override
    public Event getEventById(String EventId) {
        return events.get(EventId);
    }

    @Override
    public Map<Date,Event> sortTheEventList(boolean reverse){
        Map<Date,Event> dateTemp;
        if (!reverse){
            dateTemp = new TreeMap<>();
        }
        else
        {
            dateTemp = new TreeMap<Date, Event>(Collections.reverseOrder());
        }

        for (Object o : events.entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            Log.v("!!!!!no sort!",((Event)pair.getValue()).getStartDate().toString()+ " = ");
            pair.getKey();
            Event e = (Event) pair.getValue();
            dateTemp.put(((Event) pair.getValue()).getStartDate(), (Event) pair.getValue());
        }
        return dateTemp;
    }

    public static EventsModel getSingletonInstance(Context appContext) {
        if(applicationContext == null) {
            applicationContext = appContext;
        }
        return LazyHolder.INSTANCE;
    }
    @Override
    public void updateEvent(Event event){
        if (event.getId()!=null)
        this.events.put(event.getId(),event);
    }
    @Override
    public String eventIdGenerator(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String number = "1234567890";
        String charRange = number+alphabet.toUpperCase()+alphabet;
        StringBuilder sb = new StringBuilder();
        int IdLength = 10;
        while (IdLength-- != 0) {
            int character = (int)(Math.random()*charRange.length());
            sb.append(charRange.charAt(character));
        }
        return sb.toString();
    }

    @Override
    public String[] dateToString(Date date) {
        String pattern = "dd-MM-yyyy h:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String dateStr = simpleDateFormat.format(date);

        return dateStr.split(" ");
    }

    @Override
    public ArrayList<Event> eventsArrForDay(Date date) {
        ArrayList<Event> eventsInDay = new ArrayList<>();
        Map<Date,Event> mapInDate = sortTheEventList(false);
        for (Map.Entry<Date, Event> entry : mapInDate.entrySet()) {
            if (dateToString(entry.getKey())[0].equals(dateToString(date)[0])) {
                Log.v("???", dateToString(entry.getKey())[0]);
                eventsInDay.add(entry.getValue());
            }
        }
        return eventsInDay;
    }

    @Override
    public void deleteEvent(String eventID){
        events.remove(eventID);
    }

}
