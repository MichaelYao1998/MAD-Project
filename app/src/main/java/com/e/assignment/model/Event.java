package com.e.assignment.model;


import java.util.Date;
import java.util.Map;

public interface Event {
    void setId(String id);

    void setTitle(String title);

    void setStartDate(Date startDate);

    void setEndDate(Date endDate);

    void setVenue(String venue);

    void setLocation(String location);

    String getId();
    String getTitle();

    Date getStartDate();
    Date getEndDate();
    String getVenue();
    String getLocation();

    Movie getMovie();
    Map<String, String> getAttendees();
    void setMovie(Movie movie);
    void setAttendees(String email, String name);

    void setAttendeesList(Map<String, String> list);

    /*
     *  @param email    remove the attendee from attendee list in a event by using email
     */
    void rmAttendees(String email);
}
