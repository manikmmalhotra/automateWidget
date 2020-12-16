package com.example.automatewidget;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MyService extends Service {
    private Timer myTimer;
    public static List<String> activeApp = new ArrayList<>();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {

               retriveNewApp();

            }
        },0,1000);

        return START_STICKY;
    }

    private void retriveNewApp() {
        if (Build.VERSION.SDK_INT >= 21) {
            String currentApp = "";
            UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> applist = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (applist != null && applist.size() > 0) {

                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : applist) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
            Log.e("manikk", "Current App in foreground is: " + currentApp);



                activeApp.add(currentApp.toString());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //do stuff like remove view etc
                    MainActivity.adapter.notifyDataSetChanged();
                }
            });
        }
        else {

            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String mm= manager.getRunningTasks(1).get(0).topActivity.getPackageName();
            Log.e("manik", "Current App in foreground is: " + mm);
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
