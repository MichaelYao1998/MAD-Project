package com.e.assignment.model;

import android.content.Context;
import android.util.Log;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EventsModelImpl implements EventsModel {

    private Map<String, Event> events = new HashMap<>();
    private Map<String, Movie> movies = new HashMap<>();
    private Fileloader loader = new Fileloader();

    private static Context applicationContext;
    public EventsModelImpl() {
        loader.loadEvents(events,applicationContext);
        loader.loadMovies(movies,applicationContext);
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
    public void setMovieToEvent(String EventId, String MovieID) {
        Movie tempMovie = getMovieById(MovieID);
        Event tempEvent = getEventById(EventId);
        if (tempEvent!=null){
            events.get(EventId).setMovie(tempMovie);
        }
    }

    @Override
    public void removeAttendeeFromEvent(String EventID, String attendeeEmail) {

        events.get(EventID).rmAttendees(attendeeEmail);
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
    public void updateEvent(Event event){
        if (event.getId()!=null)
        this.events.put(event.getId(),event);
    }

}
