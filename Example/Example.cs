using UnityEngine;

public class Example : MonoBehaviour
{
    
    private void Start()
    {
        Notification n = new Notification();
        n.SetDaily(1, 18, 59, 59);
        n.SetAppPackage("com.huongbp.example");
        n.SetContent("Unity Notification", "Let\'s play game now! ");
        n.notify();
    }

}
