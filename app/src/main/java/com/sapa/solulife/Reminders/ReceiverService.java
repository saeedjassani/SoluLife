package com.sapa.solulife.Reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pooja S on 10/1/2016.
 */
public class ReceiverService extends BroadcastReceiver
{
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        String str1 = paramIntent.getStringExtra("title");
        String str2 = paramIntent.getStringExtra("content");
        int i = paramIntent.getExtras().getInt("code");
        int j = paramIntent.getExtras().getInt("color");
        Log.d("ReceiverService", "NotifyCode:" + i);
        Intent localIntent = new Intent(paramContext, NotificationIntentService.class);
        localIntent.putExtra("rem_title", str1);
        localIntent.putExtra("rem_content", str2);
        localIntent.putExtra("rem_color", j);
        localIntent.putExtra("code", i);
        paramContext.startService(localIntent);
    }
}