package com.e.assignment.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.assignment.R;
import com.e.assignment.adapter.AttendeeListAdapter;
import com.e.assignment.controller.editMovieListener;
import com.e.assignment.model.Event;
import com.e.assignment.model.EventImpl;
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;
import com.e.assignment.model.Movie;
import com.e.assignment.model.MovieImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditEventActivity extends AppCompatActivity {
    private EventsModel model;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    private String[] permissions = {Manifest.permission.READ_CONTACTS};
    private Button addContactButton;
    private String eventID;
    static Calendar calendar = Calendar.getInstance();
    static Calendar calendarEnd = Calendar.getInstance();
    private Event selectedEvent = null;
    private EditText eventTitle;
    private TextView eventMovie;
    private EditText eventStartDate;
    private EditText eventEndDate;
    private EditText eventVenue;
    private EditText eventLat;
    private EditText eventLng;
    private EditText eventEndTime;
    private EditText eventStartTime;
    public final int PICK_CONTACT = 2015;
    public void initSelectedEvent(){
        //Init a temp object
        selectedEvent = new EventImpl(model.getEventById(eventID).getId(),
                model.getEventById(eventID).getTitle(),
                model.getEventById(eventID).getStartDate(),
                model.getEventById(eventID).getEndDate(),
                model.getEventById(eventID).getVenue(),
                model.getEventById(eventID).getLocation()
        );
        if (model.getEventById(eventID).getAttendees()!=null){
            Map<String,String> tempList = new HashMap<>(model.getEventById(eventID).getAttendees());
            selectedEvent.setAttendeesList(tempList);
        }
        if (model.getEventById(eventID).getMovie()!=null){
            Movie reference = model.getEventById(eventID).getMovie();
            Movie tempMovie = new MovieImpl(reference.getId(),reference.getTitle(),reference.getYear(),reference.getPoster());
            selectedEvent.setMovie(tempMovie);
        }
        Button deleteButton = findViewById(R.id.deleteEvent);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.deleteEvent(eventID);
                finish();
            }
        });
        calendar.setTime(selectedEvent.getStartDate());
        calendarEnd.setTime(selectedEvent.getEndDate());

    }
    public void saveTempEvent() {
        selectedEvent.setTitle(eventTitle.getText().toString());
        selectedEvent.setVenue(eventVenue.getText().toString());
        selectedEvent.setLocation(eventLat.getText().toString()+","+eventLng.getText().toString());
    }
    @Override
    public void onPause() {
        super.onPause();
        saveTempEvent();
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        // etc.
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model  = EventsModelImpl.getSingletonInstance(getApplicationContext());
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit_event);
        eventTitle = findViewById(R.id.editTitile);
        eventMovie = findViewById(R.id.editMovie);
        eventStartDate = findViewById(R.id.editStartDate);
        eventStartTime = findViewById(R.id.editStartTime);
        eventEndDate = findViewById(R.id.editEndDate);
        eventEndTime = findViewById(R.id.editEndTime);
        eventVenue = findViewById(R.id.editVenue);
        eventLat = findViewById(R.id.editLat);
        eventLng = findViewById(R.id.editLng);
        eventID = (String) intent.getExtras().get(Intent.EXTRA_TEXT);
        if (!eventID.equals(""))
        {
            initSelectedEvent();
        }
        else {
            calendar.setTime(new Date());
            calendarEnd.setTime(new Date());
            selectedEvent=new EventImpl(model.eventIdGenerator(),"",calendar.getTime(),calendarEnd.getTime(),"","");
        }
        addContactButton = findViewById(R.id.addAttendee);
        ContactPicker contactPicker = new ContactPicker();
        contactPicker.setPickerOnButton(addContactButton, permissions,EditEventActivity.this);
    }
    public static void updateAttendeeListHeight(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup view = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, view);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams param = listView.getLayoutParams();
        param.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(param);
        listView.requestLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Movie tempMovie = model.getMovieById(result);
                selectedEvent.setMovie(tempMovie);
            }
        }
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri result = data.getData();
            String emailIdOfContact = null;
            String contactName = null;
            Cursor cursor = getContentResolver().query(result, null, null, null, null);
            if ((cursor != null ? cursor.getCount() : 0) > 0) {
                while (cursor.moveToNext()) {
                    String id = result.getLastPathSegment();
                    contactName = cursor
                            .getString(cursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor cr = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    Cursor emails = getContentResolver().query(Email.CONTENT_URI, null,
                            Email.CONTACT_ID + " = " + id, null, null);

                    while (emails != null && emails.moveToNext()) {
                        emailIdOfContact = emails.getString(emails
                                .getColumnIndex(Email.DATA));

                        selectedEvent.setAttendees(emailIdOfContact,contactName);
                    }
                    emails.close();
                    cr.close();
                }
            }

            cursor.close();
        }
    }

    @Override
    protected  void onStart() {
        super.onStart();
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.newDatePickerDialog(this,eventStartDate,true,selectedEvent);
        dateTimePicker.newTimePickerDialog(this,eventStartTime,true,selectedEvent);
        dateTimePicker.newDatePickerDialog(this,eventEndDate,false,selectedEvent);
        dateTimePicker.newTimePickerDialog(this,eventEndTime,false,selectedEvent);

        eventTitle.setText(selectedEvent.getTitle());

        String[] startDateStr = model.dateToString(selectedEvent.getStartDate());
        String[] endDateStr = model.dateToString(selectedEvent.getEndDate());
        eventStartDate.setText(startDateStr[0]);
        eventStartTime.setText(startDateStr[1]+startDateStr[2]);
        eventEndDate.setText(endDateStr[0]);
        eventEndTime.setText(endDateStr[1]+endDateStr[2]);
        eventVenue.setText(selectedEvent.getVenue());
        String[] latLng = selectedEvent.getLocation().split(",");
        if (latLng.length>1){
            eventLat.setText(latLng[0]);
            eventLng.setText(latLng[1]);
        }
        Button saveButton = findViewById(R.id.saveEvent);
        Button editMovieButton = findViewById(R.id.editMovieButton);
        editMovieButton.setOnClickListener(new editMovieListener(selectedEvent,this));

        Map<String, String> attendees = selectedEvent.getAttendees();
        AttendeeListAdapter attendeeListAdapter = new AttendeeListAdapter(this, attendees);
        ListView attendeeListView =findViewById(R.id.attendeesList);
        attendeeListView.setAdapter(attendeeListAdapter);
        if (selectedEvent.getMovie() != null){
            eventMovie.setText(selectedEvent.getMovie().getTitle());
        }
        updateAttendeeListHeight(attendeeListView);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTempEvent();
                if (validation()) {
                    model.updateEvent(selectedEvent);
                    finish();
                }
            }
        });
    }
    public boolean validation(){
        boolean isValid = true;
        if (!selectedEvent.getStartDate().before(selectedEvent.getEndDate())){
            isValid=false;
            Toast warn = Toast.makeText(getApplicationContext(),"Invalid End Date!!",Toast.LENGTH_LONG);
            warn.show();
        }
        return isValid;
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case REQUEST_RUNTIME_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // you have permission go ahead
                    addContactButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                            startActivityForResult(i, PICK_CONTACT);
                        }
                    });
                }
            }
        }
    }
}