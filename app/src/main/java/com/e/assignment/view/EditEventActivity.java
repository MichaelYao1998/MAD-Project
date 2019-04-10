package com.e.assignment.view;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.e.assignment.R;
import com.e.assignment.controller.EditEventListener;
import com.e.assignment.controller.editMovieListener;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;

public class EditEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Intent intent = getIntent();
        String eventID = (String) intent.getExtras().get(Intent.EXTRA_TEXT);

        EventsModel model = EventsModelImpl.getSingletonInstance(getApplicationContext());

        Event event = model.getEventById(eventID);

        EditText eventTitle = findViewById(R.id.editTitile);
        TextView eventMovie = findViewById(R.id.editMovie);
        EditText eventStartDate = findViewById(R.id.editStartDate);
        EditText eventEndDate = findViewById(R.id.editEndDate);
        EditText eventVenue = findViewById(R.id.editVenue);
        EditText eventLat = findViewById(R.id.editLat);
        EditText eventLng = findViewById(R.id.editLng);
        if (event.getMovie() != null){
            eventMovie.setText(event.getMovie().getTitle());
        }
        eventTitle.setText(event.getTitle());
        eventStartDate.setText(event.getStartDate().toString());
        eventEndDate.setText(event.getEndDate().toString());
        eventVenue.setText(event.getVenue());
        String[] latLng = event.getLocation().split(",");
        eventLat.setText(latLng[0]);
        eventLng.setText(latLng[1]);

        Button editMovieButton = findViewById(R.id.editMovieButton);

        editMovieButton.setOnClickListener(new editMovieListener(event.getId(),this));

        //TODO
        //Movie
//        attendee


        Toast.makeText(this, "Edit event ID: " + eventID, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
//refresh the updated data
        finish();
        startActivity(getIntent());

    }
}
