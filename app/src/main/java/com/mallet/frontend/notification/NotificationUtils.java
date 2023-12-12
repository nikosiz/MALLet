package com.mallet.frontend.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.mallet.R;
import com.mallet.frontend.view.common.activity.ActivityOpening;

import java.util.Random;

public class NotificationUtils {

    public static final String CHANNEL_ID = "your_channel_id";
    public static final String CHANNEL_NAME = "Your Channel Name";
    public static final String CHANNEL_DESCRIPTION = "Your Channel Description";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static SharedPreferences sharedPreferences;
    private static int lastTestScore;

    public static Notification buildRandomNotification(Context context) {
        Intent intent = new Intent(context, ActivityOpening.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        lastTestScore = sharedPreferences.getInt("lastTestScore", 0);

        String[] titles = {"MALLet misses you \uD83E\uDD7A",
                "Do you know this word?",
                "Can you do it?"};
        String[] contentTexts = {"Come back and test your knowledge!",
                "Check out today's word of the day!",
                "Last time you scored " + lastTestScore + " points. Can you beat this score?"};

        int randomIndex;// = new Random().nextInt(titles.length);

        if (lastTestScore == 0) {
            randomIndex = new Random().nextInt(2);
        } else {
            randomIndex = new Random().nextInt(titles.length);
        }

        String title = titles[randomIndex];
        String contentText = contentTexts[randomIndex];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }


    public static void showNotification(Context context, int notificationId, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);

        Log.d("NotificationUtils", "Notification shown with ID: " + notificationId);
    }

    public static void cancelAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
