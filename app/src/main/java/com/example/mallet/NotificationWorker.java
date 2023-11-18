package com.example.mallet;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Perform background tasks, such as sending notifications

        NotificationUtils.cancelAllNotifications(getApplicationContext());

        sendNotification();

        Log.d("NotificationWorker", "Notification work performed successfully");

        return Result.success();
    }

    private void sendNotification() {
        // Build and show the notification here
        Notification notification = NotificationUtils.buildRandomNotification(getApplicationContext());
        NotificationUtils.showNotification(getApplicationContext(), 1, notification);
    }

    public static void scheduleReminderNotifications(Context context) {
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