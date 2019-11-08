package com.zhengsr.ariesuilib.wieght.point;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
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
public class PointHelper implements View.OnTouchListener, BezierPointWindow.PointListener {
    private static final String TAG = "PointHelper";
    private View mStaticView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private BezierPointWindow mPointView;
    private int[] mPos = new int[2];
    private FrameLayout.LayoutParams mLayoutParams;
    private FrameLayout mContainer;
    private ImageView mImageView;
    public PointHelper(View view){
        mStaticView = view;
        mContext = view.getContext();
        initWindow();
    }

    /**
     * 创建windowmanager
     */
    private void initWindow() {
        if (mWindowManager == null){
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mParams = new WindowManager.LayoutParams();
            mParams.gravity = Gravity.LEFT | Gravity.TOP;
            mParams.format = PixelFormat.TRANSLUCENT;
            int type ;
            if (Build.VERSION.SDK_INT >= 26) {
                type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else{
                type = WindowManager.LayoutParams.TYPE_TOAST;
            }
            mParams.type = type;
            mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            mLayoutParams = new FrameLayout.LayoutParams(mStaticView.getWidth(), mStaticView.getHeight());
            //添加小红点
            mPointView = new BezierPointWindow(mContext);
            mPointView.setWindowType(type);
            mImageView = new ImageView(mContext);
            mImageView.setBackgroundResource(R.drawable.anim_blow);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){

        /*    if (!mPointView.isCanMove()){
                return false;
            }*/

            ViewParent parent = v.getParent();
            if (parent == null) {
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
        return mPointView.onTouchEvent(event);
    }

    private void addPointToWindow() {

        if (mContainer == null) {
            mContainer = new FrameLayout(mContext);
            mContainer.setClipChildren(false);
            mContainer.setClipToPadding(false);
            mPointView.setLayoutParams(mLayoutParams);
            mImageView.setLayoutParams(mLayoutParams);
            mContainer.addView(mPointView, mParams);
        }

        //初始化坐标
        mStaticView.getLocationOnScreen(mPos);
        int width = mStaticView.getWidth();
        int height = mStaticView.getHeight();

        mLayoutParams.setMargins(mPos[0], mPos[1], 0, 0);

        //设置大小和起始位置
        mPointView.initPoint(mPos[0]+width/2,mPos[1]+height/2);
        mPointView.setPointListener(this);
        mPointView.setVisibility(View.VISIBLE);
        //拿到bitmap
        mStaticView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mStaticView.getDrawingCache());
        mStaticView.setDrawingCacheEnabled(false);
        mPointView.setBitmap(bitmap);

        mWindowManager.addView(mContainer,mParams);
        //这个时候，一开始的view，开始消失了
        mStaticView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void drawUp() {
        if (mWindowManager != null) {
            mWindowManager.removeView(mContainer);
        }
        mStaticView.setVisibility(View.VISIBLE);
    }

    @Override
    public void desStroy(float x, float y) {
        mPointView.setVisibility(View.GONE);
        AnimationDrawable background = (AnimationDrawable) mImageView.getBackground();
        mLayoutParams.setMargins((int) x,(int) y ,0,0);
        mContainer.addView(mImageView,mLayoutParams);
        background.start();

        mContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mWindowManager != null) {
                    mWindowManager.removeView(mContainer);
                }
            }
        },getAnimationDrawableTime(background));
    }



    private long getAnimationDrawableTime(AnimationDrawable drawable) {
        int numberOfFrames = drawable.getNumberOfFrames();
        long time = 0;
        for (int i=0;i<numberOfFrames;i++){
            time += drawable.getDuration(i);
        }
        return time;
    }

}
