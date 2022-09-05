package com.skyward.drag;

import android.graphics.Point;
import android.view.View;

public class DragInfo {

    /**
     * 拖动时，（手指/鼠标）的位置坐标
     */
    public float x = -1;
    public float y = -1;
    public long itemId = -1;
    /**
     * 被拖拽的View
     */
    public View draggingView;
    /**
     * 被拖拽的View位于ViewPager中第几页
     */
    public int pageIndex = -1;

    public Point shadowSize = new Point();
    public Point shadowTouchPoint = new Point();
    public DragInfo(){}
    public DragInfo(DragInfo other){
        x = other.x;
        y = other.y;
        itemId = other.itemId;
        pageIndex= other.pageIndex;
        draggingView = other.draggingView;
        shadowSize.set(other.shadowSize.x, other.shadowSize.y);
        shadowTouchPoint.set(other.shadowTouchPoint.x, other.shadowTouchPoint.y);
    }

    public void reset(){
        x = -1;
        y = -1;
        itemId = -1;
        pageIndex= -1;
        draggingView = null;
    }
}