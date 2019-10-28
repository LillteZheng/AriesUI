package com.zhengsr.ariesuilib.wieght;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zhengsr.ariesuilib.R;

/**
 * @auther by zhengshaorui on 2019/10/28
 * describe:
 */
public class ScanView2 extends FrameLayout {
    public ScanView2(Context context) {
        this(context,null);
    }
    
    public ScanView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public ScanView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.scan_circle_layout,this,false);
        addView(view);

    }
}
