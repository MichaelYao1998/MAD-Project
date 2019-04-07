package com.e.assignment.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AbstractEvent implements Event {
    private String id;



    private Movie movie;
    private String title;
    private Date startDate;
    private Date endDate;
    private String venue;
    private String location;
    private String[] attendees = new String[0];
    public AbstractEvent(String id, String title, Date Start, Date end, String venue, String location){
        this.id = id;
        this.title  = title;
        startDate = Start;
        endDate = end;
        this.venue = venue;
        this.location = location;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }
    @Override
    public  String getVenue() { return venue; }
    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public int getAttendeesNum() {
        return attendees.length;
    }

    @Override
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    @Override
    public Movie getMovie() {
        return movie;
    }
    @Override
    public String[] getAttendees() {
        return attendees;
    }
    @Override
    public void setAttendees(String[] attendees) {
        this.attendees = attendees;
    }
}
