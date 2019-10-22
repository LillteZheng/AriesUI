package com.zhengsr.ariesuilib.select.colors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhengsr.ariesuilib.select.colors.callback.ColorSelectLiseter;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @auther by zhengshaorui on 2019/10/22
 * describe: 颜色条
 */
public class RectColors extends View {
    private static final String TAG = "RectColors";
    private Paint mPaint;
    private Rect mRect;
    private int mWidth, mHeight;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private RectF mRoundRect;
    private Paint mRoundPaint;
    private int mCurrentColor = -1;
    private ColorSelectLiseter mListener;
    private int[] mhueColor;
    private int mRoundSize = 4;
    private int mMove = 0;
    private boolean mIsVertical = true;
    private Path mPath;

    public RectColors(Context context) {
        this(context, null);
    }

    public RectColors(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectColors(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new Rect(0, 0, w, h);
        mhueColor = new int[361];
        int count = 0;
        for (int i = mhueColor.length - 1; i >= 0; i--, count++) {
            mhueColor[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        LinearGradient linearGradient;
        if (w < h) {
            linearGradient = new LinearGradient(mRect.left, mRect.top,
                    mRect.left, mRect.bottom, mhueColor, null, Shader.TileMode.CLAMP);
            mIsVertical = true;
        } else {
            mIsVertical = false;
            linearGradient = new LinearGradient(mRect.left, mRect.top,
                    mRect.right, mRect.top, mhueColor, null, Shader.TileMode.CLAMP);
        }
        mPaint = new Paint();
        mPaint.setShader(linearGradient);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawRect(mRect, mPaint);
        int offset = 2;
        mRoundPaint = new Paint();
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setColor(Color.WHITE);
       // mRoundPaint.setStyle(Paint.Style.STROKE);
        mRoundPaint.setStrokeWidth(2);
        mPath = new Path();
        if (mIsVertical) {
            mRoundRect = new RectF(mRect.left - offset, mRect.top, mRect.right + offset, mRoundSize);
           /* mPath.moveTo(5,0);
            mPath.lineTo(10,2);
            mPath.lineTo(15,0);
            mPath.close();*/
        } else {
            mRoundRect = new RectF(mRect.left , mRect.top- offset, mRoundSize, mRect.bottom + offset);
          /*  mPath.moveTo(5,0);
            mPath.lineTo(10,2);
            mPath.lineTo(15,0);
            mPath.close();*/
        }
        if (mListener != null) {
            if (mCurrentColor != -1) {
                mListener.readyToShow(mCurrentColor);
            } else {
                mListener.readyToShow(Color.RED);
            }
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.save();
        if (mIsVertical) {
            canvas.translate(0, mMove);
        }else{
            canvas.translate(mMove,0);
        }
        canvas.drawRoundRect(mRoundRect, 2, 2, mRoundPaint);
       // canvas.drawPath(mPath,mPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        if (mIsVertical) {
            if (y >= mRect.bottom - mRoundSize) {
                y = mRect.bottom - mRoundSize;
            }
            if (y <= mRect.top) {
                y = mRect.top;
            }
        } else {
            if (x <= mRect.left) {
                x = mRect.left;
            }
            if (x >= mRect.right - mRoundSize) {
                x = mRect.right - mRoundSize;
            }
        }
        if (mRect.contains(x, y)) {
            int colorIndex;
            if (mIsVertical) {
                mMove = y;
                colorIndex = (int) (y * 360 * 1.0f / mRect.height());
            } else {
                mMove = x;
                colorIndex = (int) (x * 360 * 1.0f / mRect.width());
            }

            invalidate();
            if (mhueColor.length >= colorIndex) {
                mCurrentColor = mhueColor[colorIndex];
                if (mListener != null) {
                    mListener.onGetColor(mCurrentColor);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public interface RectColorListener {
        void getColor(int color);
    }

    public RectColors addListener(ColorSelectLiseter listener) {
        mListener = listener;
        return this;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("InstanceState");
            mMove = bundle.getInt("dy");
        }
        invalidate();
        super.onRestoreInstanceState(state);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("InstanceState", super.onSaveInstanceState());
        bundle.putInt("dy", mMove);
        bundle.putInt("color", mCurrentColor);
        return bundle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = AriesUtils.measureWidth(widthMeasureSpec);
        mHeight = AriesUtils.measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
}
