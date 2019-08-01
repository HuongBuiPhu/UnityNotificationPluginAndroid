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

    private AlarmManager manager;
    private PendingIntent pendingIntent;
    private int alarmType;

    public NotificationService() {
        super("UnityService");
        alarmType = 0;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        alarmType = intent.getIntExtra(UnityPlugin.EXTRA, 0);
        StartAlarmDaily(alarmType);
    }

    private void StartAlarmDaily(int _type) {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(UnityPlugin.EXTRA, _type);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        SetCalendar(calendar, _type);

        manager.cancel(pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void SetCalendar(Calendar calendar, int _type) {
        SharedPreferences preferences = getSharedPreferences(UnityPlugin.PLAYER_PREF, Context.MODE_PRIVATE);
        int day;
        int hour;
        int minute;
        int second;
        switch (_type) {
            case UnityPlugin.FLAG_DAILY:
                day = preferences.getInt("Day", 1);
                hour = preferences.getInt("Hour", 18);
                minute = preferences.getInt("Minute", 59);
                second = preferences.getInt("Second", 59);

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                calendar.add(Calendar.DATE, day);
                break;
            case UnityPlugin.FLAG_ADD_DAY:
                day = preferences.getInt("DayAdd", 1);
                calendar.add(Calendar.DATE, day);
                break;
            case UnityPlugin.FLAG_ADD_HOUR:
                hour = preferences.getInt("HourAdd", 1);
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                break;
            case UnityPlugin.FLAG_ADD_MINUTE:
                minute = preferences.getInt("MinuteAdd", 1);
                calendar.add(Calendar.MINUTE, minute);
                break;
            case UnityPlugin.FLAG_ADD_SECOND:
                second = preferences.getInt("SecondAdd", 1);
                calendar.add(Calendar.SECOND, second);
                break;
        }
    }

}
