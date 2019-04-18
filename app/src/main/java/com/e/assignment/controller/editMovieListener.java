package com.e.assignment.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.e.assignment.view.ListMovieActivity;

public class editMovieListener implements View.OnClickListener{
    private Context context;

    public editMovieListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent editMovieIntent = new Intent(context, ListMovieActivity.class);
        ((Activity)context).startActivityForResult(editMovieIntent,1);
    }
}

