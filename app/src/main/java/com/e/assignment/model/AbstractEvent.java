package com.e.assignment.model;

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
    private Map<String, String> attendees = new HashMap<>();
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setVenue(String venue) {
        this.venue = venue;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

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
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    @Override
    public Movie getMovie() {
        return movie;
    }
    @Override
    public Map<String, String> getAttendees() {
        return attendees;
    }
    @Override
    public void setAttendees(String email, String name) {
        attendees.put(email,name);
    }
    @Override
    public void setAttendeesList(Map<String, String> list) {
        attendees=list;
    }

    @Override
    public void rmAttendees(String email) {
        attendees.remove(email);
    }
}
