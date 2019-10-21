package com.zhengsr.ariesuilib.select.colors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

public class ColorsGradient extends View {
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
    /**
     * logic
     */
    private Paint mPaint;
    private float mCx = -1,mCy = -1;
    private Paint mCirclePaint;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private ColorGradientListener mListener;
    private int mCurrentColor;
    private int mLastWidth;

    public ColorsGradient(Context context) {
       this(context,null);
   }

   public ColorsGradient(Context context, @Nullable AttributeSet attrs) {
       this(context, attrs,0);
   }

   public ColorsGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
       super(context, attrs, defStyleAttr);

       TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorsGradient);
       mDefaultColor = ta.getColor(R.styleable.ColorsGradient_gra_default_color,Color.YELLOW);
       mCircleRadius = ta.getDimensionPixelSize(R.styleable.ColorsGradient_gra_circle_radius,20);
       ta.recycle();

       setClickable(true);
       mCurrentColor = mDefaultColor;
   }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        if (mCx == -1) {
            mCx = w * 1.0f - mCircleRadius/2;
        }else{
            mCx = mCx * w / mLastWidth;
        }
        if (mCy == -1) {
            mCy = mCircleRadius/2;
        }
        Rect rect = new Rect(0,0 , w, h);
        LinearGradient black  = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom,
                Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
        LinearGradient gradient = new LinearGradient(rect.left, rect.top, rect.right, rect.top,
                Color.WHITE, mDefaultColor, Shader.TileMode.CLAMP);
        ComposeShader shader = new ComposeShader(black,gradient, PorterDuff.Mode.MULTIPLY);
        mPaint.setShader(shader);

        mBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawRect(rect,mPaint);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(2);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        if (mListener != null) {
            mListener.readyToShow(mCurrentColor);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);
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
     * @param event
     */
    private void getPointColor(MotionEvent event) {
        mCx = (int) event.getX();
        mCy = (int) event.getY();
        //边界限定
        if (mCx > mWidth){
            mCx = mWidth;
        }
        if (mCx < 0){
            mCx = 0;
        }
        if (mCy < 0){
            mCy = 0;
        }
        if (mCy > mHeight){
            mCy = mHeight;
        }
        int x = (int) (mCx * mBitmap.getWidth() *1.0f/mWidth);
        int y = (int) (mCy * mBitmap.getHeight() *1.0f/mHeight);

        try {
            if (mCx <= OFFSET && mCy <= OFFSET){
                mCurrentColor = Color.WHITE;
            }else if (mCx == mBitmap.getWidth() -OFFSET && mCy <= OFFSET){
                mCurrentColor = mDefaultColor;
            }else if (mCy >= mBitmap.getHeight() - OFFSET){
                mCurrentColor = Color.BLACK;
            }else {
                mCurrentColor = mBitmap.getPixel(x, y);
            }
            if (mListener != null){
                mListener.onGetColor(mCurrentColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
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
        state.putInt("radius",mCircleRadius);
        state.putFloat("cx",mCx);
        state.putFloat("cy",mCy);
        state.putInt("width",mBitmap.getWidth());
        return state;
    }

    public interface ColorGradientListener{
        void readyToShow(int color);
        void onGetColor(int color);
    }

    public ColorsGradient addListener(ColorGradientListener listener){
        mListener = listener;
        return this;
    }

    public ColorsGradient color(int color){
        mDefaultColor = color;
        mCurrentColor = mDefaultColor;
        invalidate();
        return this;
    }
    public ColorsGradient radiuds(int radiuds){
        mCircleRadius = radiuds;
        invalidate();
        return this;
    }

    public int  getColor(){
        return mCurrentColor;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = AriesUtils.measureWidth(widthMeasureSpec);
        mHeight = AriesUtils.measureWidth(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }



}
