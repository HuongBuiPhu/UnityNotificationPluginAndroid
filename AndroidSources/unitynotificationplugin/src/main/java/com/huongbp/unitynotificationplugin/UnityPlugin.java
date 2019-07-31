package com.huongbp.unitynotificationplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by HuongBP on 31/7/2019.
 * Gmail: huong.bp.97@gmail.com
 */

public class UnityPlugin {
    public static final String PLAYER_PREF = "Notify";
    public static final String TAG = "Unity";

    public UnityPlugin() {
        Log.i(TAG, "UnityPlugin: create");
    }

    public void CreateNotificationService(Activity game) {
        Intent notifyService = new Intent(game, NotificationService.class);
        game.startService(notifyService);
    }

    public void SetNotificationContent(Activity game, String _title, String _text) {
        SharedPreferences preferences = game.getSharedPreferences(PLAYER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Title", _title);
        editor.putString("Text", _text);
        editor.commit();
    }

    public void SetTimeAlarm(Activity game, int _h, int _m, int _s) {
        SharedPreferences preferences = game.getSharedPreferences(PLAYER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
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
}
