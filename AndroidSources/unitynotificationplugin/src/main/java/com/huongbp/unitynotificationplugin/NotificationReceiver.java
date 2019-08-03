package com.huongbp.unitynotificationplugin;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;

import java.util.Calendar;


/**
 * Created by HuongBP on 31/7/2019.
 * Gmail: huong.bp.97@gmail.com
 */
public class NotificationReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "HUONGBP";

    private Notification.Builder builder;
    private NotificationManager manager;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        builder = new Notification.Builder(context);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        WakeDevice(context);
        CreateNotificationChannel();
        CreateNotify(context);

        int typeAlarm = intent.getIntExtra(UnityPlugin.EXTRA, 0);
        StartAlarmDaily(context, typeAlarm);

    }

    private void CreateNotify(Context _c) {
        SharedPreferences preferences = _c.getSharedPreferences(UnityPlugin.PLAYER_PREF, Context.MODE_PRIVATE);
        String title = preferences.getString("Title", "Unity Notification");
        String text = preferences.getString("Text", "Do you have played game today?");
        String app = preferences.getString("App", null);

        Bitmap largeIcon = null;
        int smallIcon = _c.getApplicationInfo().icon;
        try {
            Drawable ic = _c.getPackageManager().getApplicationIcon(app);
            largeIcon = ((BitmapDrawable) ic).getBitmap();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent intent = _c.getPackageManager().getLaunchIntentForPackage(app);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(_c, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= 21)
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);

        builder.setSmallIcon(smallIcon);
        builder.setLargeIcon(largeIcon);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        manager.notify(1412, builder.build());
    }

    private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Unity";
            String description = "Unity notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            builder.setChannelId(CHANNEL_ID);
            manager.createNotificationChannel(channel);
        }
    }

    private void WakeDevice(Context _c) {
        PowerManager manager = (PowerManager) _c.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? manager.isInteractive() : manager.isScreenOn();
        if (!isScreenOn) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl =
                    manager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "BUI");
            wl.acquire(3000);
        }
    }

    private void StartAlarmDaily(Context _c, int _type) {
        alarmManager = (AlarmManager) _c.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(_c, NotificationReceiver.class);
        intent.putExtra(UnityPlugin.EXTRA, _type);
        pendingIntent = PendingIntent.getBroadcast(_c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        SetCalendar(_c, calendar, _type);

        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void SetCalendar(Context _c, Calendar calendar, int _type) {
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
