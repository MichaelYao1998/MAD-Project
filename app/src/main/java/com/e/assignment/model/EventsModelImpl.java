package com.e.assignment.model;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

public class EventsModelImpl implements EventsModel {

    private Map<String, Event> events = new HashMap<>();
    private Map<String, Movie> movies = new HashMap<>();
    private Fileloader loader = new Fileloader();

    private static Context applicationContext;
    private EventsModelImpl() {
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


    public static EventsModel getSingletonInstance(Context appContext) {
        if(applicationContext == null) {
            applicationContext = appContext;
        }
        return LazyHolder.INSTANCE;
    }

}
