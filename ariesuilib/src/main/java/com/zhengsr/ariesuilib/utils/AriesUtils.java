package com.zhengsr.ariesuilib.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 一些自定义view 的工具类
 */
public class AriesUtils {

    /**
     * 拿到view大小
     * @param defaultSize 默认大小
     * @param measureSpec measureSpec
     * @return
     */
    public static int getDefaultSize(int defaultSize,int measureSpec){
        int result ;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == View.MeasureSpec.UNSPECIFIED){
            result = defaultSize;
        }else{
            result = specSize;
        }
        return result;
    }

    /**
     * 绘制文字
     * @param msg
     * @param paint
     * @param rect
     * @param canvas
     */
    public static void drawText(String msg, Paint paint, RectF rect, Canvas canvas){
        float width = paint.measureText(msg);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float x = (rect.width() - width)/2+rect.left;
        float y = rect.top+rect.height()/2 - (metrics.descent+metrics.ascent)/2;
        canvas.drawText(msg,x,y,paint);
    }


}
