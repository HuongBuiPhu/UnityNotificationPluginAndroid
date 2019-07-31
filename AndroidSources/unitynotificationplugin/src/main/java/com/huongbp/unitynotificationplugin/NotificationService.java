package com.huongbp.unitynotificationplugin;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;


/**
 * Created by HuongBP on 31/7/2019.
 * Gmail: huong.bp.97@gmail.com
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("UnityService");
    }

    private AlarmManager manager;
    private PendingIntent pendingIntent;

    @Override
    protected void onHandleIntent(Intent intent) {
        StartAlarm();
    }

    private void StartAlarm() {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences preferences = getSharedPreferences(UnityPlugin.PLAYER_PREF, Context.MODE_PRIVATE);
        int hour = preferences.getInt("Hour", 18);
        int minute = preferences.getInt("Minute", 59);
        int second = preferences.getInt("Second", 59);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.add(Calendar.DATE, 1);

        manager.cancel(pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
