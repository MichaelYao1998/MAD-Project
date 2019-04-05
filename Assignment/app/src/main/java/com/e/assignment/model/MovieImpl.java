package com.e.assignment.model;

public class MovieImpl extends AbstractEvent {
    public MovieImpl(String id, String title, String Start, String end, String location, String[] attendees) {
        super(id, title, Start, end, location, attendees);
    }
}
