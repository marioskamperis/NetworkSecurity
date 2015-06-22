package com.example.marios.networksecurity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                for (int i = 0; i < 3; i++) {
                    Log.d(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(500);// the faster it goes the heavier the app
                    } catch (InterruptedException e) {
                    }

                }
                Log.d(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                sendNotification("Your Security Code: "
                        + extras.get("m"));
                Log.d(TAG, "Received: " + extras.get("m").toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.gcm_cloud)
                        .setContentTitle("Your Security Code")
                        .setContentText(message);

        Intent resultIntent = new Intent(this, Generator.class);
        resultIntent.putExtra("message", message);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


//
//
//
//        Context context = getApplicationContext();
//        long when = System.currentTimeMillis(); //now
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.drawable.gcm_cloud, message, when);
//
//        Intent notificationIntent = new Intent(this, Generator.class);
//        notificationIntent.putExtra("message", String.valueOf(message));
//
////        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
////                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//
//        notification.defaults |= Notification.DEFAULT_LIGHTS;
//
//        PendingIntent intent1 = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);
//
//
//        notification.setLatestEventInfo(context, "GCM", message, intent1);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(0, notification);

    }
}
