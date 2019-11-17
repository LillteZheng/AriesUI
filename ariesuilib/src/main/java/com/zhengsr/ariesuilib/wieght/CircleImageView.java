package com.zhengsr.ariesuilib.wieght;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @auther by zhengshaorui on 2019/11/16
 * describe: 圆形图片
 */
public class CircleImageView extends AppCompatImageView {
    private Bitmap mBitmap;
    private Shader mShader;
    private Paint mPaint;
    private float mRadius,mBorderRadius;
    private Matrix mMatrix;
    private int mBorderWidth;
    private int mBorderColor;
    private Paint mBoardPaint;
    private RectF mBorderRect;
    private RectF mDrawableRect;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.CENTER_CROP);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth,0);
        mBorderColor = ta.getColor(R.styleable.CircleImageView_cv_boardColor,-2);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        mBoardPaint = new Paint();
        mBoardPaint.setAntiAlias(true);
        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setStrokeWidth(mBorderWidth);
        mBoardPaint.setColor(mBorderColor);
        mMatrix = new Matrix();
        mDrawableRect = new RectF();
        mBorderRect = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawCircle(mDrawableRect.centerX(),mDrawableRect.centerY(),mRadius,mPaint);
        if (mBorderWidth > 0){
            canvas.drawCircle(mBorderRect.centerX(),mBorderRect.centerY(),mBorderRadius,mBoardPaint);
        }
    }

    /**
     * 拿到bitmap
     */
    private void initializeBitmap() {
        BitmapDrawable bd = (BitmapDrawable) getDrawable();
        if (bd != null) {
            mBitmap = bd.getBitmap();
        }
        setUp();
    }

    private void setUp() {
        if (getWidth() == 0 || getHeight() == 0 || mBitmap == null) {
            return;
        }

        mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);


        mBorderRect.set(calculateBounds());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);

        mRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);


        updateShaderMatrix();
        invalidate();
    }


    /**
     * 拿到除padding后的 rect
     * @return
     */
    private RectF calculateBounds() {
        int availableWidth  = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mMatrix.set(null);


        if (mBitmap.getWidth() * mDrawableRect.height() > mDrawableRect.width() * mBitmap.getHeight()) {
            scale = mDrawableRect.height() / (float) mBitmap.getHeight();
            dx = (mDrawableRect.width() - mBitmap.getWidth() * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmap.getWidth();
            dy = (mDrawableRect.height() - mBitmap.getHeight() * scale) * 0.5f;
        }

        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mShader.setLocalMatrix(mMatrix);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = AriesUtils.getDefaultSize(100, widthMeasureSpec);
        int height = AriesUtils.getDefaultSize(100, heightMeasureSpec);
        //保证是要圆形
        int result = Math.min(width, height);
        setMeasuredDimension(result, result);
    }
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }
}
