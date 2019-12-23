package com.zhengsr.ariesui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesuilib.wieght.FlowLayout;

public class FlowActivity extends AppCompatActivity {

    private FlowLayout mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mFlowLayout = findViewById(R.id.flowlayout);
    }

    public void add(View view) {
        Button button = new Button(this);
        ViewGroup.LayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setText("add");
        mFlowLayout.addView(button);
    }
}
