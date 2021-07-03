package com.example.andy.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;


public class singleTonExample {

    public static final String ANDROID_CHANNEL_ID = "com.example.andy.myapplication";
    private static singleTonExample ourInstance = new singleTonExample();

    private Context appContext;

    private singleTonExample() {
    }

    public static Context get() {
        return getInstance().getContext();
    }

    public static synchronized singleTonExample getInstance() {

        return ourInstance;
    }

    public void init(Context context) {
        if (appContext == null) {
            this.appContext = context;
        }
    }

    private Context getContext() {
        return appContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notification(final MainActivity mainActivity) {
        NotificationManager notif = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
        androidChannel.enableLights(true);
        androidChannel.enableVibration(true);
        androidChannel.setLightColor(Color.GREEN);
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notif.createNotificationChannel(androidChannel);

        Notification notify = null;
        notify = new Notification.Builder(mainActivity, ANDROID_CHANNEL_ID).setContentTitle("Notification").setContentText("Sample Text").
                setContentTitle("Sample Subject").setSmallIcon(R.mipmap.ic_launcher).build();
        notif.notify(0, notify);
    }
}
