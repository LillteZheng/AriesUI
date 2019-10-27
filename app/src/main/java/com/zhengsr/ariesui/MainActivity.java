package com.zhengsr.ariesui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhengsr.ariesuilib.select.colors.ColorsSelectView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FrameLayout frameLayout = findViewById(R.id.scan_ly);
        int count = frameLayout.getChildCount();
        View[] Views = new View[4];
        for (int i = 0; i < count; i++) {
            ImageView imageView = (ImageView) frameLayout.getChildAt(i);
            Views[i] = imageView;
        }
        List<Animation> animations = Arrays.asList(
                AnimationUtils.loadAnimation(this,R.anim.scale_alpha_anim),
                AnimationUtils.loadAnimation(this,R.anim.scale_alpha_anim),
                AnimationUtils.loadAnimation(this,R.anim.scale_alpha_anim),
                AnimationUtils.loadAnimation(this,R.anim.scale_alpha_anim)
                );
        Views[0].startAnimation(animations.get(0));
        animations.get(1).setStartOffset(600);
        Views[1].startAnimation(animations.get(1));
        animations.get(2).setStartOffset(1200);
        Views[1].startAnimation(animations.get(2));
        animations.get(3).setStartOffset(1800);
        Views[1].startAnimation(animations.get(3));


    }

    private String mCurrentColor;
    public void colorclick(View view) {
        CusDialog dialog = new CusDialog.Builder()
                .setContext(this)
                .setLayoutId(R.layout.colorselector)
                .setWidth(getResources().getDimensionPixelSize(R.dimen.dialog_width))
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOutSideDimiss(true)
                .builder();
        dialog.setDismissByid(R.id.cancel);
        final ColorsSelectView colorsSelectView = dialog.getViewbyId(R.id.colorselector);
        final String[] colorMsg = new String[1];
       /* colorsSelectView.circleColor(Color.WHITE)
                .type(ColorsSelectView.TRI)
                .typeColor(Color.WHITE)
                .triSize(10)
                .triOritation(ColorsSelectView.TOP)
                .go();*/
       colorsSelectView.circleColor(Color.WHITE)
               .go();
        dialog.setOnClickListener(R.id.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = colorsSelectView.getColor();
                String msg = "#"+Integer.toHexString(color).toUpperCase();
                Toast.makeText(MainActivity.this, "选中颜色: "+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
