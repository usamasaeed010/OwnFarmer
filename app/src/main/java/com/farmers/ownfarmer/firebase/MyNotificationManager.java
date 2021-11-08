package com.farmers.ownfarmer.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MyNotificationManager {

    private final Context mCtx;
    private static MyNotificationManager mInstance;
    private static final String NOTIFICATION = "notification";


    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }


    public void display(String title, String body) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx.getApplicationContext(), "notify_001");

        Intent ii = new Intent(mCtx.getApplicationContext(), MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(mCtx, 0, ii,
                PendingIntent.FLAG_UPDATE_CURRENT );
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(body);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText(body);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setColor(ContextCompat.getColor(mCtx, R.color.white))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setSummaryText(body))
                .setShowWhen(true)
                .setSound(sound)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotifyMgr.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotifyMgr.notify(0, mBuilder.build());
    }

    public void displayNotification(String title, String body) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(),
                                R.mipmap.ic_launcher))
                        .setColor(ContextCompat.getColor(mCtx, R.color.white))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setSummaryText(body))
                        .setShowWhen(true)
                        .setSound(sound)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setAutoCancel(true);
        //Vibration
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        mBuilder.setOngoing(true);

        //LED
        mBuilder.setLights(android.graphics.Color.RED, 3000, 3000);

        Intent intent = new Intent(mCtx, MainActivity.class);
        intent.putExtra(NOTIFICATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(mCtx, 100, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);


        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

}