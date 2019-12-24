package com.zhengsr.ariesuilib.wieght.flow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @auther by zhengshaorui on 2019/12/25
 * describe:
 */
public abstract class FlowAdapter {
    public abstract int getItemCount();

    public abstract View getView(LayoutInflater inflater, ViewGroup viewGroup,int position);
    public abstract void bindView(View view,int position);
}
