package com.skyward.drag;

import static android.view.DragEvent.ACTION_DRAG_ENDED;
import static android.view.DragEvent.ACTION_DRAG_ENTERED;
import static android.view.DragEvent.ACTION_DRAG_EXITED;
import static android.view.DragEvent.ACTION_DRAG_LOCATION;
import static android.view.DragEvent.ACTION_DRAG_STARTED;
import static android.view.DragEvent.ACTION_DROP;

import android.graphics.PointF;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import java.lang.ref.WeakReference;

public abstract class DragListenerDispatcher<V extends View, T extends DragInfo> implements View.OnDragListener {
    protected final WeakReference<V> viewRef;
    protected DragManager mDragManager;
    public DragListenerDispatcher(V v){
        viewRef = new WeakReference<>(v);
    }

    public DragListenerDispatcher(V v, DragManager dragManager){
        viewRef = new WeakReference<>(v);
        this.mDragManager = dragManager;
    }

    public void attachDragManager(DragManager dragManager){
        this.mDragManager = dragManager;
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        if (view != viewRef.get() || !(event.getLocalState() instanceof DragInfo)) {
            return false;
        }

        T dragInfo = (T)event.getLocalState();
        V v = (V) view;

        if (!onDragPrepare(dragInfo, v)) {
            return false;
        }

        dragInfo.x = event.getX();
        dragInfo.y = event.getY();

        int action = event.getAction();
        Log.d("skyward", "onDrag: " + actionToString(event.getAction()));
        switch (action) {
            case ACTION_DRAG_STARTED:
                onDragStart(dragInfo, v);
                break;
            case ACTION_DRAG_ENDED:
                onDragEnd(dragInfo, v);
                break;
            case ACTION_DRAG_ENTERED:
                onDragEnter(dragInfo, v);
                break;
            case ACTION_DRAG_EXITED:
                onDragExit(dragInfo, v);
                break;
            case ACTION_DRAG_LOCATION:
                onDragLocation(dragInfo, v);
                break;
            case ACTION_DROP:
//                if (acceptDrop(dragInfo, v)) {
                    onDrop(dragInfo, v);
//                }
                break;
        }
        return true;
    }

    public static String actionToString(int action) {
        switch (action) {
            case ACTION_DRAG_STARTED:
                return "ACTION_DRAG_STARTED";
            case ACTION_DRAG_LOCATION:
                return "ACTION_DRAG_LOCATION";
            case ACTION_DROP:
                return "ACTION_DROP";
            case ACTION_DRAG_ENDED:
                return "ACTION_DRAG_ENDED";
            case ACTION_DRAG_ENTERED:
                return "ACTION_DRAG_ENTERED";
            case ACTION_DRAG_EXITED:
                return "ACTION_DRAG_EXITED";
        }
        return Integer.toString(action);
    }

    /**
     * need check param type
     * @param v your listener view
     */
    public abstract boolean onDragPrepare(T dragInfo, V v);

    /**
     * ACTION_DRAG_STARTED -> 开始
     * ACTION_DRAG_ENTERED 当（手指/鼠标）移动到屏幕内就会触发 ->
     * ACTION_DRAG_LOCATION （手指/鼠标）在屏幕中移动时会不断触发 ->
     * ACTION_DROP 当（手指/鼠标）离开屏幕就会触发->
     * ACTION_DRAG_ENDED 结束
     * 正常时间流转如上：
     * ACTION_DRAG_EXITED 当（手指/鼠标）移动到屏幕外就会触发
     * 已开始
     * @param dragInfo
     * @param v
     */
    public abstract void onDragStart(T dragInfo, V v);

    public abstract void onDragEnd(T dragInfo, V v);

    public abstract void onDrop(T dragInfo, V v);

    public abstract void onDragEnter(T dragInfo, V v);

    public abstract void onDragLocation(T dragInfo, V v);

    public abstract void onDragExit(T dragInfo, V v);

    public abstract boolean acceptDrop(T dragInfo, V v);

    public abstract long getDraggingId();

    public abstract PointF getLastTouchPoint();

    public abstract void clearMove();

    public abstract void onPageTransfer(T lastDragInfo, T dragInfo);
}
