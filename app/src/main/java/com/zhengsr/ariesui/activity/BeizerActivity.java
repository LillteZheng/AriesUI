package com.zhengsr.ariesui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesui.adapter.LGAdapter;
import com.zhengsr.ariesui.adapter.LGViewHolder;
import com.zhengsr.ariesuilib.wieght.point.BezierPointView;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;


public class BeizerActivity extends AppCompatActivity {
    private static final String TAG = "BeizerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beizer);
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

        final BezierPointView view = findViewById(R.id.pointview);


        final BezierPointView pointView = findViewById(R.id.pointview2);
        pointView.listener(false, new BezierPointView.PointViewListener() {
            @Override
            public void destory(View view) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
                animator.setDuration(1000);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        pointView.removeView();
                    }
                });
                animator.start();
            }
        });


        final ImageView imageView = findViewById(R.id.image);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_alpha);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.VISIBLE);
                pointView.setVisibility(View.VISIBLE);
                imageView.startAnimation(animation);
            }
        });


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
