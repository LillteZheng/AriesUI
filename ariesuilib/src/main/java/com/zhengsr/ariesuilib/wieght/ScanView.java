package com.zhengsr.ariesuilib.wieght;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 类似于雷达扫描的图
 */
public class ScanView extends View {
    private static final String TAG = "ScanView";
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mOffset;
    private int mRadius;
    private int mWidth ,mHeight;
    private RectF mRect;
    private SweepGradient mGradient;
    private Paint mArcPaint;
    private int mScanColor;
    private Paint mDashPaint;
    private float mRotate;
    private Paint mRandonPaint;
    private boolean isRanDownAanmDone = true;

    public ScanView(Context context) {
        this(context,null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1.5f);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.GRAY);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScanView);
        mScanColor = ta.getColor(R.styleable.ScanView_scan_color,Color.WHITE);
        ta.recycle();

        mDashPaint = new Paint();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setColor(Color.GRAY);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{5,5},5));
        mRandonPaint = new Paint();
        mRandonPaint.setAntiAlias(true);
        mRandonPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        //绘制圆
        mRadius = w/2;
        mOffset = (int) (mRadius * 1.0f / 4);
        mRect = new RectF(0,0,w,h);
        int[] colors = {

                Color.TRANSPARENT, changeAlpha(mScanColor, 0), changeAlpha(mScanColor, 168),
                changeAlpha(mScanColor, 255), changeAlpha(mScanColor, 255)
        };
        //mGradient = new SweepGradient(mRadius,mRadius,colors,null);
        mGradient = new SweepGradient(mRadius, mRadius,
              colors, new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});

        mArcPaint.setShader(mGradient);

        ValueAnimator animator = ValueAnimator.ofFloat(0,360);
        animator.setDuration(5000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotate = (float) animation.getAnimatedValue();
                invalidate();

            }
        });
        animator.start();


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
        //绘制圆和圆点
        for (int i = 0; i < 4; i++) {
            if (i != 0){
                mPaint.setStrokeWidth(0.56f);
            }else{
                mPaint.setStrokeWidth(1.5f);
            }
            canvas.drawCircle(mRadius ,mRadius ,mRadius - mOffset * i,mPaint);
        }
        //绘制虚线
        drawDashLine(canvas);

        canvas.save();
        //旋转
        canvas.rotate(mRotate,mRadius,mRadius);
        //画渐变扇形
        canvas.drawCircle(mRadius,mRadius,mRadius,mArcPaint);
        //绘制一条线
       // mPaint.setStrokeWidth(1.5f);

       // canvas.drawLine(mRadius,mRadius,mWidth,mRadius,mPaint);
        canvas.restore();

        drawRandonCircle(canvas);
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
    private void drawRandonCircle(Canvas canvas){
        //让小圆点随机出现
        if (isRanDownAanmDone) {
            isRanDownAanmDone = false;
            int radius = 10;
            int count = 0;
            while (count < 4) {
                int x = (int) (Math.random() * (mWidth - radius));
                int y = (int) (Math.random() * (mHeight - radius));
                if (isInCircle(x, y, radius)) {
                    count++;
                    Log.d(TAG, "zsr - drawRandonCircle: "+count);
                    canvas.drawCircle(x, y, radius, mRandonPaint);
                }
            }

        }

    }

    private boolean isInCircle(int x,int y,int radius){
        return (mRadius-radius) > Math.sqrt((mRadius - x) * (mRadius - x) + (mRadius - y)*(mRadius - y));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = AriesUtils.getDefaultSize(200,widthMeasureSpec);
        mHeight = AriesUtils.getDefaultSize(200,heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
}
