package com.e.assignment.model;


import java.util.Date;
import java.util.Map;

public interface Event {
    String getId();
    String getTitle();

    Date getStartDate();
    Date getEndDate();
    String getVenue();
    String getLocation();
    int getAttendeesNum();
    void setMovie(Movie movie);
    Movie getMovie();
    Map<String, String> getAttendees();
    void setAttendees(String email, String name);
    void rmAttendees(String email);
}
