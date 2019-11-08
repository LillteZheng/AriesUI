package com.zhengsr.ariesuilib.wieght.point;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 模仿QQ的红点拖拽view
 */
public class BezierPointView extends AbsPointView {
    private static final String TAG = "BezierPointView";

    public BezierPointView(Context context) {
        this(context, null);
    }

    public BezierPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BezierPointView);
        mDefaultCircleSize = ta.getDimensionPixelSize(R.styleable.BezierPointView_bez_default_circleSize,40);
        mDrawCircleSize = ta.getDimensionPixelSize(R.styleable.BezierPointView_bez_draw_circlecSize,40);
        mDrawColor = ta.getColor(R.styleable.BezierPointView_bez_draw_color, Color.RED);
        mMaxMoveLength = ta.getDimensionPixelSize(R.styleable.BezierPointView_bez_max_length,120);
        mIsCanMove = ta.getBoolean(R.styleable.BezierPointView_bez_isCanMove,true);
        mRecoveryBound = ta.getDimensionPixelSize(R.styleable.BezierPointView_bez_recovery_bound,10);
        ta.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setOnTouchListener(new PointHelper(this));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = AriesUtils.getDefaultSize(40,widthMeasureSpec);
        int height =AriesUtils.getDefaultSize(40,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }
}
