package com.zhengsr.ariesui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class TestView extends ViewGroup {
    private static final String TAG = "TestView";
    public TestView(Context context) {
        this(context,null);
    }
    
    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
      //  Log.d(TAG, "zsr - dispatchDraw: ");
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
       // canvas.drawCircle(100,100,40,paint);
        Rect rectF = new Rect(0, 38, 56, 38);
        canvas.drawRect(rectF,paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // Log.d(TAG, "zsr - onDraw: ");
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        canvas.drawCircle(100,100,40,paint);
    }
}
