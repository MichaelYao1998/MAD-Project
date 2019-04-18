package com.e.assignment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface EventsModel {
    Map<String, Event> getEventsList();

    Map<String, Movie> getMovieList();

    Movie getMovieById(String MovieId);

    Event getEventById(String EventId);

    /*
     *  @param event    put the event into the HashMap in singleton
     */
    void updateEvent(Event event);

    /*
     *  check the sorting option in the singleton
     */
    boolean isReverse();

    /*
     *  @param reverse      update the sorting option
     */
    void setReverse(boolean reverse);

    /*
     *  @param reverse      true for descending sorting, false for ascending.
     *  @return     a HashMap sorted by date
     */
    Map<Date, Event> sortTheEventList(boolean reverse);

    /*
     *  generate a random id contain number and alphabet
     */
    String eventIdGenerator();

    /*
     *  transfer the date type into string array
     */
    String[] dateToString(Date date);

    /*
     *  @return a arraylist which contain all events in a specific @param date
     */
    ArrayList<Event> eventsArrForDay(Date date);

    /*
     *  remove event from the event list
     */
    void deleteEvent(String eventID);
}
