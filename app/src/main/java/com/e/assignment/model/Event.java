package com.e.assignment.model;

import android.app.Person;

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
    String[] getAttendees();
    void setAttendees(String[] attendees);
}
