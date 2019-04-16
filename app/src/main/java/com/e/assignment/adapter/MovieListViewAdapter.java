package com.e.assignment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.e.assignment.R;
import com.e.assignment.controller.EditEventListener;
import com.e.assignment.controller.addMovieListener;
import com.e.assignment.model.Movie;

import java.util.Map;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private final String TAG = getClass().getName();
    private Context context;
    private Map<String, Movie> movies;
    public MovieListViewAdapter(Context context, Map<String, Movie> movies) {
        super(context, 0, movies.values().toArray(new Movie[movies.size()]));
        this.context = context;
        this.movies = movies;
    }

    public View getView(int position, View movieItemView, ViewGroup parent) {

        String movieId = String.valueOf(position + 1);
//        String eventId = String.valueOf(position + 1);
        if (movieItemView == null) {
            movieItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        }
        TextView movieTitle = movieItemView.findViewById(R.id.movieTitle);
        TextView movieYear = movieItemView.findViewById(R.id.movieYear);
        TextView moviePoster = movieItemView.findViewById(R.id.moviePoster);

        Movie item = movies.get(movieId);
        Log.d(TAG, "set view");
        if (item != null) {
            movieTitle.setText(item.getTitle());
            movieYear.setText(item.getYear());
            moviePoster.setText(item.getPoster());
        }

        Button addMovieButton = movieItemView.findViewById(R.id.addMovieButton);

        addMovieButton.setOnClickListener(new addMovieListener(context,item.getId()));

        return movieItemView;
    }
}
