package com.zhengsr.ariesuilib.wieght.colors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;
/**
 * @auther by zhengshaorui on 2019/10/22
 * describe: 颜色渐变
 */
public class ColorGradient extends View {
    private static final String TAG = "ColorsGradient";
    /**
     * static
     */
    private static final int OFFSET = 2;
    /**
     * attrs
     */
    private int mDefaultColor;
    private int mCircleRadius;
    private int mBgRadius;
    /**
     * logic
     */
    private Paint mPaint;
    private float mCx = -1, mCy = -1;
    private Paint mCirclePaint;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ColorSelectLiseter mListener;
    private int mCurrentColor;
    private int mLastWidth;
    private Rect mRect;
    private LinearGradient mDefaultGradient;
    private LinearGradient mChangeGradient;
    private Paint mBitmapPaint;
    private RectF mBitmapRect;

    public ColorGradient(Context context) {
        this(context, null);
    }

    public ColorGradient(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorGradient);
        mDefaultColor = ta.getColor(R.styleable.ColorGradient_gra_default_color, Color.YELLOW);
        mCircleRadius = ta.getDimensionPixelSize(R.styleable.ColorGradient_gra_circle_radius, 20);
        int circleColor = ta.getColor(R.styleable.ColorGradient_gra_circle_color,Color.WHITE);
        mBgRadius = ta.getDimensionPixelSize(R.styleable.ColorGradient_gra_bg_radius,0);
        ta.recycle();

        setClickable(true);
        mCurrentColor = mDefaultColor;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mBitmapRect = new RectF();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new Rect(0, 0, w, h);
        if (mCx == -1) {
            mCx = mRect.right - 2 * mCircleRadius;
        } else {
            mCx = mCx * w / mLastWidth;
        }
        if (mCy == -1) {
            mCy = mRect.top + 2 * mCircleRadius;
        }
        mDefaultGradient = new LinearGradient(mRect.left, mRect.top, mRect.left, mRect.bottom,
                Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);

        mChangeGradient = new LinearGradient(mRect.left, mRect.top, mRect.right, mRect.top,
                Color.WHITE, mDefaultColor, Shader.TileMode.CLAMP);
        ComposeShader shader = new ComposeShader(mDefaultGradient, mChangeGradient, PorterDuff.Mode.MULTIPLY);
        mPaint.setShader(shader);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawRect(mRect, mPaint);


        if (mListener != null) {
            mListener.readyToShow(mCurrentColor);
        }
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        mBitmapRect.set(0,0,mWidth,mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.drawRoundRect(mBitmapRect, mBgRadius, mBgRadius, mBitmapPaint);
        canvas.restore();
        //画小球
        canvas.drawCircle(mCx, mCy, mCircleRadius, mCirclePaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        getPointColor(event);

        return super.onTouchEvent(event);
    }


    /**
     * 拿到某个像素点的颜色值
     *
     * @param event
     */
    private void getPointColor(MotionEvent event) {
        mCx = (int) event.getX();
        mCy = (int) event.getY();
        //边界限定
        if (mCx >= mRect.right) {
            mCx = mRect.right;
        }
        if (mCx <= mRect.left) {
            mCx = mRect.left;
        }
        if (mCy <= mRect.top) {
            mCy = mRect.top;
        }
        if (mCy >= mRect.bottom) {
            mCy = mRect.bottom;
        }
        int x = (int) mCx;
        int y = (int) mCy;
        if (mRect.contains(x,y)){
            showColor(x,y);
        }
        invalidate();
    }

    /**
     * 显示颜色
     * @param x
     * @param y
     */
    private void showColor(int x, int y) {
        if (mCx <= OFFSET && mCy <= OFFSET){
            mCurrentColor = Color.WHITE;
        }else if (mCx >= mRect.right - 10 * OFFSET && mCy <= 10 *OFFSET){
            mCurrentColor = mDefaultColor;
        }else if (mCy >= mRect.bottom - 2 * OFFSET){
            mCurrentColor = Color.BLACK;
        }else {
            mCurrentColor = mBitmap.getPixel(x, y);
        }
        if (mListener != null) {
            mListener.onGetColor(mCurrentColor);
        }
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentColor = bundle.getInt("color");
            mCircleRadius = bundle.getInt("radius");
            mLastWidth = bundle.getInt("width");
            mCx = bundle.getFloat("cx");
            mCy = bundle.getFloat("cy");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable("instanceState", super.onSaveInstanceState());
        state.putInt("color", mCurrentColor);
        state.putInt("radius", mCircleRadius);
        state.putFloat("cx", mCx);
        state.putFloat("cy", mCy);
        state.putInt("width", mBitmap.getWidth());
        return state;
    }


    public ColorGradient addListener(ColorSelectLiseter listener) {
        mListener = listener;
        return this;
    }

    public ColorGradient color(int color) {
        mDefaultColor = color;
        mCurrentColor = mDefaultColor;
        if (mPaint != null){
            mChangeGradient = new LinearGradient(mRect.left, mRect.top, mRect.right, mRect.top,
                    Color.WHITE, mDefaultColor, Shader.TileMode.CLAMP);
            mPaint.setShader(new ComposeShader(mDefaultGradient,mChangeGradient, PorterDuff.Mode.MULTIPLY));
            mCanvas.drawRect(mRect,mPaint);
        }
        return this;
    }

    public ColorGradient radiuds(int radiuds) {
        mCircleRadius = radiuds;
        return this;
    }
    public ColorGradient circleColor(int circleColor){
        mCirclePaint.setColor(circleColor);
        return this;
    }

    public void go(){
        invalidate();
    }

    public int getColor() {
        return mCurrentColor;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = AriesUtils.getDefaultSize(100,widthMeasureSpec);
        mHeight = AriesUtils.getDefaultSize(100,heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }


}
