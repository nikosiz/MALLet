package com.example.mallet;

import android.content.Context;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    public static void scheduleNotifications(Context context) {
        // Schedule a one-time work request for immediate notification
        OneTimeWorkRequest initialNotificationWork = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .build();
        WorkManager.getInstance(context).enqueue(initialNotificationWork);

        // Schedule a periodic work request for twice a day
        PeriodicWorkRequest periodicNotificationWork = new PeriodicWorkRequest.Builder(
                NotificationWorker.class, 12, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(context).enqueue(periodicNotificationWork);
    }
}
