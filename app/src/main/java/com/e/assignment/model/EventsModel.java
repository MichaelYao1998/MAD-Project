package com.e.assignment.model;

import java.util.Map;

public interface EventsModel {
    Map<String, Event> getEventsList();

    Event getEventById(String EventId);

}
