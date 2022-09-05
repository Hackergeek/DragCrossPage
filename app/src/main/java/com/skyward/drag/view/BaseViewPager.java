package com.skyward.drag.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

import com.skyward.drag.adapter.BasePagerAdapter;

public class BaseViewPager extends ViewPager {


    public BaseViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BaseViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(getAdapter() instanceof BasePagerAdapter){
            ((BasePagerAdapter) getAdapter()).release();
        }
    }

}
