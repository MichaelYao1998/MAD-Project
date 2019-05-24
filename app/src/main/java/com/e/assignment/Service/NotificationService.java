package com.e.assignment.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.e.assignment.R;
import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Event;

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
    private String DestiLocation="&destinations=";
    private Map<String, Event> m;
    protected LocationManager locationManager;
    private Intent i;
    public NotificationService() {
        super("NotificationService");
    }

    private NotificationManager mNotificationManager;
    public int threshold;
    public int checkPeriod;
    public int remindAgain;
    @Override
    public void onHandleIntent(Intent intent) {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        databaseHelper dh = new databaseHelper(getApplicationContext());
        m = dh.readEvents(dh.getReadableDatabase());
        if(intent!=null && intent.getExtras()!=null
                && Objects.equals(intent.getExtras().getString("makeNotify"), "make")){

            Log.i("makeNotify:", "makeNotify: ");
            makeNotification(m.get(intent.getExtras().getString("event")));
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        threshold = Integer.parseInt(sharedPreferences.getString("noti_threshold",null));

        checkPeriod = Integer.parseInt(sharedPreferences.getString("noti period",null));



        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pi;
        pi = PendingIntent.getService(getApplicationContext(), 100, intent, 0);
        i = intent;
        am.setExact(AlarmManager.RTC_WAKEUP,getNextTime(checkPeriod),pi);

        Originlocation = getOriginlocation();
        for (Event value : m.values()) {
            if(Objects.equals(value.getLocation(), "")){
                continue;
            }
            if (sharedPreferences.contains(value.getId())&&sharedPreferences.getString(value.getId(),null).equals(value.getStartDate().toString())){
                continue;
            }
            Calendar arriveTimePlus = Calendar.getInstance();



            Log.i("duration", "event time: "+ getDuration(readFromUrl(value.getLocation())));
            arriveTimePlus.add(Calendar.SECOND,getDuration(readFromUrl(value.getLocation())));
            arriveTimePlus.add(Calendar.SECOND,threshold);

            if(arriveTimePlus.getTime().after(value.getStartDate())){


                makeNotification(value);
            }
        }
        dh.close();
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
    public void makeNotification(Event event){
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.activity_notification);
        setPendingIntentToRemoteViews(contentView,event);
        mNotificationManager.notify(event.getId().hashCode(),createDefaultNotificationBuilder("test",event.getId()).setCustomContentView(contentView).build());
    }
    public long getNextTime(int gapTime) {
        long now = System.currentTimeMillis();
        return now + gapTime * 1000;
    }
    public void setPendingIntentToRemoteViews(RemoteViews rv, Event event){
        rv.setOnClickPendingIntent(R.id.dismissButton,makePnedingIntent(event,"dismiss"));
        rv.setOnClickPendingIntent(R.id.cancelButton,makePnedingIntent(event,"cancel"));
        rv.setOnClickPendingIntent(R.id.remindButton,makePnedingIntent(event,"remind"));
        int remindGap=remindAgain;
        rv.setTextViewText(R.id.remindButton,"remind in "+remindGap+" minutes");
        rv.setTextViewText(R.id.NotifyText,event.getTitle());

    }
    private PendingIntent makePnedingIntent(Event event,String select)
    {
        return PendingIntent.getService(this,(event.getId()+select).hashCode(),
                new Intent(this, SelectHandler.class).putExtra("event",event.getId()).putExtra("select",select).putExtra("date",event.getStartDate().toString()),
                PendingIntent.FLAG_UPDATE_CURRENT);
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

    @SuppressLint("WrongConstant")
    private Notification.Builder createDefaultNotificationBuilder(CharSequence text, String eventId)
    {
        Notification.Builder builder = null;
        builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        return builder;
    }
}
