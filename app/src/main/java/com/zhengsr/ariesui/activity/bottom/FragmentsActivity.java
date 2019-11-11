package com.zhengsr.ariesui.activity.bottom;

import android.os.Bundle;
import android.view.View;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesuilib.wieght.bottom.CusBottomLayout;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;


public class FragmentsActivity extends SupportActivity implements CusBottomLayout.IBottomClickListener {
    private static final String TAG = "FragmentsActivity";


    private SupportFragment[] mFragments = new SupportFragment[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        CusBottomLayout layout = findViewById(R.id.bottom_ly);
        layout.setBottomClickListener(this);

        SupportFragment firstFragment = findFragment(TestFragment.class);
        if (firstFragment == null){
            mFragments[0] = TestFragment.newInstance("首页");
            mFragments[1] = TestFragment.newInstance("通讯录");
            mFragments[2] = TestFragment.newInstance("活动");
            mFragments[3] = TestFragment.newInstance("发现");
            mFragments[4] = TestFragment.newInstance("分类");

            getSupportDelegate().loadMultipleRootFragment(R.id.content_ly, 0,
                    mFragments);
        }else{
            //切换屏幕导致的问题，自己去弄了
        }


    }



    @Override
    public void onBottomClick(View view, int curPosition, int prePosition) {
        if (curPosition != prePosition) {
            showHideFragment(mFragments[curPosition], mFragments[prePosition]);
        }
    }
}
