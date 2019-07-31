package com.huongbp.unitynotificationplugin;

import android.annotation.SuppressLint;
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


/**
 * Created by HuongBP on 31/7/2019.
 * Gmail: huong.bp.97@gmail.com
 */
public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "HUONGBP";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notifyService = new Intent(context, NotificationService.class);
        context.startService(notifyService);
        WakeDevice(context);
        createNotificationChannel(context);
        CreateNotify(context);
    }

    private void CreateNotify(Context _c) {
        SharedPreferences preferences = _c.getSharedPreferences(UnityPlugin.PLAYER_PREF, Context.MODE_PRIVATE);
        String title = preferences.getString("Title", "Unity Notification");
        String text = preferences.getString("Text", "Do you have played game today?");
        String app = preferences.getString("App", null);

        Bitmap icon = null;
        try {
            Drawable ic = _c.getPackageManager().getApplicationIcon(app);
            icon = ((BitmapDrawable) ic).getBitmap();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent intent = _c.getPackageManager().getLaunchIntentForPackage(app);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(_c, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(_c)
                .setSmallIcon(_c.getApplicationInfo().icon)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) _c.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1412, builder.build());
    }

    private void createNotificationChannel(Context _c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Unity";
            String description = "Unity notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = _c.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
}
