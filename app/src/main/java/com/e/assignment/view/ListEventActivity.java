package com.e.assignment.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.e.assignment.R;
import com.e.assignment.adapter.ListViewAdapter;
import com.e.assignment.model.Event;
import com.e.assignment.model.viewModel.EventListViewModel;

import java.util.Map;

public class ListEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        EventListViewModel myViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
        myViewModel.getEvents().observe(this, new Observer<Map<String, Event>>() {
            @Override
            public void onChanged(Map<String, Event> items) {
                // Update your UI with the loaded data.
                // Returns cached data automatically after a configuration change
                // and this method will be called again if underlying Live Data object is modified
                ListViewAdapter mAdapter = new ListViewAdapter(ListEventActivity.this, items);
                ListView myListView3 = findViewById(R.id.EventListView);
                myListView3.setAdapter(mAdapter);
            }
        });

    }
}
