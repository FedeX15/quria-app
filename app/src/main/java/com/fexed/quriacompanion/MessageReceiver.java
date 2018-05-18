package com.fexed.quriacompanion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by What's That Lambda on 11/6/17.
 */

public class MessageReceiver extends FirebaseMessagingService {
    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;

    public MessageReceiver() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("NOTIF", remoteMessage.getFrom());

        final String title = remoteMessage.getNotification().getTitle();
        final String message = remoteMessage.getNotification().getBody();

        showNotifications(title, message);
    }

    private void showNotifications(String title, String msg) {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "Default";

        Notification.Builder notification = new Notification.Builder(this)
                .setContentText(msg)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.quriaicon);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(NOTIFICATION_ID, notification.build());

        String oldtxt = FileHelper.ReadFile(this.getApplicationContext(), "messages.txt");
        oldtxt = (oldtxt == "-error") ? "" : oldtxt;
        StringBuilder newtxt = new StringBuilder().append(oldtxt).append(title).append("\n").append(msg).append("\n\n");
        FileHelper.saveToFile(newtxt.toString(), getApplicationContext(), "messages.txt");
    }
}