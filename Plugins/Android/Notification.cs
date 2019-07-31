using UnityEngine;

public class Notification
{
    private AndroidJavaObject notifier;

    public Notification()
    {
        notifier = new AndroidJavaObject("com.huongbp.unitynotificationplugin.UnityPlugin");
    }

    public void Init()
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("CreateNotificationService", unity);
    }

    public void SetAppPackage(string _package)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetAppPackage", unity, _package);
    }

    public void SetDaily(int _h, int _m, int _s)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetTimeAlarm", unity, _h, _m, _s);
    }

    public void SetContent(string _title, string _text)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetNotificationContent", unity, _title, _text);
    }
}
