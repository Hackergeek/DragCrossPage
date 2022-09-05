package com.skyward.views;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

import com.skyward.ViewPagerHelper;

public class RecyclerViewPager<T> extends ViewPager {
    protected ViewPagerHelper<T> viewPagerHelper;

    public RecyclerViewPager(Context context) {
        super(context);
    }

    public RecyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPagerHelper(ViewPagerHelper<T> viewPagerHelper) {
        this.viewPagerHelper = viewPagerHelper;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (viewPagerHelper != null) {
            viewPagerHelper.release();
        }
        super.onDetachedFromWindow();
    }
}
