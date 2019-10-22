package com.zhengsr.ariesui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class ColorsView extends View {

    private Paint paint;
    private Rect rect;

    public ColorsView(Context context) {
        this(context,null);
    }

    public ColorsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new Rect(0,0,w,h);
        int[] hue = new int[361];
        int count = 0;
        for (int i = hue.length - 1; i >= 0; i--,count++) {
            hue[count] = Color.HSVToColor(new float[]{i,1f,1f});
        }
        LinearGradient linearGradient = new LinearGradient(rect.left, rect.top,
                rect.left, rect.bottom,hue,null, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setShader(linearGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect,paint);
    }
}
