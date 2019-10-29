package com.zhengsr.ariesui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesuilib.wieght.ScanView;

public class ScanViewActivity extends AppCompatActivity {
    private ScanView mScanView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_view);
        mScanView = findViewById(R.id.scan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScanView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanView.stop();
    }
}
