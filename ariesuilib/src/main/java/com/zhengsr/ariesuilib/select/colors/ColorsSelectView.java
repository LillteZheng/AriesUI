package com.zhengsr.ariesuilib.select.colors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhengsr.ariesuilib.R;
import com.zhengsr.ariesuilib.select.colors.callback.ColorSelectLiseter;

/**
 * @auther by zhengshaorui on 2019/10/22
 * describe:模仿Android studio 的颜色选择器
 */
public class ColorsSelectView extends FrameLayout {
    private static final String TAG = "ColorsSelectView";
    /**
     * static
     */
    public static final int RECT = 1;
    public static final int TRI = 2;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;
    private ColorListener mLiseter;
    private MultiColors mMulColors;
    private ColorsGradient mColorsGradient;
    private int mColor;

    public ColorsSelectView(Context context) {
        this(context,null);
    }

    public ColorsSelectView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorsSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ColorsSelectView);
        int gradientHeight = ta.getDimensionPixelSize(R.styleable.ColorsSelectView_color_gradient_height,-1);
        int mulColorHeight = ta.getDimensionPixelSize(R.styleable.ColorsSelectView_color_mulcolor_height,-1);
        int colorLyHeight = ta.getDimensionPixelSize(R.styleable.ColorsSelectView_color_textly_height,-1);
        int circleSize = ta.getDimensionPixelSize(R.styleable.ColorsSelectView_color_circle_size,-1);
        int textSize = ta.getDimensionPixelSize(R.styleable.ColorsSelectView_color_text_size,14);
        int textColor = ta.getColor(R.styleable.ColorsSelectView_color_text_color, Color.BLACK);
        ta.recycle();

        /**
         * view
         */
        View view = LayoutInflater.from(context).inflate(R.layout.colorselect_layout,this,false);
        addView(view);
        setClipChildren(false);

        mColorsGradient = view.findViewById(R.id.colorgradient);
        mMulColors = view.findViewById(R.id.rectcolor);
        final View backView = view.findViewById(R.id.view);
        final TextView textView = view.findViewById(R.id.textview);
        LinearLayout linearLayout = view.findViewById(R.id.color_ly);
        /**
         * logic
         */
        if (circleSize != -1){
            mColorsGradient.radiuds(circleSize);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        textView.setTextColor(textColor);
        mMulColors.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {
                mColorsGradient.color(color);
                backView.setBackgroundColor(color);
                String colorMsg = "#"+Integer.toHexString(color).toUpperCase();
                textView.setText(colorMsg);
                mColor = color;
            }

            @Override
            public void onGetColor(int color) {
                mColorsGradient.color(color);
                if (mLiseter != null) {
                    mLiseter.onColor(color);
                }
                mColor = color;
                String colorMsg = "#"+Integer.toHexString(color).toUpperCase();
                textView.setText(colorMsg);
                backView.setBackgroundColor(color);
            }
        });
        mColorsGradient.addListener(new ColorSelectLiseter() {
            @Override
            public void readyToShow(int color) {

            }

            @Override
            public void onGetColor(int color) {
                mColor = color;
                if (mLiseter != null) {
                    mLiseter.onColor(color);
                }
                backView.setBackgroundColor(color);
                String colorMsg = "#"+Integer.toHexString(color).toUpperCase();
                textView.setText(colorMsg);
            }
        });

        //设置间距
        if (gradientHeight != -1) {
            setDimen(mColorsGradient, gradientHeight);
        }
        if (mulColorHeight != -1) {
            setDimen(mMulColors, mulColorHeight);
        }
        if (colorLyHeight != -1){
            setDimen(linearLayout,colorLyHeight);
        }



    }



    public interface ColorListener{
        void onColor(int color);
    }

    public ColorsSelectView addListener(ColorListener liseter){
        mLiseter = liseter;
        return this;
    }
    private void setDimen(View view,int height){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }


    public ColorsSelectView type(int type){
        mMulColors.type(type);
        return this;
    }
    public ColorsSelectView typeColor(int typeColor){
        mMulColors.typeColor(typeColor);
        return this;
    }

    public ColorsSelectView triSize(int triSize){
        mMulColors.triSize(triSize);
        return this;
    }

    public ColorsSelectView triOritation(int triOritation){
        mMulColors.triOritation(triOritation);
        return this;
    }

    public ColorsSelectView circleColor(int circleColor){
        mColorsGradient.circleColor(circleColor);
        return this;
    }
    public ColorsSelectView circleRadius(int circleRadius){
        mColorsGradient.radiuds(circleRadius);
        return this;
    }

    public void go() {
        mMulColors.go();
        mColorsGradient.go();
    }

    public int getColor(){
        return mColor;
    }

}
