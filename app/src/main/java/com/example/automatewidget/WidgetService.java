package com.example.automatewidget;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.Build;
import android.os.IBinder;
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

import java.util.Calendar;

public class WidgetService extends Service {
    int LAYOUT_FLAG;
    View floatingView;
    WindowManager windowManager;
    ImageView imageClose;
    TextView textView;
    int height;
    int widht;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget,null);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
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
                            if (layoutParams.y>(height*0.6)){
                                stopSelf();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                         layoutParams.x = initialX+(int)(initialTouchY-event.getRawX());
                        layoutParams.y = initialY+(int)(event.getRawY()-initialTouchY);

                        windowManager.updateViewLayout(floatingView,layoutParams);

                        return true;
                }
                return false;
            }
        });


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView!=null){
            windowManager.removeView(floatingView);
        }

        if(imageClose!=null){
            windowManager.removeView(imageClose);
        }
    }
}
