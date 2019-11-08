package com.zhengsr.ariesuilib.wieght.point;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class AbsPointView extends View {
    private static final String TAG = "AbsPointView";
    protected float mDefaultCircleSize;
    protected float mDrawCircleSize;
    protected int mDrawColor;
    protected float mMaxMoveLength;
    protected float mRecoveryBound;
    protected boolean mIsCanMove;
    protected float mWidth,mHeight;
    public AbsPointView(Context context) {
        this(context,null);
        Log.d(TAG, "zsr - AbsPointView: ");
    }
    
    public AbsPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public AbsPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "zsr - AbsPointView: "+mDefaultCircleSize);
    }
}
