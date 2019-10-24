package com.zhengsr.ariesui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class AppTextView extends AppCompatTextView {
    private static final String TAG = "AppTextView";
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF bounds = new RectF();
    public AppTextView(Context context) {
        this(context,null);
    }

    public AppTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AppTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "zsr - AppTextView: ");
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制强调背景
        Layout layout = getLayout();
        bounds.left = layout.getLineLeft(1);
        bounds.right = layout.getLineRight(1);
        bounds.top = layout.getLineTop(1);
        bounds.bottom = layout.getLineBottom(1);
        Log.d(TAG, "zsr - onDraw: "+bounds);
        canvas.drawRect(bounds, paint);
       /* bounds.left = layout.getLineLeft(layout.getLineCount() - 4);
        bounds.right = layout.getLineRight(layout.getLineCount() - 4);
        bounds.top = layout.getLineTop(layout.getLineCount() - 4);
        bounds.bottom = layout.getLineBottom(layout.getLineCount() - 4);
        canvas.drawRect(bounds, paint);*/
    }
}
