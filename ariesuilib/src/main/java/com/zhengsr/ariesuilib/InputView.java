package com.zhengsr.ariesuilib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @auther by zhengshaorui on 2019/11/16
 * describe: 一个结合 edittext 和图片的控件
 */
public abstract class InputView extends FrameLayout implements View.OnFocusChangeListener, TextWatcher {
    /**
     * static
     */
    private static final String TAG = "InputView";
    private int mBottomColor;
    private ImageView[] mLeftImages;
    protected EditText mEditText;
    private ImageView[] mRightImages;


    public InputView(Context context) {
        this(context, null);
    }

    public InputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray ta = context.obtainStyledAttributes(attrs, com.zhengsr.ariesuilib.R.styleable.InputView);
        //左边ImageView
        //EditText
        int textColor = ta.getColor(com.zhengsr.ariesuilib.R.styleable.InputView_iv_edTextColor, Color.BLUE);
        int hintColor = ta.getColor(com.zhengsr.ariesuilib.R.styleable.InputView_iv_edHintColor, Color.parseColor("#999999"));
        int textSize = ta.getDimensionPixelSize(com.zhengsr.ariesuilib.R.styleable.InputView_iv_edTextSize, 14);
        //底部Bottom
        mBottomColor = ta.getColor(com.zhengsr.ariesuilib.R.styleable.InputView_iv_bottomColor, Color.BLUE);
        int bottomSize = ta.getDimensionPixelSize(com.zhengsr.ariesuilib.R.styleable.InputView_iv_bottomSize, 2);
        ta.recycle();


        /**
         * 左边图片
         */
        int leftMargin = 0;
        int[] leftImgSize = getLeftImgSize();
        if (leftImgSize != null) {
            mLeftImages = leftImageView();
            for (int i = 0; i < mLeftImages.length; i++) {
                ImageView imageView = mLeftImages[i];
                LayoutParams params = new LayoutParams(leftImgSize[0], leftImgSize[1]);
                params.setMarginStart(leftImgSize[0] * i);
                params.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
                addView(imageView, params);
                leftMargin += leftImgSize[0];
            }
        }
        /**
         * 右边图片
         */
        int rightMargin = 0;
        int[] rightImgSize = getRightImgSize();
        if (rightImgSize != null) {
            mRightImages = rightImageView();
            //加一个padding，以便于点击
            int paddding = getIntDpSize(3);
            for (int i = 0; i < mRightImages.length; i++) {
                ImageView imageView = mRightImages[i];
                LayoutParams params = new LayoutParams(rightImgSize[0], rightImgSize[1]);
                imageView.setPadding(paddding,paddding,paddding,paddding);
                params.rightMargin = (int) ((rightImgSize[0] + getDpSize(5)) * i);
                params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
                addView(imageView, params);
                rightMargin += rightImgSize[0];
            }
        }

        //添加EditText

        mEditText = new EditText(context);
        LayoutParams edParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        edParams.gravity = Gravity.CENTER_VERTICAL;
        edParams.setMargins(leftMargin + (int)getDpSize(5), (int)getDpSize(3), rightMargin, 0);
        mEditText.setBackgroundColor(Color.TRANSPARENT);
        mEditText.setSingleLine();
        mEditText.setTextColor(textColor);
        mEditText.setHintTextColor(hintColor);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        mEditText.setOnFocusChangeListener(this);
        mEditText.addTextChangedListener(this);
        addView(mEditText, edParams);

        //配置下划线大小和颜色
        View view = new View(context);
        LayoutParams viewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                bottomSize);
        viewParams.gravity = Gravity.BOTTOM;
        view.setBackgroundColor(mBottomColor);
        addView(view, viewParams);

        config();
    }




    /**
     * 可以在这里配置EditText 的属性
     */
    public void config() {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mLeftImages != null) {
            for (ImageView image : mLeftImages) {
                if (hasFocus) {
                    image.setColorFilter(mBottomColor);
                } else {
                    image.clearColorFilter();
                }
            }
        }
        if (mRightImages != null) {
            for (ImageView image : mRightImages) {
                if (mEditText.getText().length() > 0) {
                    image.setVisibility(VISIBLE);
                } else {
                    image.setVisibility(INVISIBLE);
                }

            }
        }
    }

    /**
     * 拿到右边图片的大小
     * @return
     */
    public int[] getRightImgSize() {
        return null;
    }

    /**
     * 拿到左边图片的大小
     * @return
     */
    public int[] getLeftImgSize() {
        return null;
    }

    /**
     * 拿到坐标图片
     * @return
     */
    public abstract ImageView[] leftImageView();

    /**
     * 拿到右边图片
     * @return
     */
    public abstract ImageView[] rightImageView();

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mRightImages != null) {
            for (ImageView image : mRightImages) {
                if (mEditText.getText().length() > 0) {
                    image.setVisibility(VISIBLE);
                } else {
                    image.setVisibility(INVISIBLE);
                }
            }
        }
    }

    /**
     * 改变EditText的模式，是否隐藏
     * @param hide
     */
    protected void changeEdPassMode(boolean hide){
        if (hide) {
            //隐藏
            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else{
            mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mEditText.setSelection(mEditText.getText().toString().length());
    }

    /**
     * 是否是隐藏模式
     * @return
     */
    protected boolean isEdHideMode(){
        return mEditText.getTransformationMethod() == HideReturnsTransformationMethod.getInstance();
    }

    /**
     * 拿到dp
     * @param dp
     * @return
     */
    protected int getIntDpSize(int dp){
        return (int) getDpSize(dp);
    }
    /**
     * 拿到dp
     * @param dp
     * @return
     */
    protected float getDpSize(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }
}
