package com.e.assignment.Service;

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

import com.e.assignment.view.EditEventActivity;

import java.util.Objects;

public class SelectHandler extends IntentService {

    public SelectHandler() {
        super("SelectHandler");
    }

    private NotificationManager mNotificationManager;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String select = Objects.requireNonNull(intent.getExtras()).getString("select");
        String eventId = Objects.requireNonNull(intent.getExtras()).getString("event");
        mNotificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.i("!", "onHandleIntent: ");
        if (Objects.equals(select, "dismiss")){
            mNotificationManager.cancel(eventId.hashCode());
        }
        else if (Objects.equals(select, "remind")){
            setRemind(eventId,10);
        }
        else if (Objects.equals(select, "cancel")){
            Intent i = new Intent(getApplicationContext(), EditEventActivity.class);
            i.putExtra(Intent.EXTRA_TEXT,eventId);
            startActivity(i);
            setRemind(eventId,10);
        }
    }

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
        mNotificationManager.cancel(eventId.hashCode());
    }

}
