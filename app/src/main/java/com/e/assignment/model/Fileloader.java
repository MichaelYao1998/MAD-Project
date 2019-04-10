package com.e.assignment.model;

import android.content.Context;
import android.util.Log;

import com.e.assignment.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class Fileloader {

    private final String TAG = getClass().getName();
    protected void loadMovies(Map<String, Movie> movies, Context applicationContext) {
        movies.clear();
        Scanner scanner = null;
        try{
            scanner = new Scanner(applicationContext.getResources().openRawResource(R.raw.movies));
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                String tempLine = line.replace("\"","");
                String[] components = tempLine.split(",");
                movies.put(components[0],new MovieImpl(components[0],components[1],components[2],components[3]));
            }
        }catch(Exception e){
            Log.i(TAG,e.getMessage());
        }finally{
            if(scanner!=null){
                scanner.close();
            }
        }
    }
    protected void loadEvents(Map<String, Event> events, Context applicationContext) {
        events.clear();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Log.d(TAG, "load event");
        Scanner scanner = null;
        try {
            scanner = new Scanner(applicationContext.getResources().openRawResource(R.raw.events));
            while(scanner.hasNext()) {
                ;
                String line = scanner.nextLine();
                Log.d(TAG, "read file:"+line);
                String tempLine = line.replace("\"","");
                String[] components = tempLine.split(",");

                Date startDate = format.parse ( components[2] );
                Date endDate = format.parse ( components[3]);
                events.put(components[0], new EventImpl(components[0], components[1], startDate, endDate, components[4], components[5]+","+components[6]));
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }
}
