package com.e.assignment.model;

import android.content.Context;
import android.util.Log;

import com.e.assignment.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MovieModelImpl implements MovieModel {
    private Map<String, Movie>movies = new HashMap<>();
    private final String TAG = getClass().getName();
    private static Context applicationContext;
    private MovieModelImpl(){loadMovies();}
    private static class LazyHolder{
        static final MovieModel INSTANCE = new MovieModelImpl();
    }

    @Override
    public Map<String, Movie> getMovieList() {
        return movies;
    }

    @Override
    public Movie getMovieById(String MovieId) {
        return movies.get(MovieId);
    }

    public static MovieModel getSingletonInstance(Context appContext){
        if(applicationContext==null){
            applicationContext = appContext;
        }
        return LazyHolder.INSTANCE;
    }


    private void loadMovies() {
        movies.clear();
        Log.d(TAG,"load movies");
        Scanner scanner = null;
        try{
            scanner = new Scanner(applicationContext.getResources().openRawResource(R.raw.movies));
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                Log.d(TAG,"read file: "+line);
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
}
