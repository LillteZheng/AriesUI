package com.zhengsr.ariesuilib.wieght.point;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
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
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

import com.zhengsr.ariesuilib.R;
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
    private Bitmap mOriginalBitmap;
    private float mDefaultRadius;
    private boolean mIsCanMove;
    private float mMaxMoveLength;
    private float mRecoveryBound;
    private float mDrawRadius;
    private ValueAnimator mBackAnim;
    private PointListener mPointListener;
    private Rect mBitmapRect;
    private RectF mDstBitRect;
    private int mBitmapIndex;
    private boolean mDrawBitmap;
    private boolean mIsCanStart;

    private Bitmap[] mBitmaps = {
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow1),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow2),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow3),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow4),
            BitmapFactory.decodeResource(getResources(), R.mipmap.blow5),
            null,
    };
    private boolean mUseDefaultAnim;
    private ValueAnimator mBitmapAnim;

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
        mUseDefaultAnim = view.isUseDefaultAnim();
        Bitmap bitmap = mBitmaps[0];
        mBitmapRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDstBitRect = new RectF();
        return this;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (mDrawBitmap) {
            if (mBitmapIndex <= mBitmaps.length - 2) {
                canvas.drawBitmap(mBitmaps[mBitmapIndex], mBitmapRect, mDstBitRect, null);
            }
        } else {
            if (!mIsBreakUp) {
                calculateBeizer();
                canvas.drawPath(mPath, mPaint);
                canvas.drawCircle(mStartPoint.x, mStartPoint.y, mDrawRadius, mPaint);
            }
            canvas.drawBitmap(mOriginalBitmap, mMovePoint.x - mBitmapWidth,
                    mMovePoint.y - mBitmapHeight, null);
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
        if (!mIsCanStart) {
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
                    if (mUseDefaultAnim) {
                        mDrawBitmap = true;
                        bitmapAnim();
                    } else {
                        if (mPointListener != null) {
                          //  setX(event.getRawX());
                          //  setY(event.getRawY() - mBarHeight);
                            mPointListener.destroy();
                        }
                    }
                }
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void bitmapAnim() {

        float offset = 3 * mDefaultRadius / 2;
        mDstBitRect.set(mMovePoint.x - offset, mMovePoint.y - offset,
                mMovePoint.x + offset, mMovePoint.y + offset);

        mBitmapAnim = ValueAnimator.ofInt(0, mBitmaps.length - 1);
        mBitmapAnim.setDuration(1000);
        mBitmapAnim.setInterpolator(new OvershootInterpolator());
        mBitmapAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBitmapIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mBitmapAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mPointListener != null) {
                    mPointListener.destroy();

                }
            }
        });
        mBitmapAnim.start();

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

        mPath.moveTo(p0x, p0y);
        mPath.quadTo(anchorx, anchory, p2x, p2y);
        mPath.lineTo(p3x, p3y);
        mPath.quadTo(anchorx, anchory, p1x, p1y);
        mPath.close();

        //超过一定距离时，且圆形的半径也要跟着变小
        double distance = getDistance(mMovePoint, mStartPoint);
        mDrawRadius = (int) (mDefaultRadius - distance / 8);
        if (distance > mMaxMoveLength) {
            // 超过一定距离 贝塞尔和固定圆都不要画了
            mPath.reset();
            mIsBreakUp = true;
            return;
        }
        if (mDrawRadius <= 7) {
            mDrawRadius = 7;
        }

    }

    public void setWindowType(int type) {
        if (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
        }
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
        void destroy();
    }


    public void removeAnim() {
        if (mBitmapAnim != null) {
            mBitmapAnim.removeAllUpdateListeners();
            mBitmapAnim.end();
            mBitmapAnim.cancel();
        }
        if (mBackAnim != null) {
            mBackAnim.removeAllUpdateListeners();
            mBackAnim.end();
            mBackAnim.cancel();
        }
    }


    public void setPointListener(PointListener listener) {
        mPointListener = listener;
    }


    public void setBitmap(Bitmap bitmap) {
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
