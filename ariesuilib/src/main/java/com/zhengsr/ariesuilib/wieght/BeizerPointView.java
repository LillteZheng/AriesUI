package com.zhengsr.ariesuilib.wieght;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

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
        mPath = new Path();
        mRectF = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, mRadius, mPaint);

        calculateBeizer();
        canvas.drawPath(mPath, mPaint);

        canvas.drawCircle(mMovePoint.x, mMovePoint.y, mWidth/2, mPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {


            case MotionEvent.ACTION_MOVE:
                mIsMove = true;
                mMovePoint.set(event.getX(), event.getY());
                float radius = mWidth / 2;
                mRectF.set(mMovePoint.x - radius - mWidthOffset,
                        mMovePoint.y - radius,
                        mMovePoint.x + radius + mWidthOffset,
                        mMovePoint.y + radius);
                break;
            case MotionEvent.ACTION_UP:
               // mIsMove = false;
              //  mPath.reset();
              //  mRadius = mWidth/2;
              //  mMovePoint.set(mStartPoint.x, mStartPoint.y);
                backAnim();
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void backAnim(){
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(),mMovePoint,mStartPoint);
        animator.setDuration(100);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMovePoint = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
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
            pointF.y = startValue.y + fraction * fraction * (endValue.y - startValue.y);// y方向抛物线加速移动
            return pointF;
        }
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

        float offsetx = (float) (mWidth/2 * Math.sin(a));
        float offsety = (float) (mWidth/2 * Math.cos(a));

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

        //同时，在超过一定距离时，且圆形的半径也要跟着变小

        double sqrt = Math.sqrt(dx * dx + dy * dy);
        if (sqrt >= 50) {
            mRadius -= 0.5f;
            if (mRadius <= 8) {
                mRadius = 8;
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = AriesUtils.getDefaultSize(100, widthMeasureSpec);
        int height = AriesUtils.getDefaultSize(100, heightMeasureSpec);
        int result = Math.min(width, height);
        setMeasuredDimension(result, result);

    }
}
