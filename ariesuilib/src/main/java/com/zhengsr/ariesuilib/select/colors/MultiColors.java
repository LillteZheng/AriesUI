package com.zhengsr.ariesuilib.select.colors;

import android.content.Context;
import android.content.res.TypedArray;
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

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.select.colors.callback.ColorSelectLiseter;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @auther by zhengshaorui on 2019/10/22
 * describe: 颜色条
 */
public class MultiColors extends View {
    private static final String TAG = "RectColors";
    /**
     * static
     */
    public static final int RECT = 1;
    public static final int TRI = 2;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;
    /**
     * logic
     */
    private Paint mPaint;
    private Rect mRect;
    private int mWidth, mHeight;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private RectF mRoundRect;
    private Paint mTypePaint;
    private int mCurrentColor = -1;
    private ColorSelectLiseter mListener;
    private int[] mhueColor;
    private int mRoundSize = 4;
    private int mMove = 0;
    private boolean mIsVertical = true;
    private Path mPath;
    private Path mLinePath;
    private Paint mLinePaint;

    /**
     * attrs
     */
    private int mType;
    private int mTriOritation;
    private int mTriSize;
    private boolean mShowTriLine;


    public MultiColors(Context context) {
        this(context, null);
    }

    public MultiColors(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiColors(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiColors);
        mType = ta.getInteger(R.styleable.MultiColors_mul_type,RECT);
        int typeColor = ta.getColor(R.styleable.MultiColors_mul_type_color,Color.WHITE);
        mTriOritation = ta.getInteger(R.styleable.MultiColors_mul_tri_oritation, LEFT);
        mTriSize = ta.getDimensionPixelSize(R.styleable.MultiColors_mul_tri_size,14);
        mShowTriLine = ta.getBoolean(R.styleable.MultiColors_mul_tri_show_line,false);
        ta.recycle();

        mPaint = new Paint();

        mTypePaint = new Paint();
        mTypePaint.setAntiAlias(true);
        mTypePaint.setColor(typeColor);
        mTypePaint.setStrokeWidth(3);
        if (mType == RECT){
            mTypePaint.setStyle(Paint.Style.STROKE);
        }

        mLinePath = new Path();
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(typeColor);

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

        mPaint.setShader(linearGradient);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawRect(mRect, mPaint);
        int offset = 2;
        int var1 = (int) (mTriSize * 1.0f / 2);
        mPath = new Path();
        if (mIsVertical) {
            if (mType == RECT) {
                mRoundRect = new RectF(mRect.left - offset, mRect.top, mRect.right + offset, mRoundSize);
            }else {
                if (mTriOritation == LEFT) {
                    mPath.moveTo( -4,-var1);
                    mPath.lineTo(mTriSize, 0);
                    mPath.lineTo(-4,var1);

                }else{
                    mPath.moveTo(mRect.right+4, -var1);
                    mPath.lineTo(mRect.right - mTriSize,0);
                    mPath.lineTo(mRect.right+4, var1);
                }
                if (mShowTriLine) {
                    mLinePath.moveTo(0, 0);
                    mLinePath.lineTo(mRect.width(),0);
                }
            }
        } else {
            if (mType == RECT) {
                mRoundRect = new RectF(mRect.left, mRect.top - offset, mRoundSize, mRect.bottom + offset);
            }else {
                if (mTriOritation == TOP) {
                    //稍微比矩形高出一点点
                    mPath.moveTo(-var1,-4);
                    mPath.lineTo(0,mTriSize);
                    mPath.lineTo(var1,-4);

                }else{
                    //稍微比矩形高出一点点
                    mPath.moveTo(-var1,mRect.height()+4);
                    mPath.lineTo(0,mRect.height() - mTriSize);
                    mPath.lineTo(var1,mRect.height()+4);

                }
                if (mShowTriLine) {
                    mLinePath.moveTo(0, 0);
                    mLinePath.lineTo(0, mRect.height());
                }
            }
        }
        if (mListener != null) {
            if (mCurrentColor != -1) {
                mListener.readyToShow(mCurrentColor);
            } else {
                mListener.readyToShow(Color.RED);
            }
        }

    }
    

    public MultiColors triOritation(int triOritation){
        mTriOritation = triOritation;
        return this;
    }
    public MultiColors triSize(int triSize){
        mTriSize = triSize;
        return this;
    }
    public MultiColors type(int mulType){
        mType = mulType;
        if (mType == RECT){
            mTypePaint.setStyle(Paint.Style.STROKE);
        }else{
            mTypePaint.setStyle(Paint.Style.FILL);
        }
        return this;
    }
    public MultiColors typeColor(int mulTypeColor){
        mTypePaint.setColor(mulTypeColor);
        return this;
    }
    public MultiColors showTriLine(boolean showTriLine){
        mShowTriLine = showTriLine;
        return this;
    }
    public MultiColors go(){
        invalidate();
        return this;
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
        if (mType == RECT) {
            canvas.drawRoundRect(mRoundRect, 2, 2, mTypePaint);
        }else {
            canvas.drawPath(mPath, mTypePaint);
            if (mShowTriLine) {
                canvas.drawPath(mLinePath, mLinePaint);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        if (mIsVertical) {
            if (mType == RECT) {
                if (y >= mRect.bottom - mRoundSize) {
                    y = mRect.bottom - mRoundSize;
                }
            }else{
                if (y >= mRect.bottom) {
                    y = mRect.bottom;
                }
            }
            if (y <= mRect.top) {
                y = mRect.top;
            }
        } else {
            if (x <= mRect.left) {
                x = mRect.left;
            }
            if (mType == RECT){
                if (x >= mRect.right - mRoundSize) {
                    x = mRect.right- mRoundSize;
                }
            }else {
                if (x >= mRect.right) {
                    x = mRect.right;
                }
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



    public MultiColors addListener(ColorSelectLiseter listener) {
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
        mWidth = AriesUtils.getDefaultSize(100,widthMeasureSpec);
        mHeight = AriesUtils.getDefaultSize(100,heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
}
