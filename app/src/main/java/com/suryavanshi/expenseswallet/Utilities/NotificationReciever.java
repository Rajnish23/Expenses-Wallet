package com.suryavanshi.expenseswallet.Utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.suryavanshi.expenseswallet.Activities.HomeActivity;
import com.suryavanshi.expenseswallet.R;

public class NotificationReciever extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;


    public NotificationReciever(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create the content intent for the notification, which launches this activity
        Intent contentIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentViewSmall = new RemoteViews(context.getPackageName(),R.layout.notification_layout);
        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setCustomContentView(contentViewSmall)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //Deliver the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
