package com.zhengsr.ariesui.activity.bottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zhengshaorui on 2018/5/3.
 */

public class TestFragment extends SupportFragment {
    public static final String CUSKEY = "cuskey";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        Bundle bundle = getArguments();
        if (bundle != null){
            String title = bundle.getString(CUSKEY);
            textView.setText(title);
        }

        return textView;
    }

    public static TestFragment newInstance(String key) {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        args.putString(CUSKEY,key);
        fragment.setArguments(args);
        return fragment;
    }

}
