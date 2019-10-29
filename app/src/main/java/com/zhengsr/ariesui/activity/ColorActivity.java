package com.zhengsr.ariesui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesuilib.wieght.colors.ColorGradient;
import com.zhengsr.ariesuilib.wieght.colors.ColorSelectLiseter;
import com.zhengsr.ariesuilib.wieght.colors.MultiColors;

public class ColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        final View view = findViewById(R.id.view);
        final ColorGradient gradient = findViewById(R.id.colorgradient);
        gradient.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {

            }

            @Override
            public void onGetColor(int color) {
                view.setBackgroundColor(color);
            }
        });

        MultiColors colors = findViewById(R.id.multicolor);
        colors.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {
                gradient.color(color);
                view.setBackgroundColor(color);
            }

            @Override
            public void onGetColor(int color) {
                gradient.color(color);
                view.setBackgroundColor(color);
            }
        });

        MultiColors colors2 = findViewById(R.id.multicolor2);
        colors2.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {
               // gradient.color(color);
               // view.setBackgroundColor(color);
            }

            @Override
            public void onGetColor(int color) {
                gradient.color(color);
                view.setBackgroundColor(color);
            }
        });
    }
}
