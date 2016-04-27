package com.hzs.nitweather.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.hzs.nitweather.R;
import com.hzs.nitweather.activity.MainActivity;

public class NotificationService extends Service {
    private SharedPreferences mSharedPreferences;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = getSharedPreferences("info",MODE_PRIVATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new Notification.Builder(this).setContentTitle(mSharedPreferences.getString("titleName", null))
                .setContentText(mSharedPreferences.getString("condTxt", null)+"  当前温度"+mSharedPreferences.getString("tempFlu", null) + "℃")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return super.onStartCommand(intent, flags, startId);
    }
}
