package com.e.assignment.model;

public interface Event {
    String getId();
    String getIitle();
    //?
    String getStartDate();
    String getEndDate();

    String getLocation(long lat, long longi);//?
    String[] getAttendees();
}
