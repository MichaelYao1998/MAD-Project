package com.e.assignment.view;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

public class NotificationActivity extends Activity {
    private static final String BASE_CHANNEL_ID = "channel";
    private static int channelCount = 0;
    private String channelID;
    private NotificationManager mNotificationManager;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }



}
