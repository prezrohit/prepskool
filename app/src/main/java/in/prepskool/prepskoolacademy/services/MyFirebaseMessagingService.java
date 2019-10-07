package in.prepskool.prepskoolacademy.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.prepskool.prepskoolacademy.activities.HomeActivity;
import in.prepskool.prepskoolacademy.activities.SplashActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private  NotificationManager notificationManager;
    private static final String ADMIN_CHANNEL_ID ="admin_channel";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        notificationManager =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.putExtra("body", "1");
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,  notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority( Notification.PRIORITY_HIGH )
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(String adminChannelName, String adminChannelDescription){
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor( Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
