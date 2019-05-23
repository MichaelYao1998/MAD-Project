package com.e.assignment.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.e.assignment.R;
import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Event;
import com.e.assignment.view.EditEventActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class NotificationService extends IntentService implements LocationListener {
    public static final String URL_HEAD = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
    private static final String API = "&mode=driving&key=AIzaSyAIHxjxhZivpxuh-G_HeQHgTU8XRpcjpiE";
    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private String Originlocation;
    private int threadhold = 5000;
    private String DestiLocation="&destinations=";

    protected LocationManager locationManager;
    private Intent i;
    public NotificationService() {
        super("NotificationService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onHandleIntent(Intent intent) {
        super.onCreate();
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Activity.ALARM_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pi;
        pi = PendingIntent.getService(getApplicationContext(), 100, intent, 0);
        i = intent;
        am.setExact(AlarmManager.RTC_WAKEUP,getNextTime(),pi);

        Originlocation = getOriginlocation();
        databaseHelper dh = new databaseHelper(getApplicationContext());
        Map<String, Event> m = dh.readEvents(dh.getReadableDatabase());
        int count=0;
        for (Event value : m.values()) {
            if(Objects.equals(value.getLocation(), "")){
                continue;
            }
            Calendar arriveTimePlus = Calendar.getInstance();


            arriveTimePlus.add(Calendar.SECOND,getDuration(readFromUrl(value.getLocation())));
            arriveTimePlus.add(Calendar.SECOND,threadhold);

            Log.i("calendar", "time: "+ arriveTimePlus.getTime().toString());
            if(arriveTimePlus.getTime().after(value.getStartDate())){
                makeNotification(count,value.getId());
            }
            count++;
        }

    }
    public int getDuration(String jsonStr){
        int durationInSec = 0;

        try {
            JSONObject body = new JSONObject(jsonStr);
            JSONArray rowArray = body.getJSONArray("rows");
            JSONObject row = rowArray.getJSONObject(0);
            JSONArray elementsArray = row.getJSONArray("elements");
            JSONObject elements = elementsArray.getJSONObject(0);
            JSONObject duration = elements.getJSONObject("duration");
            durationInSec = duration.getInt("value");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return durationInSec;
    }
    public String readFromUrl(String destination){
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            Log.i("URL:",URL_HEAD+Originlocation+DestiLocation+destination+API);
            URL url = new URL(URL_HEAD+Originlocation+DestiLocation+destination+API);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();
    }
    private void makeNotification(int id,String eventID){
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.activity_notification);
        contentView.setTextViewText(R.id.NotifyText,"notify test");
        mNotificationManager.notify(id,createDefaultNotificationBuilder("test",eventID).setCustomContentView(contentView).build());
    }
    private long getNextTime() {
        long now = System.currentTimeMillis();
        return now + 10 * 1000;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d("MyIntentService", "onDestroy");
    }
    @SuppressLint("MissingPermission")
    public String getOriginlocation() {
        Location location = null;
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnable || isNetworkEnable) {
            if (isNetworkEnable){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                if (locationManager!=null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnable){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null)
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        return location.getLatitude() + "," + location.getLongitude();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private PendingIntent makeEventIntent(String eventId)
    {
        // The PendingIntent to launch our activity if the user selects this
        // Notification. Note the use of FLAG_UPDATE_CURRENT so that if there
        // is already an active matching pending intent, we will update its
        // extras to be the ones passed in here.
        return PendingIntent.getActivity(this, 0,
                new Intent(this, EditEventActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Intent.EXTRA_TEXT,eventId),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @SuppressLint("WrongConstant")
    private Notification.Builder createDefaultNotificationBuilder(CharSequence text, String eventId)
    {
        Notification.Builder builder = null;
        builder = new Notification.Builder(this)
                .setContentTitle("TEST")//.setContentTitle(getText(R.string.status_bar_notifications_mood_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(text)
                .setContentIntent(makeEventIntent(eventId))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        return builder;
    }
}
