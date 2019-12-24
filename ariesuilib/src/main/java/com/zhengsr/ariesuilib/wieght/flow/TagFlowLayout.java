package com.zhengsr.ariesuilib.wieght.flow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhengsr.ariesuilib.wieght.flow.FlowLayout;

/**
 * @auther by zhengshaorui on 2019/12/25
 * describe: 标签类型的flowlayout，这里继承flowlayout，
 * 测试就不用自己去测量了，只需要关注数据即可
 */
public class TagFlowLayout extends FlowLayout {
    private FlowAdapter mAdapter;

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(FlowAdapter adapter) {
        mAdapter = adapter;
        notifyDataChanged();

    }

    private void notifyDataChanged() {
        FlowAdapter adapter = mAdapter;
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            View view = adapter.getView(LayoutInflater.from(getContext()), this, i);
            adapter.bindView(view, i);
            addView(view);
        }
    }

}
