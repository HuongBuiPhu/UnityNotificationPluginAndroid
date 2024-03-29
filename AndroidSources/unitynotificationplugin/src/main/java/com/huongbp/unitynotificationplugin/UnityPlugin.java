package com.huongbp.unitynotificationplugin;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

/**
 * Created by HuongBP on 31/7/2019.
 * Gmail: huong.bp.97@gmail.com
 */

public class UnityPlugin {

    public static final String PLAYER_PREF = "Notify";

    public static final String EXTRA = "Plugin";
    public static final int FLAG_DAILY = 0;
    public static final int FLAG_ADD_DAY = 1;
    public static final int FLAG_ADD_HOUR = 2;
    public static final int FLAG_ADD_MINUTE = 3;
    public static final int FLAG_ADD_SECOND = 4;

    private int alarmType;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public UnityPlugin() {
        alarmType = FLAG_DAILY;
    }

    public void CreateNotificationService(Activity game) {
        StartAlarmDaily(game, alarmType);
    }

    public void SetNotificationContent(Activity game, String _title, String _text) {
        SharedPreferences preferences = game.getSharedPreferences(PLAYER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Title", _title);
        editor.putString("Text", _text);
        editor.commit();
    }

    //for daily notification
    public void SetDailyAlarm(Activity game, int _d, int _h, int _m, int _s) {
        alarmType = FLAG_DAILY;
        SharedPreferences preferences = game.getSharedPreferences(PLAYER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Day", _d);
        editor.putInt("Hour", _h);
        editor.putInt("Minute", _m);
        editor.putInt("Second", _s);
        editor.commit();
    }

    public void SetAppPackage(Activity game, String _package) {
        SharedPreferences preferences = game.getSharedPreferences(PLAYER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("App", _package);
        editor.commit();
    }

    //for non-daily notification
    public void SetTimeAdd(Activity game, int _type, int _time) {
        SharedPreferences preferences = game.getSharedPreferences(PLAYER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (_type) {
            case FLAG_ADD_DAY:
                alarmType = FLAG_ADD_DAY;
                editor.putInt("DayAdd", _time);
                break;
            case FLAG_ADD_HOUR:
                alarmType = FLAG_ADD_HOUR;
                editor.putInt("HourAdd", _time);
                break;
            case FLAG_ADD_MINUTE:
                alarmType = FLAG_ADD_MINUTE;
                editor.putInt("MinuteAdd", _time);
                break;
            case FLAG_ADD_SECOND:
                alarmType = FLAG_ADD_SECOND;
                editor.putInt("SecondAdd", _time);
                break;
            default:
                alarmType = FLAG_DAILY;
                editor.putInt("Day", 1);
                editor.putInt("Hour", 18);
                editor.putInt("Minute", 59);
                editor.putInt("Second", 59);
                break;
        }
        editor.commit();
    }

    private void StartAlarmDaily(Activity _c, int _type) {
        alarmManager = (AlarmManager) _c.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(_c, NotificationReceiver.class);
        intent.putExtra(UnityPlugin.EXTRA, _type);
        pendingIntent = PendingIntent.getBroadcast(_c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        SetCalendar(_c, calendar, _type);

        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void SetCalendar(Activity _c, Calendar calendar, int _type) {
        SharedPreferences preferences = _c.getSharedPreferences(UnityPlugin.PLAYER_PREF, Context.MODE_PRIVATE);
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
