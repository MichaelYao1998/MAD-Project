package com.e.assignment.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.model.Movie;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class databaseHelper {
    //    private Event event;
    private Movie movie;
    private Context context;
    private SQLiteDatabase database;
    private final String TAG = getClass().getName();
    private EventsModelImpl emi = new EventsModelImpl();

    public databaseHelper(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }


    /**
     * create event table if it does not exist
     */
    public void CreateEventTable(Map<String, Event> events) {


        database.execSQL(
                "CREATE TABLE IF NOT EXISTS event (\n" +
                        "    id varchar(100) NOT NULL PRIMARY KEY,\n" +
                        "    eventTitle varchar(200) NOT NULL ,\n" +
                        "    startDate datetime NOT NULL,\n" +
                        "    endDate datetime NOT NULL,\n" +
                        "    venue varchar(200) NOT NULL,\n" +
                        "    location varchar(200) NOT NULL,\n" +
                        "    movieId varchar(200) ,\n" +
                        "    attendee varchar(200) NOT NULL\n" +
                        ");"
        );

        for (Map.Entry<String, Event> entry : events.entrySet()) {
            addEvent(entry.getValue());
        }
    }

    /**
     * create movie table if it does not exist
     */
    public void CreateMovieTable(Map<String, Movie> movies) {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS movie (\n" +
                        "    id varchar(100) NOT NULL PRIMARY KEY,\n" +
                        "    title varchar(200) NOT NULL ,\n" +
                        "    year varchar(100) NOT NULL,\n" +
                        "    poster varchar(200) NOT NULL\n" +
                        ");"
        );

        for (Map.Entry<String, Movie> entry : movies.entrySet()) {
            addMovie(entry.getValue());
        }
    }

    /**
     * retrieve data from the activity
     * then add them to the database. If movie does not exist, then add the record to the database
     * otherwise update the data in the database
     */
    public void addEvent(Event selectedEvent) {
        String id = selectedEvent.getId();
        String eventTitle = selectedEvent.getTitle();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy  h:mm a");
        String startDate = sdf.format(selectedEvent.getStartDate());
        String endDate = sdf.format(selectedEvent.getEndDate());
        String venue = selectedEvent.getVenue();
        String location = selectedEvent.getLocation();
        String attendees = selectedEvent.getAttendees().toString();
        String movieId = "";

        String insertSQL = "INSERT INTO event \n" +
                "(id, eventTitle, startDate, endDate, venue,location,movieId,attendee)\n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?, ?, ?, ?);";

        String updateSQL = "UPDATE event \n" +
                "SET eventTitle = ?, \n" +
                "startDate = ?, \n" +
                "endDate = ?, \n" +
                "venue = ?, \n" +
                "location = ?, \n" +
                "movieId = ?, \n" +
                "attendee = ? \n" +
                "WHERE id = ?;\n";

        if (selectedEvent.getMovie() != null) {
            movieId = selectedEvent.getMovie().getId();
        }

        Log.d(TAG, String.valueOf(isDataAvailable(id)));
        if (isDataAvailable(id) != 0) {
            // update
            database.execSQL(updateSQL, new String[]{eventTitle, startDate, endDate,
                    venue, location, movieId, attendees, id});
        } else {
            //insert
            database.execSQL(insertSQL, new String[]{id, eventTitle, startDate, endDate,
                    venue, location, movieId, attendees});
        }
    }

    /**
     * retrieve the value from activity then add to database
     */
    public void addMovie(Movie movie) {
        String id = movie.getId();
        String title = movie.getTitle();
        String year = movie.getYear();
        String poster = movie.getPoster();

        String insertSQL = "INSERT INTO movie \n" +
                "(id, title, year, poster)\n" +
                "VALUES \n" +
                "(?, ?, ?, ?);";

        String updateSQL = "UPDATE movie \n" +
                "SET title = ?, \n" +
                "year = ?, \n" +
                "poster = ? \n" +
                "WHERE id = ?;\n";
        Log.d(TAG, String.valueOf(numOfMovies(id)));
        if (numOfMovies(id) != 0) {
            database.execSQL(updateSQL, new String[]{title, year, poster, id});
        } else {
            database.execSQL(insertSQL, new String[]{id, title, year, poster});
        }
    }

    /**
     * delete the record when click the delete button
     */
    public void deleteRecord(final Event selectedEvent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to delete this record?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sql = "DELETE FROM event WHERE id = ?";
                database.execSQL(sql, new String[]{selectedEvent.getId()});
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * check eventId whether in the database
     */

    public int isDataAvailable(String eventId) {
        int total = 0;
        try {
            Cursor c = null;
            c = database.rawQuery("select id from event where id = ?", new String[]{eventId});
            if (c.getCount() != 0)
                total = c.getCount();

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public int numOfMovies(String movieId) {
        int total = 0;
        try {
            Cursor c = null;
            c = database.rawQuery("select id from movie where id = ?", new String[]{movieId});
            if (c.getCount() != 0)
                total = c.getCount();

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
