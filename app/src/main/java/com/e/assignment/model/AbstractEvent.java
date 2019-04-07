package com.e.assignment.model;

import java.util.HashMap;
import java.util.Map;

public class AbstractEvent implements Event {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String[] attendees;
    private Map<String, Event> list = new HashMap<>();
    public AbstractEvent(String id, String title, String Start, String end, String location, String[] attendees){
        this.id =id;
        this.title  = title;
        startDate = Start;
        endDate = end;
        this.location = location;
        this.attendees = attendees;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getIitle() {
        return title;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String getLocation(long latitude, long longitude) {
        return location;
    }

    @Override
    public String[] getAttendees() {
        return attendees;
    }

    @Override
    public Map<String, Event> getEventList() {
        return list;
    }

    @Override
    public Event getEventById(String eventId) {
        return list.get(eventId);
    }
}
