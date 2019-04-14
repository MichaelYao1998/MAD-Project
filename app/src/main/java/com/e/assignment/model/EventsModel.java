package com.e.assignment.model;

import java.util.Map;

public interface EventsModel {
    Map<String, Event> getEventsList();

    Map<String, Movie> getMovieList();

    Movie getMovieById(String MovieId);

    Event getEventById(String EventId);

    void setMovieToEvent(String EventId, String MovieID);
    void removeAttendeeFromEvent(String EventID, String attendeeEmail);

}
