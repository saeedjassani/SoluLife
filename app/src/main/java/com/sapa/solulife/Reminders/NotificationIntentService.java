package com.sapa.solulife.Reminders;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.sapa.solulife.R;

/**
 * Created by Pooja S on 10/1/2016.
 */
public class NotificationIntentService extends IntentService
{
    public static final String EXTRA_NOTE = "EXTRA_NOTE";
    Activity activity;

    public NotificationIntentService()
    {
        super("Notes");
    }

    public NotificationIntentService(Activity paramActivity)
    {
        super("Notes");
        this.activity = paramActivity;
    }

    protected void onHandleIntent(Intent paramIntent)
    {
        NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(this);
        String str1 = paramIntent.getStringExtra("rem_title");
        String str2 = paramIntent.getStringExtra("rem_content");
        int i = paramIntent.getExtras().getInt("rem_color");
        int j = paramIntent.getExtras().getInt("code");
        Intent localIntent = new Intent();
        localIntent.setAction("android.intent.action.SEND");
        localIntent.putExtra("android.intent.extra.TEXT", "Title: " + str1 + "\nContent: " + str2);
        localIntent.setType("text/plain");
        PendingIntent localPendingIntent = PendingIntent.getActivity(this, 0, localIntent, 0);
        Uri localUri = RingtoneManager.getDefaultUri(2);
        NotificationCompat.WearableExtender localWearableExtender = new NotificationCompat.WearableExtender().setHintHideIcon(true).setContentIcon(2130837682);
        localBuilder.setContentTitle(str1);
        localBuilder.setContentText(str2);
        localBuilder.setSmallIcon(R.drawable.home);
        localBuilder.setLights(i, 750, 750);
        localBuilder.setSound(localUri);
        localBuilder.extend(localWearableExtender);
        localBuilder.setColor(i);
        localBuilder.addAction(0, "SHARE NOTE", localPendingIntent);
        NotificationCompat.InboxStyle localInboxStyle = new NotificationCompat.InboxStyle();
        localInboxStyle.setBigContentTitle(str1);
        String[] arrayOfString = str2.split("\\r?\\n");
        int k = arrayOfString.length;
        for (int m = 0; m < k; m++)
            localInboxStyle.addLine(arrayOfString[m]);
        localBuilder.setStyle(localInboxStyle);
        localBuilder.setContentIntent(PendingIntent.getActivity(this, j, new Intent(this, ReminderActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification localNotification = localBuilder.build();
        NotificationManagerCompat.from(this).notify(j, localNotification);
    }
}