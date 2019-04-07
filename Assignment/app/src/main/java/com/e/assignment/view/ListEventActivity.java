package com.e.assignment.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.e.assignment.R;

public class ListEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        ItemsListViewModel myViewModel = ViewModelProviders.of(this).get(ItemsListViewModel.class);


    }
}
