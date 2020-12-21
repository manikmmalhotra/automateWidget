package com.example.automatewidget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WidgetActivity extends AppCompatActivity {

    Button buttonAddWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        buttonAddWidget = findViewById(R.id.button_widget);

        getPermission();

        buttonAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.RED);
                if(!Settings.canDrawOverlays(WidgetActivity.this)){
                        getPermission();
                }else {
                        Intent intent = new Intent(WidgetActivity.this,WidgetService.class);
                        startService(intent);
                }
            }
        });
    }

    public void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){
            if (!Settings.canDrawOverlays(WidgetActivity.this)){
                Toast.makeText(this, "Permission Denied By User.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}