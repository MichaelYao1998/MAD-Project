package com.e.assignment.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditEventActivity extends AppCompatActivity {
    Context context;

    EventsModel model;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    String[] permissons = {Manifest.permission.READ_CONTACTS};
    Button addContactButton;
    String eventID;
    ArrayAdapter<String> adapter;

    static Calendar calendar = Calendar.getInstance();
    private Event selectedEvent = null;
    private Map<String,String> attendees = new HashMap<>();
    private AttendeeListAdapter attendeeListAdapter;
    private EditText eventTitle;
    private TextView eventMovie;
    private EditText eventStartDate;
    private EditText eventEndDate;
    private EditText eventVenue;
    private EditText eventLat;
    private EditText eventLng;
    private EditText eventEndTime;
    private EditText eventStartTime;
    int year;
    int month;
    int day;

    int hour;
    int minute;

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

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model  = EventsModelImpl.getSingletonInstance(getApplicationContext());
        Intent intent = getIntent();
        eventID = (String) intent.getExtras().get(Intent.EXTRA_TEXT);
        if (eventID!=null)
        {
            initSelectedEvent();
        }
        else {
            selectedEvent=new EventImpl("","",new Date(),new Date(),"","");
        }
        setContentView(R.layout.activity_edit_event);
        addContactButton = findViewById(R.id.addAttendee);
        if (CheckPermission(EditEventActivity.this, permissons[0])) {
            addContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(i, PICK_CONTACT);
                }
            });

        } else {
            // you do not have permission go request runtime permissions
            RequestPermission(EditEventActivity.this, permissons, REQUEST_RUNTIME_PERMISSION);
        }
        Toast.makeText(this, "asddsa" + eventID, Toast.LENGTH_SHORT).show();

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


        //TODO
        //Movie
//        attendee

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO
        //refactor
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Movie tempMovie = model.getMovieById(result);
                selectedEvent.setMovie(tempMovie);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri result = data.getData();


            String emailIdOfContact = null;
            int emailType = Email.TYPE_WORK;
            String contactName = null;


            Cursor cursor = getContentResolver().query(result, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
//                    String id = cursor.getString(cursor
//                            .getColumnIndex(BaseColumns._ID));
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

                    while (emails.moveToNext()) {
                        emailIdOfContact = emails.getString(emails
                                .getColumnIndex(Email.DATA));

                        selectedEvent.setAttendees(emailIdOfContact,contactName);
                    }
                    emails.close();
                }
            }
        }
    }
 //   @Override
//    protected void onRestart() {
//        super.onRestart();
//refresh the updated data
//        finish();
//        startActivity(getIntent());
//    }
    //TODO ask tutor
    //How to update the UI? How to Notify them?
    //writing the reading function in onStart or in onRestart is good or not?
    //Which is better?
    public  void newTimePickerDialog(final EditText editTime){
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minuteOfHour);
                        hour = hourOfDay;
                        minute = minuteOfHour;
                        String am_pm = "";
                        int hourIn12;
                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else
                            am_pm = "PM";
                        if (hourOfDay>12)
                            hourIn12=hourOfDay-12;
                        else if (hourOfDay%12==0)
                            hourIn12=12;
                        else
                            hourIn12=hourOfDay;

                        editTime.setText(String.format("%02d:%02d %s",hourIn12,minuteOfHour,am_pm));

                    }
                }, hour,minute , false);
        editTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                timePickerDialog.show();
                return false;
            }

        });

    }

    public void newDatePickerDialog(final EditText eventStartDate, final EditText editTime, final boolean isStartDate){


        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int yearSinceJesus, int monthOfYear, int dayOfMonth)
            {

                year=yearSinceJesus;
                month=monthOfYear;
                day=dayOfMonth;
                eventStartDate.setText(dayOfMonth+"/"+month+"/"+year);
                if (isStartDate==true){
                    calendar.set(year,month,day,hour,minute);
                    Log.v("!!!!!",calendar.toString());
                }
            }
        }, year, month, day);

        eventStartDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean  onTouch(View v, MotionEvent event) {
                if (isStartDate==false){

                    Log.v("!!!!!",calendar.toString());
                    datePickerDialog.getDatePicker().setMinDate(0);
                    datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                }
                datePickerDialog.show();
                return false;
            }

        });
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected  void onStart() {
        super.onStart();
        eventTitle = findViewById(R.id.editTitile);
        eventMovie = findViewById(R.id.editMovie);
        eventStartDate = findViewById(R.id.editStartDate);
        eventStartTime = findViewById(R.id.editStartTime);
        newDatePickerDialog(eventStartDate,eventStartDate,true);
        newTimePickerDialog(eventStartTime);


        eventEndDate = findViewById(R.id.editEndDate);

        eventEndTime = findViewById(R.id.editEndTime);
        newDatePickerDialog(eventEndDate,eventEndTime,false);
        newTimePickerDialog(eventEndTime);


        eventVenue = findViewById(R.id.editVenue);
        eventLat = findViewById(R.id.editLat);
        eventLng = findViewById(R.id.editLng);




        eventTitle.setText(selectedEvent.getTitle());
        eventStartDate.setText(selectedEvent.getStartDate().toString());

        eventEndDate.setText(selectedEvent.getEndDate().toString());
        eventVenue.setText(selectedEvent.getVenue());
        String[] latLng = selectedEvent.getLocation().split(",");
        //if (latLng.length)
        eventLat.setText(latLng[0]);
        eventLng.setText(latLng[1]);
        Button saveButton = findViewById(R.id.saveEvent);
        Button editMovieButton = findViewById(R.id.editMovieButton);

        //TODO ask tutor
        //Could we pass "this" from Activity class?
        //As Lecture said, it is inappropriate.

        //Do we have to use startActivityForResult?????
        editMovieButton.setOnClickListener(new editMovieListener(selectedEvent,this));


        attendees=selectedEvent.getAttendees();

        attendeeListAdapter =new AttendeeListAdapter(this,attendees);
        ListView attendeeListView =findViewById(R.id.attendeesList);
        attendeeListView.setAdapter(attendeeListAdapter);
        if (selectedEvent.getMovie() != null){
            eventMovie.setText(selectedEvent.getMovie().getTitle());
        }
        updateAttendeeListHeight(attendeeListView);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEvent.setTitle(eventTitle.getText().toString());
                selectedEvent.setLocation(eventLat.getText().toString()+","+eventLng.getText().toString());
                selectedEvent.setVenue(eventVenue.getText().toString());
                model.updateEvent(selectedEvent);
                finish();
            }
        });


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
                }  // you do not have permission show toast.

            }
        }
    }

    public void RequestPermission(Activity thisActivity, String[] Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission[0])
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission[0])) {
            } else {
                ActivityCompat.requestPermissions(thisActivity, Permission,
                        Code);
            }
        }
    }

    public boolean CheckPermission(Context context, String Permission) {
        if (ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
