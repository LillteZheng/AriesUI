package com.zhengsr.ariesuilib.wieght.point;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhengsr.ariesuilib.R;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 原理就是用 windmanager ，把小红点的view添加过来，这样就可以显示在所有view的上面了
 */
class PointHelper implements View.OnTouchListener, BezierPointWindow.PointListener {
    private static final String TAG = "PointHelper";
    private BezierPointView mPointView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private BezierPointWindow mWindowView;
    private int[] mPos = new int[2];
    private FrameLayout.LayoutParams mLayoutParams;
    private FrameLayout mContainer;
    private Animator mAnimator;
    private ImageView mImageView;
    private AnimationDrawable mAnimationDrawable;

    public PointHelper(BezierPointView view) {
        mPointView = view;
        mContext = view.getContext();
        initWindow();
    }

    /**
     * 创建windowmanager
     */
    private void initWindow() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams();
            mParams.gravity = Gravity.LEFT | Gravity.TOP;
            mParams.format = PixelFormat.TRANSLUCENT;
            int type;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                type = WindowManager.LayoutParams.TYPE_TOAST;
            }

            mParams.type = type;
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mLayoutParams = new FrameLayout.LayoutParams(mPointView.getWidth(), mPointView.getHeight());
            //添加小红点
            mWindowView = new BezierPointWindow(mContext);
            mWindowView.addOriginalView(mPointView).setWindowType(type);

            //添加imageview

            mImageView = new ImageView(mContext);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {

            if (!mWindowView.isCanMove()) {
                return false;
            }
            ViewParent parent = v.getParent();
            if (parent == null) {
                return false;
            }
            //接管父控件的touch事件
            parent.requestDisallowInterceptTouchEvent(true);

            /**
             *  当按下时，再把View添加到windowmanager，此时的windowmanager是全屏的；
             *  所以，pointview的操作不会因为控件大小的限制，导致绘制不全的问题
             */

            addPointToWindow();
        }
        return mWindowView.onTouchEvent(event);
    }

    /**
     * 把TextView 的缓存bitmap给BezierPointWindow绘制
     */
    private void addPointToWindow() {

        if (mContainer == null) {
            mContainer = new FrameLayout(mContext);
            mContainer.setClipChildren(false);
            mContainer.setClipToPadding(false);
            mWindowView.setLayoutParams(mLayoutParams);
        }
        mContainer.removeAllViews();
        mContainer.addView(mWindowView, mParams);
        mWindowManager.addView(mContainer, mParams);
        //初始化坐标
        mPointView.getLocationInWindow(mPos);
        int width = mPointView.getWidth();
        int height = mPointView.getHeight();

        //设置大小和起始位置
        mWindowView.initPoint(mPos[0] + width / 2, mPos[1] + height / 2);
        mWindowView.setPointListener(this);
        mWindowView.setVisibility(View.VISIBLE);
        //拿到bitmap
        mPointView.setDrawingCacheEnabled(true);
        //拿到TextView的缓存bitmap，给BezierPointWindow 绘制，也为后面up的 imageview 当做背景图
        Bitmap bitmap = Bitmap.createBitmap(mPointView.getDrawingCache());
        mPointView.setDrawingCacheEnabled(false);
        mWindowView.setBitmap(bitmap);
    }

    @Override
    public void onDrawReady() {
        //这个时候，可以消失了，避免闪烁
        mPointView.setVisibility(View.GONE);
    }

    @Override
    public void drawUp() {
        mPointView.setVisibility(View.VISIBLE);
        //有点延时，避免闪烁
        mPointView.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeView();
            }
        }, 100);
    }

    @Override
    public void destroy(PointF pointF) {
        //移除所有
        mContainer.removeAllViews();
        //添加imageview，用于动画
        mContainer.addView(mImageView, mLayoutParams);
        //指定初始位置
        mImageView.setX(pointF.x - mPointView.getWidth() * 1.0f / 2);
        mImageView.setY(pointF.y - mPointView.getHeight() * 1.0f / 2);

        if (mPointView.isUseSelfAnim()){
            //使用自定义动画
            mImageView.setImageBitmap(mWindowView.getBitmap());
            if (mPointView.getListener() != null) {
                mPointView.getListener().destory(mImageView);
            }
        }else{
            //使用布局标签的属性动画
           if (mPointView.getAnimatorRes() != -1){
                mImageView.setImageBitmap(mWindowView.getBitmap());
                mAnimator = AnimatorInflater.loadAnimator(mContext, mPointView.getAnimatorRes());
                mAnimator.setTarget(mImageView);
                mAnimator.start();
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        removeView();
                    }
                });
            }else{
                //使用默认动画,即图片爆炸
                mImageView.setBackgroundResource(R.drawable.anim_blow);
                mAnimationDrawable = (AnimationDrawable) mImageView.getBackground();
                mAnimationDrawable.start();
                mImageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeView();
                    }
                },getAnimationDrawableTime(mAnimationDrawable));
            }
        }

    }

    /**
     * 移除View，这个必须要实现，否则就有一层view在顶层，啥也操作不了
     */
    public void removeView() {
        if (mWindowManager != null) {
            if (mContainer != null && mContainer.isAttachedToWindow()) {
                mWindowManager.removeView(mContainer);
            }
        }
        //还原set属性,防止view被设置动画，初始位置错乱
        mWindowView.clearAnimation();
        mWindowView.setAlpha(1);
        mWindowView.setX(0);
        mWindowView.setY(0);
        mWindowView.setScaleX(1);
        mWindowView.setScaleY(1);
        mWindowView.setVisibility(View.GONE);
        if (mContainer != null && mContainer.getChildCount() > 0) {
            mContainer.removeView(mWindowView);
        }
    }

    public void clearAnim() {
        if (mWindowView != null) {
            mWindowView.removeAnim();
        }
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.end();
            mAnimator.cancel();
        }
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }

    }

    /**
     * 获取帧动画时间
     *
     * @param drawable
     * @return
     */
    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrames = drawable.getNumberOfFrames();
        long time = 0;
        for (int i = 0; i < numberOfFrames; i++) {
            time += drawable.getDuration(i);
        }
        return time;
    }


}
