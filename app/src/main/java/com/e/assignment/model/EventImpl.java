package com.e.assignment.model;

import android.content.Context;
import android.util.Log;

import com.e.assignment.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EventImpl extends AbstractEvent {

    public EventImpl(String id, String title, String Start, String end, String location, String[] attendees) {
        super(id, title, Start, end, location, attendees);

    }

    private String TAG = getClass().getName();
    private Map<String, Event> list = new HashMap<>();
    private static Context appllicationContext;

    // singleton support
    // thread safe lazy initialisation:
    // IMPORTANT: we need a context but should pass getApplicationContext() since this exists for the lifetime
    // of the app anyway
    private static class LasyHolder {
        private static String[] attendees;
        private static String id;
        private static String title;
        private static String Start;
        private static String end;
        private static String location;

        public static final Event INSTANCE = new EventImpl(id, title, Start, end, location, attendees);
    }

    //We need application context to access resource so that we can read from raw data files stored in resource
    public static Event getSingletonInstance(Context apptext) {
        if (appllicationContext == null) {
            appllicationContext = apptext;
        }
        return LasyHolder.INSTANCE;
    }

    public Event getEventById(String eventId) {
        return list.get(eventId);
    }

    private void loadEvent() {
        list.clear();
        Scanner scanner = null;
        try {
            scanner = new Scanner(appllicationContext.getResources().openRawResource(R.raw.events));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] components = line.split(",");
                list.put(components[0], new EventImpl(components[0], components[1], components[2], components[3], components[4], components));
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

}
