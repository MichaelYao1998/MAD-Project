package com.e.assignment.model;


import android.content.Context;
import android.util.Log;

import com.e.assignment.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EventImpl extends AbstractEvent {

    public EventImpl(String id,String title, Date Start, Date end, String venue, String location) {
        super(id, title, Start, end, venue,location);
    }

}
