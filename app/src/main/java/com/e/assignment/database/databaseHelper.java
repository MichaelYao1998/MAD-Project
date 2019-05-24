package com.e.assignment.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.e.assignment.model.Event;
import com.e.assignment.model.EventImpl;
import com.e.assignment.model.Movie;
import com.e.assignment.model.MovieImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class databaseHelper extends SQLiteOpenHelper {
    private Movie movie;
    private Context context;
    private final String TAG = getClass().getName();
    public static final String DATABASE_NAME = "MADPROJECT";
    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
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
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS movie (\n" +
                        "    id varchar(100) NOT NULL PRIMARY KEY,\n" +
                        "    title varchar(200) NOT NULL ,\n" +
                        "    year varchar(100) NOT NULL,\n" +
                        "    poster varchar(200) NOT NULL\n" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * create event table if it does not exist
     */
    public void addEventFromMap(SQLiteDatabase database, Map<String, Event> events) {
        for (Map.Entry<String, Event> entry : events.entrySet()) {
            addEvent(database,entry.getValue());
        }
    }

    /**
     * create movie table if it does not exist
     */
    public void addMovieFromMap(SQLiteDatabase database, Map<String, Movie> movies) {
        for (Map.Entry<String, Movie> entry : movies.entrySet()) {
            addMovie(database,entry.getValue());
        }
    }

    /**
     * retrieve data from the activity
     * then add them to the database. If movie does not exist, then add the record to the database
     * otherwise update the data in the database
     */
    public void addEvent(SQLiteDatabase database,Event selectedEvent) {
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

        Log.d(TAG, String.valueOf(isDataAvailable(database,id)));

        if (isDataAvailable(database,id) != 0) {
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
    public void addMovie(SQLiteDatabase database, Movie movie) {
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
        Log.d(TAG, String.valueOf(numOfMovies(database,id)));
        if (numOfMovies(database,id) != 0) {
            database.execSQL(updateSQL, new String[]{title, year, poster, id});
        } else {
            database.execSQL(insertSQL, new String[]{id, title, year, poster});
        }
    }

    /**
     * delete the record when click the delete button
     */
    public void deleteRecord(SQLiteDatabase database, String id){
        String sql = "DELETE FROM event WHERE id = ?";
        database.execSQL(sql, new String[]{id});
    }


    /**
     * check whether eventId in the database, if exist, return the exact number of occurance
     * otherwise return 0
     */

    public int isDataAvailable(SQLiteDatabase database, String eventId) {
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

    /**
     * check whether movieId in the database, if exist, return the exact number of occurance
     * otherwise return 0
     */
    public int numOfMovies(SQLiteDatabase database, String movieId) {
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

    /**
     * fetch the movie by its id
     */
    public Movie readMovieByID(SQLiteDatabase database,String movieID){
        Cursor  cursor = database.rawQuery("select * from movie WHERE id="+movieID,null);
        Movie m = null;
        if (cursor.moveToFirst()){
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String year = cursor.getString(2);
            String poster = cursor.getString(3);
            m = new MovieImpl(id,title,year,poster);
            cursor.close();
        }

        return m;
    }
    /**
     * fetch the whole movie table
     * and return them in map type
     */
    public Map<String,Movie> readMovies(SQLiteDatabase database){
        Cursor cursor = database.rawQuery("select * from movie",null);
        Map<String,Movie> m = new HashMap<String,Movie>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Movie item = new MovieImpl(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                m.put(cursor.getString(0),item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return m;
    }
    /**
     * fetch the whole events table
     * and return them in map type
     */
    public Map<String,Event> readEvents(SQLiteDatabase database) {
        Cursor  cursor = database.rawQuery("select * from event",null);
        Map<String,Event> m = new HashMap<String,Event>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(0);
                String eventTitle = cursor.getString(1);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy  h:mm a");
                Date startDate = new Date();
                Date endDate = new Date();
                try {
                    startDate = sdf.parse(cursor.getString(2));
                    endDate = sdf.parse(cursor.getString(3));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String venue = cursor.getString(4);
                String location = cursor.getString(5);
                String[] attendeeString = cursor.getString(7).replace("{","").replace("}","").split(", ");
                Map<String,String> attendees =new HashMap<>();
                for (String i: attendeeString) {
                    String[] keyValue = i.split("=");
                    if(keyValue.length ==2){
                        attendees.put(keyValue[0],keyValue[1]);
                    }
                }
                Event item = new EventImpl(id,eventTitle,startDate,endDate,venue,location);
                item.setAttendeesList(attendees);
                if(!Objects.equals(cursor.getString(6), "")){
                    item.setMovie(readMovieByID(database,cursor.getString(6)));
                }
                m.put(cursor.getString(0),item);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return m;
    }

}
