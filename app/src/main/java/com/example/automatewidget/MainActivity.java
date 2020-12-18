package com.example.automatewidget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textStat;
    public static ArrayAdapter<String> adapter;
    private ListView listView;
    private Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.buttonStart);
        stop = (Button) findViewById(R.id.buttonStop);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MyService.activeApp);
        listView.setAdapter(adapter);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
       // startForegroundService(new Intent(this, MyService.class));
       // startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//
    }

    @Override
    public void onClick(View view) {
        if (view == start) {
            startService(new Intent(this, MyService.class));
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else if (view == stop) {
            stopService(new Intent(this, MyService.class));
        }
    }
}