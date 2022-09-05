package com.skyward.drag.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.util.AttributeSet;

import com.skyward.drag.adapter.BaseGridPagerAdapter;
import com.skyward.drag.adapter.BasePagerAdapter;

public class RecycleViewPager extends BaseViewPager {
    public RecycleViewPager(@NonNull Context context) {
        super(context);
    }

    public RecycleViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@link BaseGridPagerAdapter}
     */
    public void release() {
        PagerAdapter adapter = getAdapter();
        if(adapter instanceof BasePagerAdapter){
            ((BasePagerAdapter) adapter).release();
        }
        removeAllViews();
    }

    public void restore() {
        PagerAdapter adapter = getAdapter();
        if(adapter instanceof BasePagerAdapter) {
            ((BasePagerAdapter) adapter).setContext(getContext());
            adapter.notifyDataSetChanged();
        }
    }
}
