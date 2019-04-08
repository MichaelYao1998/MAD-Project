package com.e.assignment.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.e.assignment.R;
import com.e.assignment.adapter.MovieListViewAdapter;
import com.e.assignment.model.Movie;
import com.e.assignment.model.viewModel.MovieListViewModel;

import java.util.Map;

public class ListMovieActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        MovieListViewModel myViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        myViewModel.getMovie().observe(this, new Observer<Map<String, Movie>>() {
            @Override
            public void onChanged(Map<String, Movie> items) {
                // Update your UI with the loaded data.
                // Returns cached data automatically after a configuration change
                // and this method will be called again if underlying Live Data object is modified);
                MovieListViewAdapter mAdapter = new MovieListViewAdapter(ListMovieActivity.this, items);
                ListView movieListView = findViewById(R.id.MovieListView);
                movieListView.setAdapter(mAdapter);
            }
        });
    }
}
