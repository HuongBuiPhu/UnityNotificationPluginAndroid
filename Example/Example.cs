using UnityEngine;

public class Example : MonoBehaviour
{
    
    private void Start()
    {
        Notification notify = new Notification();
        notify.SetDaily(18, 59, 59);
        notify.SetAppPackage("com.huongbp.example");
        notify.SetContent("Unity Notification", "Let\'s play game now! ");
        notify.Init();
    }

}
