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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 模仿QQ的红点拖拽view，这个我们不喜欢别人继承
 */
class BezierPointWindow extends View {
    private static final String TAG = "BeizerPointView";
    private Paint mPaint;
    private PointF mStartPoint;
    private PointF mMovePoint;
    private Path mPath;
    private boolean mIsBreakUp;
    private int mBarHeight = 0;
    private float mBitmapWidth, mBitmapHeight;

    private ValueAnimator mBackAnim;
    private PointListener mPointListener;

    /**
     * attrs
     */
    private float mDefaultRadius;
    private float mMaxMoveLength;
    private float mRecoveryBound;
    private float mDrawRadius;
    /**
     * logic
     */
    private Bitmap mOriginalBitmap;
    private boolean mDrawBitmap;
    private boolean mIsCanStart;
    private boolean mNotifyOutToRemove;
    private boolean mIsCanMove;



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
        mPath = new Path();

    }

    /**
     * 拿到自定义属性
     *
     * @param view
     * @return
     */
    public BezierPointWindow addOriginalView(BezierPointView view) {
        mPaint.setColor(view.getDrawColor());

        mDefaultRadius = view.getDefaultCircleSize() / 2;
        mIsCanMove = view.isCanMove();
        mMaxMoveLength = view.getMaxMoveLength();
        mRecoveryBound = view.getRecoveryBound();
        mDrawRadius = view.getDrawCircleSize() / 2;
        return this;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (mDrawBitmap) {
           //todo 绘制图片的效果交给外面的imageview
        } else {
            if (!mIsBreakUp) {
                //绘制贝塞尔曲线
                calculateBeizer();
                canvas.drawPath(mPath, mPaint);
                //绘制小球
                canvas.drawCircle(mStartPoint.x, mStartPoint.y, mDrawRadius, mPaint);
            }
            //绘制 TextView 的bitmap
            canvas.drawBitmap(mOriginalBitmap, mMovePoint.x - mBitmapWidth,
                    mMovePoint.y - mBitmapHeight, null);
        }

        //提醒外部，可以移除BezierPointView 了
        if (!mNotifyOutToRemove && mOriginalBitmap != null){
            mNotifyOutToRemove = true;
            mPointListener.onDrawReady();
        }
    }

    /**
     * 初始化
     *
     * @param x
     * @param y
     */
    public void initPoint(float x, float y) {
        mIsCanStart = true;
        mStartPoint = new PointF(x, y - mBarHeight);
        mMovePoint = new PointF(mStartPoint.x, mStartPoint.y);
        postInvalidate();
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsCanStart || !mIsCanMove) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsBreakUp = false;
                mDrawBitmap = false;
                mDrawRadius = mDefaultRadius;

                break;
            case MotionEvent.ACTION_MOVE:
                mMovePoint.set(event.getRawX(), event.getRawY() - mBarHeight);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsCanStart = false;

                if (getDistance(mMovePoint, mStartPoint) <= (mRecoveryBound + mDrawRadius)) {
                    backAnim();
                } else {
                   mIsBreakUp = true;
                   mDrawBitmap = true;
                    if (mPointListener != null) {
                        mPointListener.destroy(mMovePoint);
                    }
                }
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


    /**
     * 回弹动画
     */
    private void backAnim() {
        //要有个反弹的效果
        // mIsBreakUp = true;
        mBackAnim = ValueAnimator.ofObject(new PointFEvaluator(), mMovePoint, mStartPoint);
        mBackAnim.setDuration(100);
        //用回弹的插值器
        mBackAnim.setInterpolator(new OvershootInterpolator());
        mBackAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMovePoint = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        mBackAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mPointListener != null) {
                    mPointListener.drawUp();
                }
            }
        });
        mBackAnim.start();

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
        float offsetx0 = (float) (mDrawRadius * Math.sin(a));
        float offsety0 = (float) (mDrawRadius * Math.cos(a));

        //拿到第二个小球的偏移量
        float offsetx = (float) (mDefaultRadius * Math.sin(a));
        float offsety = (float) (mDefaultRadius * Math.cos(a));

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

        //形成贝塞尔曲线
        mPath.moveTo(p0x, p0y);
        mPath.quadTo(anchorx, anchory, p2x, p2y);
        mPath.lineTo(p3x, p3y);
        mPath.quadTo(anchorx, anchory, p1x, p1y);
        mPath.close();

        //超过一定距离时，且圆形的半径也要跟着变小
        double distance = getDistance(mMovePoint, mStartPoint);
        mDrawRadius = (int) (mDefaultRadius - distance / 14);
        if (mDrawRadius <= 7) {
            mDrawRadius = 7;
        }
        if (distance >= mMaxMoveLength) {
            // 超过一定距离 贝塞尔和固定圆都不要画了
            mIsBreakUp = true;
            mDrawRadius = 0;
            mPath.reset();
            return;
        }

    }

    public void setWindowType(int type) {
        mBarHeight = AriesUtils.getStatusBarHeight(getContext());
    }

    public boolean isCanMove() {
        return mIsCanMove;
    }


    public interface PointListener {

        /**
         * 需要回弹
         */
        void drawUp();

        /**
         * 需要消失
         */
        void destroy(PointF pointF);

        /**
         * 此时可以移除外面的view
         */
        void onDrawReady();
    }


    public void removeAnim() {

        if (mBackAnim != null) {
            mBackAnim.removeAllUpdateListeners();
            mBackAnim.end();
            mBackAnim.cancel();
        }
    }


    public void setPointListener(PointListener listener) {
        mPointListener = listener;
    }


    /**
     * 设置TextView的bitmap，这样就可以绘制了
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mNotifyOutToRemove = false;
        mOriginalBitmap = bitmap;
        mBitmapWidth = bitmap.getWidth() / 2;
        mBitmapHeight = bitmap.getHeight() / 2;
        postInvalidate();
    }

    public Bitmap getBitmap() {
        return mOriginalBitmap;
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

    /**
     * 自定义一个 Evaluator
     */
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
