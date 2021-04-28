package com.example.android.tidyup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title, message;
    private String TAG = "MyFirebaseMessagingService";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        Log.d(TAG, message);

        String CHANNEL_ID="MESSAGE";
        String CHANNEL_NAME="MESSAGE";
        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_group_24)
                .setPriority(4)
                .setContentTitle(title)
                .setContentText(message)
                .build();
        manager.notify(0,notification);
    }

}
