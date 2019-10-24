package com.zhengsr.ariesuilib.utils;

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


}
