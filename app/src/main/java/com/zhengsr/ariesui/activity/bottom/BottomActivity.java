package com.zhengsr.ariesui.activity.bottom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhengsr.ariesui.R;

public class BottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
    }


    public void onfragment(View view) {
        startActivity(new Intent(this, FragmentsActivity.class));
    }
    public void onviewpager(View view) {
        startActivity(new Intent(this,ViewPagerActivity.class));
    }

}
