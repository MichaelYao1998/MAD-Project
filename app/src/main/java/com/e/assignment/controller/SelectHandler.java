package com.e.assignment.controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.e.assignment.Service.NotificationService;
import com.e.assignment.view.EditEventActivity;

import java.util.Objects;
/*
 * The class used to listen the click actions of the buttons from notification
 * handle them based on the intent
 */
public class SelectHandler extends IntentService {

    public SelectHandler() {
        super("SelectHandler");
    }

    private NotificationManager mNotificationManager;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String select = Objects.requireNonNull(intent.getExtras()).getString("select");
        String eventId = Objects.requireNonNull(intent.getExtras()).getString("event");
        String dateStr = Objects.requireNonNull(intent.getExtras()).getString("date");
        mNotificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int remindAgain = Integer.parseInt(sharedPreferences.getString("remind duration",null));
        Log.i("!", "onHandleIntent: ");
        //cancel the notification, and record them into sharedPreferences to avoid duplicate notify
        if (Objects.equals(select, "dismiss")){
            sharedPreferences.edit().putString(eventId,dateStr).commit();
            mNotificationManager.cancel(eventId.hashCode());
        }
        //cancel the notification, schedule next time notification
        //and record them into sharedPreferences to avoid duplicate notify
        else if (Objects.equals(select, "remind")){
            sharedPreferences.edit().putString(eventId,dateStr).commit();
            setRemind(eventId,remindAgain);
            mNotificationManager.cancel(eventId.hashCode());
        }
        //Start the edit event activity for user if they really want to delete the event
        //cancel the notification and record them into sharedPreferences to avoid duplicate notify
        else if (Objects.equals(select, "cancel")){
            sharedPreferences.edit().putString(eventId,dateStr).commit();
            Intent i = new Intent(getApplicationContext(), EditEventActivity.class);
            i.putExtra(Intent.EXTRA_TEXT,eventId);
            startActivity(i);
            setRemind(eventId,remindAgain);
            mNotificationManager.cancel(eventId.hashCode());
        }
    }
    /*
     * Schedule the notification based on the users setting "remind me in XX seconds" SharedPreferences
     */
    public void setRemind(String eventId,int gapTime){
        NotificationService ns = new NotificationService();
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pi;
        Intent i = new Intent(getApplicationContext(), NotificationService.class);
        i.putExtra("makeNotify","make");
        i.putExtra("event",eventId);
        pi = PendingIntent.getService(getApplicationContext(), 101, i, 0);
        Log.i("makeNotify:", "remind: ");
        am.setExact(AlarmManager.RTC_WAKEUP,ns.getNextTime(gapTime),pi);
    }

}
