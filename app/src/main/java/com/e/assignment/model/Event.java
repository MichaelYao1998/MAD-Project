package com.e.assignment.model;

import java.util.Map;

public interface Event {
    String getId();
    String getIitle();
    //?
    String getStartDate();
    String getEndDate();

    String getLocation(long lat, long longi);//?
    String[] getAttendees();
    Map<String, Event>getEventList();
    Event getEventById(String eventId);}
