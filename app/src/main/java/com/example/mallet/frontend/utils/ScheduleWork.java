package com.example.mallet.frontend.utils;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.mallet.frontend.notification.NotificationWorker;

import java.util.concurrent.TimeUnit;

public class ScheduleWork {

    public static void schedulePeriodicWork() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                NotificationWorker.class,
                1, // repeat interval in minutes
                TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance().enqueue(workRequest);
    }
}
