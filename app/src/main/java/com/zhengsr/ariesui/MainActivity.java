package com.zhengsr.ariesui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhengsr.ariesuilib.select.colors.ColorsGradient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ColorsGradient gradient = findViewById(R.id.colors);

        final View view = findViewById(R.id.test);
        final TextView textView = findViewById(R.id.testview);
        gradient.addListener(new ColorsGradient.ColorGradientListener() {
            @Override
            public void readyToShow(int color) {
                view.setBackgroundColor(color);
                String msg = "#"+Integer.toHexString(color);
                textView.setText(msg);
            }

            @Override
            public void onGetColor(int color) {
                view.setBackgroundColor(color);
                textView.setText("#"+Integer.toHexString(color));
            }
        });

    }


}
