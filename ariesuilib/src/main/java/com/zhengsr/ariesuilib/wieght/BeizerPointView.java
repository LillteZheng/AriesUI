package com.zhengsr.ariesuilib.wieght;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 模仿QQ的红点拖拽view
 */
public class BeizerPointView extends View {
    private static final String TAG = "BeizerPointView";
    private Paint mPaint;
    private float mWidth, mHeight;
    private boolean mIsMove;
    private PointF mStartPoint;
    private PointF mMovePoint;
    private Path mPath;
    private float mRadius;
    private RectF mRectF;
    private float mWidthOffset = 5;
    private Paint mTextPaint;
    private RectF mTextRect;
    private boolean mIsBreakUp;
    private boolean mIsHide;
    private boolean mIsDown;
    private Bitmap[] mBitmaps = {
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow1),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow2),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow3),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow4),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow5),
            null,
    };
    private Rect mBitmapRect;
    private int mBitmapIndex;
    private float mDefualtRadius;

    public BeizerPointView(Context context) {
        this(context, null);
    }

    public BeizerPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeizerPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.WHITE);

        mPath = new Path();
        mRectF = new RectF();
        mTextRect = new RectF();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制爆炸图片
        if (mIsHide){
            if (mBitmapIndex <= mBitmaps.length - 2) {
                canvas.drawBitmap(mBitmaps[mBitmapIndex], mBitmapRect, mTextRect, null);
            }

        }else {
            if (!mIsBreakUp) {
                canvas.drawCircle(mStartPoint.x, mStartPoint.y, mRadius, mPaint);
            }
            calculateBeizer();
            canvas.drawPath(mPath, mPaint);

            canvas.drawCircle(mMovePoint.x, mMovePoint.y, mWidth / 2, mPaint);

            //绘制文字
            AriesUtils.drawText("99+", mTextPaint, mTextRect, canvas);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHide = false;
                mIsBreakUp = false;
                mRadius = mWidth / 2;
                break;

            case MotionEvent.ACTION_MOVE:
                mIsMove = true;
                mMovePoint.set(event.getX(), event.getY());

                mRectF.set(mMovePoint.x - mDefualtRadius - mWidthOffset,
                        mMovePoint.y - mDefualtRadius,
                        mMovePoint.x + mDefualtRadius + mWidthOffset,
                        mMovePoint.y + mDefualtRadius);
                mTextRect.set(mMovePoint.x - mDefualtRadius,
                        mMovePoint.y - mDefualtRadius,
                        mMovePoint.x + mDefualtRadius,
                        mMovePoint.y + mDefualtRadius);
                break;
            case MotionEvent.ACTION_UP:
                RectF rectF = new RectF(-mWidth/2,-mHeight/2,mWidth/2+mWidth,mHeight/2+mHeight);
                if (rectF.contains(mMovePoint.x,mMovePoint.y)){
                    backAnim();
                }else{
                    mIsHide = true;
                    bitmapAnim();
                }


                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


    private void bitmapAnim(){
        ValueAnimator animator = ValueAnimator.ofInt(0,mBitmaps.length -1);
        animator.setDuration(1000);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBitmapIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    private void backAnim() {
        //要有个反弹的效果

        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(), mMovePoint, mStartPoint);
        animator.setDuration(100);
        //用回弹的插值器
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMovePoint = (PointF) animation.getAnimatedValue();
                float radius = mWidth / 2;
                mTextRect.set(mMovePoint.x - radius,
                        mMovePoint.y - radius,
                        mMovePoint.x + radius,
                        mMovePoint.y + radius);
                invalidate();
            }
        });

        animator.start();

    }



    /**
     * 计算贝塞尔曲线
     * 由于Math 的扫脚函数，都自带正负的，所以，只需要计算一种方向即可
     * 这里的计算方向为右下
     */
    private void calculateBeizer() {
        float x0 = mStartPoint.x;
        float y0 = mStartPoint.y;
        float x = mMovePoint.x;
        float y = mMovePoint.y;

        //算出夹角
        float dx = x - x0;
        float dy = y - y0;
        double a = Math.atan(dy / dx);
        //拿到圆切点的长度偏移量
        float offsetx0 = (float) (mRadius * Math.sin(a));
        float offsety0 = (float) (mRadius * Math.cos(a));

        float offsetx = (float) (mWidth / 2 * Math.sin(a));
        float offsety = (float) (mWidth / 2 * Math.cos(a));

        //算出第一个圆的切点坐标
        float p0x = x0 + offsetx0;
        float p0y = y0 - offsety0;

        float p1x = x0 - offsetx0;
        float p1y = y0 + offsety0;
        //算出第二个圆的切点坐标
        float p2x = x + offsetx;
        float p2y = y - offsety;

        float p3x = x - offsetx;
        float p3y = y + offsety;
        //计算贝塞尔辅助点
        float anchorx = (x0 + x) / 2;
        float anchory = (y0 + y) / 2;
        //清掉上次，避免残留
        mPath.reset();
        if (!mIsBreakUp) {
            mPath.moveTo(p0x, p0y);
            mPath.quadTo(anchorx, anchory, p2x, p2y);
            mPath.lineTo(p3x, p3y);
            mPath.quadTo(anchorx, anchory, p1x, p1y);
            mPath.close();
        }

        //超过一定距离时，且圆形的半径也要跟着变小
        double sqrt = Math.sqrt(dx * dx + dy * dy);

        if (sqrt >= 20) {
            mRadius -= 1;
            if (mRadius <= 7) {
                mRadius = 7;

            }
            if (!mIsBreakUp && sqrt > 150) {
                //如果已经超过很多了，就要断掉了
                mIsBreakUp = true;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = mWidth / 2;
        mStartPoint = new PointF(mWidth / 2, mHeight / 2);
        mMovePoint = new PointF(mStartPoint.x, mStartPoint.y);
        mRectF.set(-mWidthOffset, 0, mWidth + mWidthOffset, mHeight);
        mTextRect.set(0, 0, mWidth, mHeight);
        Bitmap bitmap = mBitmaps[0];
        mBitmapRect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        mDefualtRadius = mWidth / 2;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = AriesUtils.getDefaultSize(100, widthMeasureSpec);
        int height = AriesUtils.getDefaultSize(100, heightMeasureSpec);
        int result = Math.min(width, height);
        setMeasuredDimension(result, result);

    }
    class PointFEvaluator implements TypeEvaluator<PointF> {
        private static final String TAG = "PointFEvaluator";
        PointF pointF = new PointF();

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            /**
             * 参数说明：
             * fraction为插值器中 getInterpolation 的input 值，默认为AccelerateDecelerateInterpolator的值
             * starValue 为初始值的object类型，同理endValue
             */
            pointF.x = startValue.x + fraction * (endValue.x - startValue.x);// x方向匀速移动
            pointF.y = startValue.y + fraction * (endValue.y - startValue.y);// y方向匀速移动
            return pointF;
        }
    }
}
