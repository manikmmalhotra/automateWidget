package com.example.automatewidget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStat = findViewById(R.id.textStats);
        if (checkUsageStatsPermission()){
            showUsageStats();
        }
        else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

    }

    private void showUsageStats() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,cal.getTimeInMillis(),System.currentTimeMillis());
        String Stats_data = "";
        for(int i = 0; i <= queryUsageStats.size()-1; i++){
            Log.d("manik",queryUsageStats.get(i).toString());
            Stats_data = Stats_data + "Package Name : "+ queryUsageStats.get(i).getPackageName() + "\n" +
                                      "Last Time Used : "+ convertTime(queryUsageStats.get(i).getLastTimeUsed()) + "\n" +
                                      "Describe Content : "+ queryUsageStats.get(i).describeContents() + "\n" +
                                      "First Time Stamp : " + convertTime(queryUsageStats.get(i).getFirstTimeStamp()) + "\n" +
                                      "Last Time Stamp : " + convertTime(queryUsageStats.get(i).getLastTimeUsed()) + "\n" +
                                      "Total Time In Foreground : "+ convertTime2(queryUsageStats.get(i).getTotalTimeForegroundServiceUsed())+ "\n"+
                                      "IsActive : "+usageStatsManager.isAppInactive(queryUsageStats.get(i).getPackageName().toString()) +"\n\n";
        }
        textStat.setText(Stats_data);
    }

    private String convertTime(Long lastTimeUsed) {
        Date date = new Date(lastTimeUsed);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        return format.format(date);
    }

    private String convertTime2(Long lastTimeUsed) {
        Date date = new Date(lastTimeUsed);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        return format.format(date);
    }

    private Boolean checkUsageStatsPermission() {
        AppOpsManager appOpsManager = null;
        int mode = 0;
        appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

}