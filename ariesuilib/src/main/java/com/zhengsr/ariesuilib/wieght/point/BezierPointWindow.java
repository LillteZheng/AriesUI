package com.zhengsr.ariesuilib.wieght.point;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 模仿QQ的红点拖拽view，这个我们不喜欢别人继承
 */
 class BezierPointWindow extends AbsPointView {
    private static final String TAG = "BeizerPointView";
    private Paint mPaint;
    private PointF mStartPoint;
    private PointF mMovePoint;
    private Path mPath;
    private float mRadius;
    private boolean mIsBreakUp;
    private boolean mIsHide;
    private int mBarHeight = 0;
    private float mBitmapWidth, mBitmapHeight;
    private Bitmap mOriginalBitmap;

    public BezierPointWindow(Context context) {
        this(context, null);
    }

    public BezierPointWindow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierPointWindow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mDrawColor);
        mPath = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (!mIsHide) {
            calculateBeizer();
            canvas.drawPath(mPath, mPaint);
            if (!mIsBreakUp) {
                canvas.drawCircle(mStartPoint.x, mStartPoint.y, mRadius, mPaint);
            }
        }


        canvas.drawBitmap(mOriginalBitmap, mMovePoint.x - mBitmapWidth,
                mMovePoint.y - mBitmapHeight, null);

    }


    public void initPoint(float x, float y) {
        mRadius = mWidth / 2;
        mStartPoint = new PointF(x, y - mBarHeight);
        mMovePoint = new PointF(mStartPoint.x, mStartPoint.y);
        postInvalidate();
        Log.d(TAG, "zsr - initPoint: "+mWidth+" "+mHeight+" "+mDrawColor+" "+mRecoveryBound);
    }


    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHide = false;
                mIsBreakUp = false;
                mRadius = mWidth / 2;
                break;

            case MotionEvent.ACTION_MOVE:
                mMovePoint.set(event.getRawX(), event.getRawY() - mBarHeight);

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mIsBreakUp) {
                    mIsHide = true;
                    backAnim();
                } else {
                    if (mPointListener != null) {
                        mPointListener.desStroy(
                                event.getRawX() - mWidth / 2,
                                event.getRawY() - mHeight / 2 - mBarHeight);
                    }
                }

                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


    private void backAnim() {
        //要有个反弹的效果
        // mIsBreakUp = true;
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(), mMovePoint, mStartPoint);
        animator.setDuration(100);
        //用回弹的插值器
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMovePoint = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mPointListener != null) {
                    mPointListener.drawUp();
                }
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

        mPath.moveTo(p0x, p0y);
        mPath.quadTo(anchorx, anchory, p2x, p2y);
        mPath.lineTo(p3x, p3y);
        mPath.quadTo(anchorx, anchory, p1x, p1y);
        mPath.close();

        //超过一定距离时，且圆形的半径也要跟着变小
        double distance = getDistance(mMovePoint, mStartPoint);
        mRadius = (int) (mDefaultCircleSize - distance / 8);
        if (distance > mMaxMoveLength) {
            // 超过一定距离 贝塞尔和固定圆都不要画了
            mPath.reset();
            mIsBreakUp = true;
            return;
        } else {
            mIsBreakUp = false;
        }
        if (mRadius <= 7) {
            mRadius = 7;
        }

    }

    public void setWindowType(int type) {
        if (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
            mBarHeight = AriesUtils.getStatusBarHeight(getContext());
        }
    }

    public boolean isCanMove() {
        return mIsCanMove;
    }


    public interface PointListener {

        /**
         * 需要消失
         */
        void drawUp();

        void desStroy(float x, float y);
    }

    private PointListener mPointListener;

    public void setPointListener(PointListener listener) {
        mPointListener = listener;
    }





    public void setBitmap(Bitmap bitmap) {
        mOriginalBitmap = bitmap;
        mBitmapWidth = bitmap.getWidth() / 2;
        mBitmapHeight = bitmap.getHeight() / 2;
        postInvalidate();
    }


    /**
     * 获取两个圆之间的距离
     *
     * @param point1
     * @param point2
     * @return
     */
    private double getDistance(PointF point1, PointF point2) {
        return Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));

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
