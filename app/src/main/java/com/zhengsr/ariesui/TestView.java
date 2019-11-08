package com.zhengsr.ariesui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhengsr.ariesuilib.wieght.point.BezierPointView;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class TestView extends AppCompatTextView {
    private static final String TAG = "TestView";
    Handler handler = new Handler(Looper.getMainLooper());
    private BezierPointView view;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }




}
