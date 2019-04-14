package com.e.assignment.model;

import java.util.Date;

public class EventImpl extends AbstractEvent {

    public EventImpl(String id,String title, Date Start, Date end, String venue, String location) {
        super(id, title, Start, end, venue,location);
    }

}
