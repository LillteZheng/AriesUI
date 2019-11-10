package com.zhengsr.ariesuilib.wieght.point;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.utils.AriesUtils;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 原理就是用 windmanager ，把小红点的view添加过来，这样就可以显示在所有view的上面了
 */
class PointHelper implements View.OnTouchListener, BezierPointWindow.PointListener {
    private static final String TAG = "PointHelper";
    private BezierPointView mStaticView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private BezierPointWindow mWindowView;
    private int[] mPos = new int[2];
    private FrameLayout.LayoutParams mLayoutParams;
    private FrameLayout mContainer;
    private Animator mAnimator;
    private ImageView mImageView;

    public PointHelper(BezierPointView view) {
        mStaticView = view;
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
            if (Build.VERSION.SDK_INT >= 26) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                type = WindowManager.LayoutParams.TYPE_TOAST;
            }
            mParams.type = type;
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mLayoutParams = new FrameLayout.LayoutParams(mStaticView.getWidth(), mStaticView.getHeight());
            //添加小红点
            mWindowView = new BezierPointWindow(mContext);
            mWindowView.addOriginalView(mStaticView).setWindowType(type);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ViewParent parent = v.getParent();
        if (parent == null) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {

            if (!mWindowView.isCanMove()) {
                return false;
            }

            //接管父控件的点击事件
            parent.requestDisallowInterceptTouchEvent(true);

            /**
             *  当按下时，再把View添加到windowmanager，此时的windowmanager是全屏的；
             *  所以，pointview的操作不会因为控件大小的限制，导致绘制不全的问题
             */

            addPointToWindow();
        }

        return mWindowView.onTouchEvent(event);
    }

    private void addPointToWindow() {

        if (mContainer == null) {
            mContainer = new FrameLayout(mContext);
            mContainer.setClipChildren(false);
            mContainer.setClipToPadding(false);
            mWindowView.setLayoutParams(mLayoutParams);
        }
        mContainer.removeView(mWindowView);
        mContainer.addView(mWindowView, mParams);
        //初始化坐标
        mStaticView.getLocationInWindow(mPos);
        int width = mStaticView.getWidth();
        int height = mStaticView.getHeight();

        mLayoutParams.setMargins(mPos[0], mPos[1], 0, 0);

        //设置大小和起始位置
        mWindowView.initPoint(mPos[0] +width/2, mPos[1]+height/2);
        mWindowView.setPointListener(this);
        mWindowView.setVisibility(View.VISIBLE);
        //拿到bitmap
        mStaticView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mStaticView.getDrawingCache());
        mStaticView.setDrawingCacheEnabled(false);
        mWindowView.setBitmap(bitmap);

        mWindowManager.addView(mContainer, mParams);
        //这个时候，一开始的view，开始消失了
        mStaticView.setVisibility(View.GONE);
    }

    @Override
    public void drawUp() {
        removeView();
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void destroy() {
        if (mStaticView.getAnimatorRes() != -1){
            mAnimator = AnimatorInflater.loadAnimator(mContext, mStaticView.getAnimatorRes());
            mAnimator.setTarget(mWindowView);
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeView();
                }
            });
            mAnimator.start();
        }else if (mStaticView.getAnimationRes() != -1){

            Animation animation = AnimationUtils.loadAnimation(mContext, mStaticView.getAnimationRes());
            mWindowView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    removeView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });



        }else {
            if (!mStaticView.isUseDefaultAnim()) {
                if (mStaticView.getListener() != null) {
                    mStaticView.getListener().destory(mWindowView);
                }
            } else {
                removeView();
            }
        }
    }

    public void removeView(){
        if (mWindowManager != null) {
            if (mContainer.isAttachedToWindow()) {
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
        if (mContainer.getChildCount() > 0) {
            mContainer.removeView(mWindowView);
        }
    }

    public void removeAnim() {
        if (mWindowView != null) {
            mWindowView.removeAnim();
        }
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.end();
            mAnimator.cancel();
        }

    }

    /**
     * 获取帧动画时间
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
