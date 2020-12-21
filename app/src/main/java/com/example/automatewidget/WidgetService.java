package com.example.automatewidget;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class WidgetService extends Service {
    int LAYOUT_FLAG;
    View floatingView;
    private Timer myTimer;
    WindowManager windowManager;
    ImageView imageClose;
    TextView textView;
    String runnig ="";
    String currentApp = "";
    String name="";
    int height;
    WindowManager.LayoutParams layoutParams;
    int widht;
    int i =0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //stickyFun();


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
//            Log.e("manikk", "Current App in foreground is: " + currentApp);


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //do stuff like remove view etc
                    // MainActivity.adapter.notifyDataSetChanged();
                    Log.e("manikk", "Current App in foreground is: " + currentApp);

                        if (currentApp.contains("youtube")) {
                            Log.e("rohit", "Current App in foreground is: " + currentApp);
                            if (name != "youtube") {
                                if(floatingView!=null && floatingView.getVisibility()==View.VISIBLE)
                                {
                                    floatingView.setVisibility(View.GONE);
                                }
                                name = "youtube";
                                stickyFun();
                            }
                            Log.d("mnik",""+layoutParams.toString());

                        }
                    if (currentApp.contains("whatsapp")) {
                        Log.e("rohit", "Current App in foreground is: " + currentApp);
                        if (name != "whatsapp") {
                            if(floatingView!=null && floatingView.getVisibility()==View.VISIBLE)
                            {
                                floatingView.setVisibility(View.GONE);
                            }
                            name = "whatsapp";
                            stickyFun();
                        }
                        Log.d("mnik",""+layoutParams.toString());

                    }
                        if (currentApp.contains("launcher")) {
                            Log.e("rohit", "Current App in foreground is: " + currentApp);
                            name = "";
                            if(floatingView!=null && floatingView.getVisibility()==View.VISIBLE)
                            {
                                floatingView.setVisibility(View.GONE);
                            }
                        }

                }
            });

        }
        else {

            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            String mm= manager.getRunningTasks(1).get(0).topActivity.getPackageName();
            Log.e("manik", "Current App in foreground is: " + mm);
        }


    }


    private void stickyFun(){
        i++;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

            floatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget,null);

            layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            layoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
            layoutParams.x = 0;
            layoutParams.y = 100;



        WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140,140,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            imageParams.gravity = Gravity.BOTTOM|Gravity.CENTER;
            imageParams.y = 100;

            windowManager = (WindowManager) getSystemService((WINDOW_SERVICE));
            imageClose = new ImageView(this);
            imageClose.setImageResource(R.drawable.ic_baseline_close_24);
            imageClose.setVisibility(View.INVISIBLE);
            windowManager.addView(imageClose,imageParams);
            windowManager.addView(floatingView,layoutParams);
            floatingView.setVisibility(View.VISIBLE);

            height = windowManager.getDefaultDisplay().getHeight();
            widht = windowManager.getDefaultDisplay().getWidth();

            textView = floatingView.findViewById(R.id.text_widget);

            textView.setText(name);
            Log.d("rujj",runnig+"");


            textView.setOnTouchListener(new View.OnTouchListener() {
                int initialX,initialY;
                float initialTouchX,initialTouchY;
                long startClickTime;
                int MAX_CLICk_DURATION = 200;
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            startClickTime = Calendar.getInstance().getTimeInMillis();
                            imageClose.setVisibility(View.VISIBLE);
                            initialX = layoutParams.x;
                            initialY = layoutParams.y;

                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();

                            return true;
                        case MotionEvent.ACTION_UP:
                            long clickDuration = Calendar.getInstance().getTimeInMillis()-startClickTime;
                            imageClose.setVisibility(View.GONE);
                            layoutParams.x = initialX+(int)(initialTouchX-event.getRawX());
                            layoutParams.y = initialY+(int)(event.getRawY()-initialTouchY);
                            if (clickDuration<MAX_CLICk_DURATION){
                                Toast.makeText(WidgetService.this, "TIME : "+textView.getText(), Toast.LENGTH_SHORT).show();
                            }else {
                                if (layoutParams.y>(height*0.8)){
                                    //stopSelf();
                                    floatingView.setVisibility(View.GONE);
                                    i=0;
                                    return true;
                                }
                            }
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            layoutParams.x = initialX+(int)(initialTouchX-event.getRawX());
                            layoutParams.y = initialY+(int)(event.getRawY()-initialTouchY);

                            windowManager.updateViewLayout(floatingView,layoutParams);

                            return true;
                    }
                    return false;
                }
            });



    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();

        if (floatingView!=null){
            windowManager.removeView(floatingView);
        }

        if(imageClose!=null){
            windowManager.removeView(imageClose);
        }
    }
}
