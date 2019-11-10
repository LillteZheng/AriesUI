package com.zhengsr.ariesuilib.wieght.point;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 模仿QQ的红点拖拽view,只是传递自定义属性
 */
public class BezierPointView extends AppCompatTextView {
    private static final String TAG = "BezierPointView";

    private float mDefaultCircleSize;
    private float mRecoveryBound;
    private float mDrawCircleSize;
    private int mDrawColor;
    private float mMaxMoveLength;
    private boolean mIsCanMove;
    private PointViewListener mListener;
    private boolean mUseDefaultAnim = true;
    private PointHelper mPointHelper;
    private int mAnimatorRes;
    private int mAnimationRes;

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
        mAnimatorRes = ta.getResourceId(R.styleable.BezierPointView_bez_animator, -1);
        mAnimationRes = ta.getResourceId(R.styleable.BezierPointView_bez_animation,-1);
        ta.recycle();
        if (mAnimationRes != -1 || mAnimatorRes != -1){
            mUseDefaultAnim = false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPointHelper = new PointHelper(this);
        setOnTouchListener(mPointHelper);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = AriesUtils.getDefaultSize(40,widthMeasureSpec);
        int height =AriesUtils.getDefaultSize(40,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    public float getDefaultCircleSize() {
        return mDefaultCircleSize;
    }

    public float getDrawCircleSize() {
        return mDrawCircleSize;
    }

    public int getDrawColor() {
        return mDrawColor;
    }

    public float getMaxMoveLength() {
        return mMaxMoveLength;
    }

    public float getRecoveryBound() {
        return mRecoveryBound;
    }

    public boolean isCanMove() {
        return mIsCanMove;
    }

    public boolean isUseDefaultAnim(){
        return mUseDefaultAnim;
    }
    public PointViewListener getListener(){
        return mListener;
    }

    public int getAnimatorRes(){
        return mAnimatorRes;
    }
    public int getAnimationRes(){
        return mAnimationRes;
    }

    public BezierPointView defaultRadius(float  defaultRadius){
        mDefaultCircleSize = defaultRadius;
        return this;
    }
    public BezierPointView drawRadius(float drawRadius){
        mDrawCircleSize = drawRadius;
        return this;
    }
    public BezierPointView drawColor(int drawColor){
        mDrawColor = drawColor;
        return this;
    }
    public BezierPointView maxLength(float maxLength){
        mMaxMoveLength = maxLength;
        return this;
    }
    public BezierPointView recoveryBound(float recoveryBound) {
        mRecoveryBound = recoveryBound;
        return this;
    }
    public BezierPointView isCanMove(boolean isCanMove){
        mIsCanMove = isCanMove;
        return this;
    }
    public BezierPointView listener(boolean useDefaultAnim,PointViewListener listener){
        mUseDefaultAnim = useDefaultAnim;
        mListener = listener;
        return this;
    }


    public void removeView(){
        if (mPointHelper != null) {
            mPointHelper.removeView();
        }
    }

    public interface PointViewListener{
        void destory(View view);
    }

    public void removeAnim(){
        mPointHelper.removeAnim();
    }

}
