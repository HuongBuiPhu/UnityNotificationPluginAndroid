using UnityEngine;

/// <summary>
/// Create by HuongBP on 31/7/2019
/// Gmail: huong.bp.97@gmail.com
/// </summary>

public class Notification
{
    private AndroidJavaObject notifier;
    /// <summary>
    /// contructor
    /// </summary>
    public Notification()
    {
        notifier = new AndroidJavaObject("com.huongbp.unitynotificationplugin.UnityPlugin");
    }

    /// <summary>
    /// start notify service
    /// </summary>
    public void Notify()
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("CreateNotificationService", unity);
    }

    /// <summary>
    /// Set app pakage for notification link to
    /// </summary>
    /// <param name="_package"> ex:com.huongbp.example </param>
    public void SetAppPackage(string _package)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetAppPackage", unity, _package);
    }

    /// <summary>
    /// Schedule notification notify in time
    /// </summary>
    /// <param name="_d"> day step </param>
    /// <param name="_h"> hour </param>
    /// <param name="_m"> minute </param>
    /// <param name="_s"> second </param>
    public void SetDaily(int _d, int _h, int _m, int _s)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetDailyAlarm", unity, _d, _h, _m, _s);
    }

    /// <summary>
    /// content for notification
    /// </summary>
    /// <param name="_title"></param>
    /// <param name="_text"></param>
    public void SetContent(string _title, string _text)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetNotificationContent", unity, _title, _text);
    }

    /// <summary>
    /// notify after _d days
    /// </summary>
    /// <param name="_d"></param>
    public void AddDay(int _d)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetTimeAdd", unity, 1, _d);
    }

    /// <summary>
    /// notify after _h hours
    /// </summary>
    /// <param name="_h"></param>
    public void AddHour(int _h)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetTimeAdd", unity, 2, _h);
    }

    /// <summary>
    /// notify after _m minutes
    /// </summary>
    /// <param name="_m"></param>
    public void AddMinute(int _m)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetTimeAdd", unity, 3, _m);
    }

    /// <summary>
    /// notify after _s seconds
    /// </summary>
    /// <param name="_s"></param>
    public void AddSecond(int _s)
    {
        var ac = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        var unity = ac.GetStatic<AndroidJavaObject>("currentActivity");
        notifier.Call("SetTimeAdd", unity, 4, _s);
    }

}
