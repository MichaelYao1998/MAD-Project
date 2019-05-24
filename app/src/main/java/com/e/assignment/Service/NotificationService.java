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
import com.e.assignment.controller.SelectHandler;
import com.e.assignment.database.databaseHelper;
import com.e.assignment.model.Event;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
/*
 *  The service class is for checking if send notification to user
 *  It is depend on the travel time to destination for each event , threshold time and event start time.
 *  Once the notification was sent, it will be recorded into SharedPreferences to avoid multiple notify
 *  When the service run, it will also schedule next time checking based on user's setting.
 *
 */
public class NotificationService extends IntentService implements LocationListener {
    public static final String URL_HEAD = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
    private static final String API = "&mode=driving&key=AIzaSyAIHxjxhZivpxuh-G_HeQHgTU8XRpcjpiE";
    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private String Originlocation;
    private String DestiLocation="&destinations=";
    private Map<String, Event> m;
    protected LocationManager locationManager;
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        databaseHelper dh = new databaseHelper(getApplicationContext());
        m = dh.readEvents(dh.getReadableDatabase());

        // Check the intent. Make the notification, if the intent is from the selectHandler class
        if(intent!=null && intent.getExtras()!=null
                && Objects.equals(intent.getExtras().getString("makeNotify"), "make")){
            // ignore if the event is deleted.
            if (m.get(intent.getExtras().getString("event"))==null){
                return;
            }
            sharedPreferences.edit().putString(intent.getExtras().getString("event"),"").commit();
            makeNotification(m.get(intent.getExtras().getString("event")));
            return;
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        threshold = Integer.parseInt(sharedPreferences.getString("noti_threshold",null));
        checkPeriod = Integer.parseInt(sharedPreferences.getString("noti period",null));
        remindAgain = Integer.parseInt(sharedPreferences.getString("remind duration",null));


        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pi;
        pi = PendingIntent.getService(getApplicationContext(), 100, intent, 0);
        am.setExact(AlarmManager.RTC_WAKEUP,getNextTime(checkPeriod),pi);

        Originlocation = getOriginlocation();

        // loop over the event map
        for (Event value : m.values()) {
            // ignore the event which don't have the location
            if(Objects.equals(value.getLocation(), "")){
                continue;
            }
            // avoid the duplicated notification
            if (sharedPreferences.contains(value.getId())&&sharedPreferences.getString(value.getId(),null).equals(value.getStartDate().toString())){
                continue;
            }
            Calendar arriveTimePlus = Calendar.getInstance();

            ReadUrlHelper urlHelper = new ReadUrlHelper(URL_HEAD+Originlocation+DestiLocation+value.getLocation()+API);
            arriveTimePlus.add(Calendar.SECOND,urlHelper.getDuration(urlHelper.readFromUrl()));
            arriveTimePlus.add(Calendar.SECOND,threshold);

            if(arriveTimePlus.getTime().after(value.getStartDate())){
                makeNotification(value);
            }
        }
        dh.close();
    }
    /*
     * make the notification with the unique hashcode
     */
    public void makeNotification(Event event){
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.activity_notification);
        setPendingIntentToRemoteViews(contentView,event);
        mNotificationManager.notify(event.getId().hashCode(),createDefaultNotificationBuilder().setCustomContentView(contentView).build());
    }
    /*
     * get the next time based on the notification period time setting
     */
    public long getNextTime(int gapTime) {
        long now = System.currentTimeMillis();
        return now + gapTime * 1000;
    }

    /*
     * Making the pending intent to the remote view
     * set pending Intent based on the button type
     * Also set the remote view
     */
    public void setPendingIntentToRemoteViews(RemoteViews rv, Event event){
        rv.setOnClickPendingIntent(R.id.dismissButton, makePendingIntent(event,"dismiss"));
        rv.setOnClickPendingIntent(R.id.cancelButton, makePendingIntent(event,"cancel"));
        rv.setOnClickPendingIntent(R.id.remindButton, makePendingIntent(event,"remind"));
        int remindGap=remindAgain;
        rv.setTextViewText(R.id.remindButton,"remind in "+remindGap+"s");
        rv.setTextViewText(R.id.NotifyText,event.getTitle()+" is going to start!!");
    }

    /*
     * Make the pending intent with extra key "select"
     */
    private PendingIntent makePendingIntent(Event event, String select)
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
    /*
     * @return the notification builder
     */
    @SuppressLint("WrongConstant")
    private Notification.Builder createDefaultNotificationBuilder()
    {
        Notification.Builder builder = null;
        builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        return builder;
    }

    /*
     * get the users location based on gps or network
     */
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


}
