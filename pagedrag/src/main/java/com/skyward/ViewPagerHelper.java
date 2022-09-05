package com.skyward;

public interface ViewPagerHelper<T> extends IRelease {
    T getCurrentItem(int position);
}