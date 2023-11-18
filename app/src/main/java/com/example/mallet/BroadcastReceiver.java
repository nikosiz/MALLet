package com.example.mallet;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent repeatingIntent = new Intent(context, ActivityMain.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Title")
                .setContentText("Notification Text")
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, builder.build());

        // Note: It's not clear what you intend to do with this line, as it seems unrelated to the notification
        intent.putExtra("fragment_class", FragmentUserLibrary_Sets.class);

        // Obtain the context from the Intent
        Context startActivityContext = (intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) != 0 ? context : null;

        if (startActivityContext != null) {
            startActivityContext.startActivity(intent);
        } else {
            // Log an error or handle it appropriately
        }
    }


}
