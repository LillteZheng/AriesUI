package com.zhengsr.ariesui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesuilib.wieght.flow.FlowAdapter;
import com.zhengsr.ariesuilib.wieght.flow.FlowLayout;
import com.zhengsr.ariesuilib.wieght.flow.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowActivity extends AppCompatActivity {

    private TagFlowLayout mFlowLayout;

    private List<String> lists = Arrays.asList("android","java","python","android","java","python","android","java","python");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mFlowLayout = findViewById(R.id.flowlayout);
        mFlowLayout.setAdapter(new TabAdapater());

    }

    class TabAdapater extends FlowAdapter{

        @Override
        public int getItemCount() {
            return lists.size();
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup viewGroup, int position) {
            View view = inflater.inflate(R.layout.item_text_layout,viewGroup,false);
            return view;
        }

        @Override
        public void bindView(View view, int position) {
            TextView textView = view.findViewById(R.id.item_text);
            textView.setText(lists.get(position));
        }
    }

    public void add(View view) {
        Button button = new Button(this);
        ViewGroup.LayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setText("add");
        mFlowLayout.addView(button);
    }
}
