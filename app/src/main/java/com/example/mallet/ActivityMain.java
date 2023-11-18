package com.example.mallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mallet.databinding.ActivityMainBinding;
import com.example.mallet.databinding.DialogCreateBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import java.util.Calendar;
import java.util.Objects;


public class ActivityMain extends AppCompatActivity {
    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";
    private ActivityMainBinding binding;
    private BottomNavigationMenuView bottomNavMenu;
    private int selectedFragmentId = R.id.bottom_nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Utils.terminateApp(thisActivity());
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(ActivityMain.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


        initializeSelectedFragment(savedInstanceState);
        setExceptionItemColor();

        setupContents();

        notificationChannel();
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(this, BroadcastReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        setNotificationAlarm(12*60*60*1000);
    }

    private void initializeSelectedFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home);
            setSelectedFragment(selectedFragmentId);
        } else {
            replaceFragment(new FragmentHome());
        }
    }

    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor() {
        BottomNavigationMenuView bottomNavMenu = (BottomNavigationMenuView) binding.mainBottomNavigation.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) bottomNavMenu.getChildAt(2);
        int color = ContextCompat.getColor(this, R.color.downriver_blue_300);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

    private void setupContents() {
        binding.mainBottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private void setSelectedFragment(int fragmentId) {
        binding.mainBottomNavigation.setSelectedItemId(fragmentId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentId);
        super.onSaveInstanceState(outState);
    }

    private void postponeReplaceFragment(final Fragment fragment, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(fragment);
            }
        }, delayMillis);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFl, fragment);
        fragmentTransaction.commit();
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        selectedFragmentId = item.getItemId();

        if (selectedFragmentId == R.id.bottom_nav_home) {
            replaceFragment(new FragmentHome());
        } else if (selectedFragmentId == R.id.bottom_nav_library) {
            replaceFragment(new FragmentDatabase());
        } else if (selectedFragmentId == R.id.bottom_nav_add_new) {
            createNewSetFolderGroupDialog();
        } else if (selectedFragmentId == R.id.bottom_nav_your_library) {
            replaceFragment(new FragmentUserLibrary());
        } else if (selectedFragmentId == R.id.bottom_nav_profile) {
            replaceFragment(new FragmentProfile());
        }
        return true;
    }


    private void createNewSetFolderGroupDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_create, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogCreateBinding dialogBinding = DialogCreateBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView createSetTv = dialogBinding.createSetTv;
        TextView createGroupTv = dialogBinding.createGroupTv;
        TextView cancelTv = dialogBinding.createCancelTv;

        createSetTv.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityEditLearningSet.class);

            //intent.putExtra("isSetNew", true);

            startActivity(intent);
        });

        createGroupTv.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityCreateGroup.class);
            startActivity(intent);
        });

        cancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private Activity thisActivity() {
        Activity thisActivity = this;
        return thisActivity;
    }

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    private void notificationChannel() {
        CharSequence name = "Notification name";
        String description = "Notification";

        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel("Notification", name, importance);

        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void setNotificationAlarm(long interval) {
        long triggerAtMillis = System.currentTimeMillis() + interval;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);


        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    public void cancelNotificationAlarm() {
        alarmManager.cancel(pendingIntent);
    }

}