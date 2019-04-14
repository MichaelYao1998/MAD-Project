package com.e.assignment.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.e.assignment.model.EventsModel;
import com.e.assignment.model.EventsModelImpl;

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
    private static Map<String,String> attendees = new HashMap<>();
    private AttendeeListAdapter attendeeListAdapter;

    public final int PICK_CONTACT = 2015;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model  = EventsModelImpl.getSingletonInstance(getApplicationContext());
        Intent intent = getIntent();
        eventID = (String) intent.getExtras().get(Intent.EXTRA_TEXT);
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
        attendees=model.getEventById(eventID).getAttendees();

        attendeeListAdapter =new AttendeeListAdapter(this,attendees);
        ListView attendeeListView =findViewById(R.id.attendeesList);
        attendeeListView.setAdapter(attendeeListAdapter);
        Toast.makeText(this, "Edit event ID: " + eventID, Toast.LENGTH_SHORT).show();

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

                        emailType = emails.getInt(emails
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        Log.i("!!!","...COntact Name ...."
                                + contactName + "...contact Number..."
                                + emailIdOfContact+"!!!:"+emails.getString(emailType));
                        model  = EventsModelImpl.getSingletonInstance(getApplicationContext());
                        model.getEventById(eventID).setAttendees(emailIdOfContact,contactName);
                    }
                    emails.close();
                    Log.i("cursor!!!","34"
                            + contactName + "...contact Number..."
                            + data.toString());
                }
                Log.i("!!!","34"
                        + contactName + "...contact Number..."
                        + emailIdOfContact);
            }
            Log.i("!!!","...COntact Name ...."
                    + contactName + "...contact Number..."
                    + emailIdOfContact);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
//refresh the updated data
        finish();
        startActivity(getIntent());
        Log.v("restart!!!::::!!!!!::",model.getEventById(eventID).getAttendees().size()+"!");

    }
    //TODO ask tutor
    //How to update the UI? How to Notify them?
    //writing the reading function in onStart or in onRestart is good or not?
    //Which is better?
    @Override
    protected  void onStart() {
        super.onStart();
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

        //TODO ask tutor
        //Could we pass "this" from Activity class?
        //As Lecture said, it is inappropriate.

        //Do we have to use startActivityForResult?????
        editMovieButton.setOnClickListener(new editMovieListener(event.getId(),this));

        ListView attendeeListView =findViewById(R.id.attendeesList);
        View attendeesListView = findViewById(R.id.editPage);
        updateAttendeeListHeight(attendeeListView);

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
