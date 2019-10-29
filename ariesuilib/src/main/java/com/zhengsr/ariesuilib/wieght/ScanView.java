package com.zhengsr.ariesuilib.wieght;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 类似于雷达扫描的图
 */
public class ScanView extends View {
    private static final String TAG = "ScanView";
    private int mOffset;
    private int mRadius;
    private int mWidth ,mHeight;
    private SweepGradient mSweepShader;
    private float mRotate;

    private List<RandomCircle> mCircles = new ArrayList<>();
    private ValueAnimator mSweepAnim;

    private Paint mArcPaint;
    private Paint mDashPaint;
    private Paint mPaint;

    /**
     * attrs
     */
    private int mSweepTime ;
    private int mSweepColor;
    private int mRandomNum;
    private int mRandomColor;
    private int mCircleNum;
    private boolean mIsShowRandom;
    private boolean mIsShowDash;
    private boolean mIsAnimAuto;
    private int mCircleColor;
    private int mDashColor;

    public ScanView(Context context) {
        this(context,null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScanView);
        mSweepColor = ta.getColor(R.styleable.ScanView_scan_sweep_color,Color.WHITE);
        mSweepTime = ta.getInteger(R.styleable.ScanView_scan_sweep_time,4000);
        mRandomNum = ta.getInteger(R.styleable.ScanView_scan_random_num,4);
        mRandomColor = ta.getColor(R.styleable.ScanView_scan_random_color,Color.WHITE);
        mCircleColor = ta.getColor(R.styleable.ScanView_scan_circle_color,Color.WHITE);
        mCircleNum = ta.getInteger(R.styleable.ScanView_scan_circle_num,4);
        mIsShowRandom = ta.getBoolean(R.styleable.ScanView_scan_show_random,true);
        mIsShowDash = ta.getBoolean(R.styleable.ScanView_scan_show_dash,true);
        mIsAnimAuto = ta.getBoolean(R.styleable.ScanView_scan_anim_auto,true);
        mDashColor = ta.getColor(R.styleable.ScanView_scan_dash_color,Color.GRAY);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mCircleColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1.5f);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);

        mDashPaint = new Paint();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setColor(mDashColor);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{5,5},5));


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //绘制圆
        mRadius = w/2;
        mOffset = (int) (mRadius * 1.0f / mCircleNum);

        //增加扫描弧度
        int[] colors = {

                Color.TRANSPARENT, changeAlpha(mSweepColor, 0), changeAlpha(mSweepColor, 168),
                changeAlpha(mSweepColor, 255), changeAlpha(mSweepColor, 255)
        };
        mSweepShader = new SweepGradient(mRadius, mRadius,
                colors, new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});

        mArcPaint.setShader(mSweepShader);

        mSweepAnim = ValueAnimator.ofFloat(0,360);
        mSweepAnim.setDuration(mSweepTime);
        mSweepAnim.setRepeatCount(-1);
        mSweepAnim.setInterpolator(new LinearInterpolator());
        mSweepAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotate = (float) animation.getAnimatedValue();
            }
        });
        //自动启动动画
        if (mIsAnimAuto && !mSweepAnim.isRunning()){
            mSweepAnim.start();
        }


    }

    /**
     * 随机生成小球
     */
    private void generaateRandomCircle() {
        //生成随机小球
        int radius = 2;
        if (mCircles.size() < mRandomNum){
            int x = (int) (Math.random() * (mWidth - 20 *radius));
            int y = (int) (Math.random() * (mHeight - 20 *radius));
            if (isInCircle(x, y, radius)) {
                //时间差
                boolean b = (int) (Math.random() * 15) == 0;
                if (b) {
                    RandomCircle circle = new RandomCircle();
                    circle.radius = radius;
                    circle.cx = x;
                    circle.cy = y;
                    mCircles.add(circle);
                }
            }
        }
    }

    /**
     * 改变颜色的透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    private static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制波纹
       // drawRipple(canvas);

        //绘制圆和圆点
        for (int i = 0; i < mCircleNum; i++) {
            if (i != 0){
                mPaint.setStrokeWidth(0.8f);
            }else{
                mPaint.setStrokeWidth(1.5f);

            }
            canvas.drawCircle(mRadius ,mRadius ,mRadius - mOffset * i,mPaint);
        }
        //绘制虚线
        if (mIsShowDash) {
            drawDashLine(canvas);
        }

        canvas.save();
        //旋转
        canvas.rotate(mRotate, mRadius, mRadius);
        //画渐变扇形
        canvas.drawCircle(mRadius, mRadius, mRadius, mArcPaint);
        canvas.restore();
        //画圆点
        if (mIsShowRandom) {
            drawRandomCircle(canvas);
        }
        invalidate();



    }


    /**
     * 随机生成小球
     * @param canvas
     */
    private void drawRandomCircle(Canvas canvas) {
        generaateRandomCircle();
        for (RandomCircle circle : mCircles) {
            circle.paint.setAlpha(circle.realAlpha);
            canvas.drawCircle(circle.cx,circle.cy,circle.radius,circle.paint);
            circle.radius += 0.1;
            circle.alpha -=  1.48f;
            circle.realAlpha = Math.round(circle.alpha);
            if (circle.alpha <= 0){
                circle.alpha = 0;
            }
        }
        removeCircle();
    }

    /**
     * 根据条件移除小球
     */
    private void removeCircle() {
        Iterator<RandomCircle> iterator = mCircles.iterator();
        while (iterator.hasNext()){
            RandomCircle circle = iterator.next();
            if (circle.radius >= 15 && circle.alpha <= 0){
                iterator.remove();
            }
        }

    }


    /**
     * 画虚线
     * @param canvas
     */
    private void drawDashLine(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(0,mHeight/2,mWidth,mHeight/2,mDashPaint);
            canvas.rotate(45,mRadius,mRadius);
        }
        canvas.restore();

    }

    /**
     * 是否在小球范围内
     * @param x
     * @param y
     * @param radius
     * @return
     */
    private boolean isInCircle(int x,int y,int radius){
        return (mRadius- 15 * radius) > Math.sqrt((mRadius - x) * (mRadius - x) + (mRadius - y)*(mRadius - y));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = AriesUtils.getDefaultSize(200,widthMeasureSpec);
        mHeight = AriesUtils.getDefaultSize(200,heightMeasureSpec);
        int size = Math.min(mWidth,mHeight);
        //保证正方形
        mWidth = mHeight = size;
        setMeasuredDimension(mWidth, mHeight);
    }


    /**
     * 一个生成圆点的类
     */
    class RandomCircle {
        public float alpha = 200;
        public int realAlpha = 200;
        public float radius = 2;
        public float cx,cy;
        public Paint paint;

        public RandomCircle() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setAlpha(realAlpha);
            paint.setColor(mRandomColor);
        }

    }

    public ScanView showDash(boolean showDash){
        mIsShowDash = showDash;
        return this;
    }
    public ScanView showRandomCircle(boolean showRandomCircle){
        mIsShowRandom = showRandomCircle;
        return this;
    }
    public ScanView autoAnim(boolean autoAnim){
        mIsAnimAuto = autoAnim;
        return this;
    }

    public ScanView circleColor(int circlecolor){
        mCircleColor = circlecolor;
        return this;
    }
    public ScanView dashColor(int dashColor){
        mDashColor = dashColor;
        return this;
    }
    public ScanView randomColor(int randomColor){
        mRandomColor = randomColor;
        return this;
    }
    public ScanView randomNum(int randomNum){
        mRandomNum = randomNum;
        return this;
    }
    public ScanView circleNum(int circleNum){
        mCircleNum = circleNum;
        return this;
    }
    public ScanView sweepTime(int sweepTime){
        mSweepTime = sweepTime;
        return this;
    }

    public ScanView sweepColor(int sweepColor){
        mSweepColor = sweepColor;
        return this;
    }
    public ScanView go(){
        invalidate();
        return this;
    }



    /**
     * 开始动画
     */
    public void start(){

        if (mSweepAnim != null) {
            mSweepAnim.start();
        }
    }

    /**
     * 结束动画
     */
    public void stop(){

        if (mSweepAnim != null) {
            mSweepAnim.cancel();;
            mSweepAnim = null;
        }
    }
}
