package com.zhengsr.ariesui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhengsr.ariesuilib.wieght.BeizerPointView;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class TestView extends ViewGroup {
    private static final String TAG = "TestView";
    Handler handler = new Handler(Looper.getMainLooper());
    public TestView(Context context) {
        this(context,null);
    }
    
    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//创建一个新view
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
                windowParams.gravity = Gravity.LEFT | Gravity.TOP;
                windowParams.format = PixelFormat.TRANSLUCENT;
                windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
                windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                // layoutParams = new FrameLayout.LayoutParams(mWidth,mHeight);
                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                /*FrameLayout view = new FrameLayout(getContext());
                TextView textView = new TextView(getContext());
                textView.setText("测试");
                view.addView(textView);*/
                BeizerPointView view = new BeizerPointView(getContext());
                windowManager.addView(view,windowParams);
            }
        },3000);

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
