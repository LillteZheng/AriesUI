package com.zhengsr.ariesui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhengsr.ariesui.activity.BeizerActivity;
import com.zhengsr.ariesui.activity.ColorActivity;
import com.zhengsr.ariesui.activity.ScanViewActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    public void colorclick(View view) {
        startActivity(new Intent(this, ColorActivity.class));
    }

    public void scanview(View view) {
        startActivity(new Intent(this, ScanViewActivity.class));
    }

    public void beizerPoint(View view) {
        startActivity(new Intent(this, BeizerActivity.class));
    }
}
