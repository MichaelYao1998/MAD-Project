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
import com.e.assignment.view.ListEventActivity;

public class NotificationService extends IntentService implements LocationListener {

    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private Location location;
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
        PendingIntent broadcast;
        broadcast = PendingIntent.getService(getApplicationContext(), 100, intent, 0);
        i = intent;
        am.setExact(AlarmManager.RTC_WAKEUP,getNextTime(),broadcast);
        Log.i("???", "onCreate: ");
        location= getLocation();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.activity_notification);
        contentView.setTextViewText(R.id.NotifyText,"notify test");
        mNotificationManager.notify(1,createDefaultNotificationBuilder("test","1").setCustomContentView(contentView).build());
        mNotificationManager.notify(2,createDefaultNotificationBuilder("test","1").setCustomContentView(contentView).build());
        if (location!=null)
            Log.i("???", "onCreate: "+location.toString());

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
    public Location getLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnable || isNetworkEnable) {
            if (isNetworkEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                if (locationManager!=null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null)
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        return location;
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
                new Intent(this, ListEventActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Intent.EXTRA_TEXT,eventId),
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
