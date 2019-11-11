package com.zhengsr.ariesui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesui.adapter.LGAdapter;
import com.zhengsr.ariesui.adapter.LGViewHolder;
import com.zhengsr.ariesuilib.wieght.point.BezierPointView;

import java.util.ArrayList;
import java.util.List;

import ezy.assist.compat.SettingsCompat;


public class BeizerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "BeizerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beizer);

        if (!SettingsCompat.canDrawOverlays(this)) {
            SettingsCompat.manageDrawOverlays(this);
        }

        ListView listView = findViewById(R.id.listview);
        List<Data> datas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Data data = new Data();
            data.text = "测试: " + i;
            data.num = i + "";
            datas.add(data);
        }

        TextAdapter adapter = new TextAdapter(datas, R.layout.item_text);
        listView.setAdapter(adapter);

        final BezierPointView pointView1 = findViewById(R.id.pointview);


        final BezierPointView pointView = findViewById(R.id.pointview2);
       /* pointView1.listener(true, new BezierPointView.PointViewListener() {
            @Override
            public void destory(View view) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
                animator.setDuration(1000);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        pointView1.removeView();
                    }
                });
                animator.start();
            }
        });*/


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointView1.setVisibility(View.VISIBLE);
                pointView.setVisibility(View.VISIBLE);
            }
        });

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击坐标: "+position, Toast.LENGTH_SHORT).show();
    }


    class Data {
        public String text;
        public String num;
    }

    class TextAdapter extends LGAdapter<Data> {

        public TextAdapter(List<Data> datas, int layoutid) {
            super(datas, layoutid);
        }

        @Override
        public void convert(LGViewHolder viewHolder, Data data, int position) {
            viewHolder.setText(R.id.item_text, data.text)
                    .setText(R.id.item_point, data.num);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
