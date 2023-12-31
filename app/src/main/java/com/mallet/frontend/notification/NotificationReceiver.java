package com.mallet.frontend.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the NotificationWorker to perform background work
        NotificationWorker.scheduleReminderNotifications(context);
    }
}