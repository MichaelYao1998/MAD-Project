package com.e.assignment.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.adapter.ListViewAdapter;
import com.e.assignment.controller.NetworkReceiver;
import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.model.viewModel.EventListViewModel;

import java.util.Date;
import java.util.Map;

public class ListEventActivity extends AppCompatActivity {
    ListViewAdapter mAdapter;
    EventListViewModel myViewModel;
    EventsModel eventsModel;
    SQLiteDatabase database;
    databaseHelper dbActivity;
    ListMovieActivity lma;
    NetworkReceiver nr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
//        startService(intent);

        // call the network check
        nr = new NetworkReceiver();
        nr.enable(this);

        //add to the intent filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("com.e.assignment.CONNECTIVITY_CHANGE");


        eventsModel = EventsModelImpl.getSingletonInstance(getApplicationContext());
        setContentView(R.layout.activity_list_event);
        myViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
        myViewModel.getEvents(eventsModel.isReverse()).observe(this, new Observer<Map<Date, Event>>() {
            @Override
            public void onChanged(Map<Date, Event> items) {
                // Update your UI with the loaded data.
                // Returns cached data automatically after a configuration change
                // and this method will be called again if underlying Live Data object is modified
                mAdapter = new ListViewAdapter(ListEventActivity.this, items);
                ListView myListView3 = findViewById(R.id.EventListView);
                myListView3.setAdapter(mAdapter);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        // Intent to go to the calendar Activity
        menu.findItem(R.id.menu_calendar).setIntent(
                new Intent(this, CalendarActivity.class));
        return true;
    }

    /*
     *  handle menu option
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ascending:
                Toast.makeText(this, "ascending", Toast.LENGTH_SHORT).show();
                if (eventsModel.isReverse()) {
                    eventsModel.setReverse(false);
                    refresh();
                }
                break;
            case R.id.descending:
                Toast.makeText(this, "descending", Toast.LENGTH_SHORT).show();
                if (!eventsModel.isReverse()) {
                    eventsModel.setReverse(true);
                    refresh();
                }
                break;
            case R.id.addEvent:
                Intent newEvent = new Intent(getApplicationContext(),EditEventActivity.class);
                newEvent.putExtra(Intent.EXTRA_TEXT, "");
                newEvent.setType("text/plain");
                startActivity(newEvent);
                break;
            case R.id.menu_map:
                Intent mapIntent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(mapIntent);
                break;
                //click calendar will redirect to CalendarActivity page
            case R.id.menu_calendar:
                Toast.makeText(this, "calendar", Toast.LENGTH_SHORT).show();
                // return false so intent is fired
                return false;

        }
        return super.onOptionsItemSelected(item);
    }

    /*
     *  refresh the page
     */
    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            refresh();
        }
    }


}
