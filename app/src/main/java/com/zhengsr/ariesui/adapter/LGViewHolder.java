package com.zhengsr.ariesui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class LGViewHolder {
    private Context mContext;
    private View mConverView;
    private static int mPosition;
    private SparseArray<View> mViewSparseArray;
    public LGViewHolder(Context context, int layout, ViewGroup parent, int position){
        mContext = context;
        mConverView = LayoutInflater.from(mContext).inflate(layout,parent,false);
        mViewSparseArray = new SparseArray<>();
        mConverView.setTag(this);
        mPosition = position;

    }

    /**
     * 可以理解成单例吧，获取一个viewholder
     * @param context
     * @param converView
     * @param layout
     * @param parent
     * @param position
     * @return
     */
    public static LGViewHolder getViewHolder(Context context, View converView, int layout,
                                             ViewGroup parent, int position){
        if (converView ==null){
            //注意这里，其实跟baseadapter的判断是一致的，其中settag也是在viewholder里
            return  new LGViewHolder(context,layout,parent,position);
        }else{
            LGViewHolder holder = (LGViewHolder) converView.getTag();
            mPosition = position;
            return holder;
        }
    }

    /**
     * 返回一个viewholder
     * @return
     */
    public View getConverView() {
        return mConverView;
    }

    /**
     * key-value 通过sparseArray来获取view，属性不同，这里用一个泛型来实现
     * @param viewid
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewid){
        View view = mViewSparseArray.get(viewid);
        if (view == null){
            view = mConverView.findViewById(viewid);
            mViewSparseArray.put(viewid,view);
        }
        return (T) view;
    }

    /**
     * 设置text
     * @param viewid
     * @param msg
     */
    public LGViewHolder setText(int viewid, String msg){
        TextView tv = getView(viewid);
        tv.setVisibility(View.VISIBLE);
        tv.setText(msg);
        return this;
    }

    public LGViewHolder setText(int viewid, int  msgid){
        TextView tv = getView(viewid);
        tv.setVisibility(View.VISIBLE);
        tv.setText(mContext.getString(msgid));
        return this;
    }


    /**
     * 设置drawable
     * @param viewid
     * @param drawable
     */
    public LGViewHolder setDrawable(int viewid, Drawable drawable){
        ImageView iv = getView(viewid);
        iv.setImageDrawable(drawable);
        return this;
    }

    public LGViewHolder setBitmap(int viewid, Bitmap bitmap){
        ImageView iv = getView(viewid);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置drawable
     * @param viewid
     * @param iconId
     */
    public LGViewHolder setDrawable(int viewid, int  iconId){
        ImageView iv = getView(viewid);
        iv.setImageResource(iconId);
        return this;
    }

    /**
     * 设置checkbox
     * @param viewid
     * @param status
     */
    public LGViewHolder setCheckbox(int viewid, boolean status){
        CheckBox cb = getView(viewid);
        cb.setVisibility(View.VISIBLE);
        cb.setChecked(status);
        return this;

    }


    /**
     * 设置itemview 的背景
     * @param viewid
     */
    public LGViewHolder setItemBackground(int viewid, int resourid){
        View view = getView(viewid);
        //Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),resourid);
        view.setBackgroundResource(resourid);
        return this;
    }


    public int getColor(int colorId) {
        return mContext.getResources().getColor(colorId);
    }
}