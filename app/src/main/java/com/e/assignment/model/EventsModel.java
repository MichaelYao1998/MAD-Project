package com.e.assignment.model;

import java.util.Date;
import java.util.Map;

public interface EventsModel {
    Map<String, Event> getEventsList();

    Map<String, Movie> getMovieList();

    Movie getMovieById(String MovieId);

    Event getEventById(String EventId);

    void setMovieToEvent(String EventId, String MovieID);
    void removeAttendeeFromEvent(String EventID, String attendeeEmail);
    void updateEvent(Event event);

    Map<Date, Event> sortTheEventList(boolean reverse);
}
