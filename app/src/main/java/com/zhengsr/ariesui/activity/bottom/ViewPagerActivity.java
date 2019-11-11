package com.zhengsr.ariesui.activity.bottom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhengsr.ariesui.R;
import com.zhengsr.ariesuilib.wieght.bottom.CusBottomLayout;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;

public class ViewPagerActivity extends SupportActivity implements CusBottomLayout.IBottomClickListener {
    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();
    private CusBottomLayout mCusBottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mViewPager = findViewById(R.id.cus_viewpager);
        mCusBottomLayout = (CusBottomLayout)findViewById(R.id.bottom_ly);
        mCusBottomLayout.setBottomClickListener(this);
        initData();
    }

    private void initData() {

        mFragments.add(TestFragment.newInstance("首页"));
        mFragments.add(TestFragment.newInstance("通讯录"));
        mFragments.add(TestFragment.newInstance("发现"));
        mFragments.add(TestFragment.newInstance("分类"));

        mViewPager.setAdapter(new cusPagerAdapter(getSupportFragmentManager()));

        mCusBottomLayout.addViewPager(mViewPager);
    }



    @Override
    public void onBottomClick(View view, int curPosition, int prePosition) {
        mViewPager.setCurrentItem(curPosition);
    }


    class cusPagerAdapter extends FragmentPagerAdapter {

        public cusPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
