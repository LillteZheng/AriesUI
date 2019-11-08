package com.zhengsr.ariesui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesui.adapter.LGAdapter;
import com.zhengsr.ariesui.adapter.LGViewHolder;

import java.util.ArrayList;
import java.util.List;

import ezy.assist.compat.SettingsCompat;

public class BeizerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beizer);
        ListView listView = findViewById(R.id.listview);
        List<Data> datas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Data data = new Data();
            data.text = "测试: "+i;
            datas.add(data);
        }

        TextAdapter adapter = new TextAdapter(datas,R.layout.item_text);
        listView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!SettingsCompat.canDrawOverlays(this)){
                SettingsCompat.manageDrawOverlays(this);
            }
        }
    }


    class Data{
        public String text;
    }

    class  TextAdapter extends LGAdapter<Data>{

        public TextAdapter(List<Data> datas, int layoutid) {
            super(datas, layoutid);
        }

        @Override
        public void convert(LGViewHolder viewHolder, Data data, int position) {
            viewHolder.setText(R.id.item_text,data.text);
        }
    }

}
