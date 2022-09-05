package com.skyward.drag.dragimp;

import android.graphics.PointF;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.skyward.drag.DragInfo;
import com.skyward.drag.DragListenerDispatcher;
import com.skyward.drag.DragNotifier;

import java.util.Objects;

import static java.lang.Float.MIN_VALUE;

public class RecyclerDragListenerImp extends DragListenerDispatcher<RecyclerView, DragInfo> {

    private DragNotifier notifier;
    private final PointF lastTouchPoint = new PointF(); // used to create ShadowBuilder
    private final PointF nextMoveTouchPoint = new PointF(MIN_VALUE, MIN_VALUE);
    private long draggingId = RecyclerView.NO_ID;
    @Nullable
    private DragInfo lastDragInfo;
    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;

    public RecyclerDragListenerImp(RecyclerView recyclerView, DragNotifier notifier){
        super(recyclerView);
        this.notifier = notifier;
        bindTouchPoint(recyclerView);
    }

    private void bindTouchPoint(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                lastTouchPoint.set(e.getX(), e.getY());
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                recyclerView.post(() -> handleScroll(recyclerView));
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                scrollState = newState;
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        handleScroll(recyclerView);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    @Override
    public PointF getLastTouchPoint() {
        return new PointF(lastTouchPoint.x, lastTouchPoint.y);
    }


    @Override
    public boolean onDragPrepare(DragInfo dragInfo, RecyclerView recyclerView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(recyclerView.getTag(), dragInfo.pageIndex);
        }else {
            throw new IllegalStateException("the OS version must >= KITKAT (19)");
        }
    }

    @Override
    public void onDragStart(DragInfo dragInfo, RecyclerView recyclerView) {
        final long itemId = dragInfo.itemId;
        draggingId = itemId;
        int adapterPosition = recyclerView.findViewHolderForItemId(itemId).getAdapterPosition();
        notifier.onDragStart(adapterPosition, dragInfo.draggingView);
    }

    @Override
    public void onDragEnd(final DragInfo dragInfo, final RecyclerView recyclerView) {
        draggingId = RecyclerView.NO_ID;
        lastDragInfo = null;

        final long itemId = dragInfo.itemId;
        // queue up the show animation until after all move animations are finished
        recyclerView.getItemAnimator().isRunning(
                () -> {
                    int position = notifier.getPositionForId(itemId);

                    RecyclerView.ViewHolder vh = recyclerView.findViewHolderForItemId(itemId);
                    if (vh != null && vh.getAdapterPosition() != position) {
                        // if positions don't match, there's still an outstanding move animation
                        // so we try to reschedule the notifyItemChanged until after that
                        recyclerView.post(() -> recyclerView.getItemAnimator().isRunning(
                                () -> {
                                    notifier.onDragEnd(notifier.getPositionForId(itemId), dragInfo.draggingView);
                                    dragInfo.reset();
                                }));
                    } else {
                        recyclerView.post(() -> {
                            notifier.onDragEnd(notifier.getPositionForId(itemId), dragInfo.draggingView);
                            dragInfo.reset();
                        });
                    }
                });
    }


    @Override
    public void onDragLocation(DragInfo dragInfo, final RecyclerView recyclerView) {
        final long itemId = dragInfo.itemId;
        final float x = dragInfo.x;
        final float y = dragInfo.y;

//        final float x = dragInfo.dragX - dragInfo.shadowTouchPoint.x + dragInfo.shadowSize.x / 2f;
//        final float y = dragInfo.dragY - dragInfo.shadowTouchPoint.y + dragInfo.shadowSize.y / 2f;

        int fromPosition = notifier.getPositionForId(itemId);
        int toPosition = -1;

        View child = recyclerView.findChildViewUnder(x, y);
        if (child != null) {
            toPosition = recyclerView.getChildViewHolder(child).getAdapterPosition();
        }

        if (toPosition >= 0 && fromPosition != toPosition) {
            RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();

            boolean scheduleNextMove = nextMoveTouchPoint.equals(MIN_VALUE, MIN_VALUE);
            nextMoveTouchPoint.set(x, y);

            if (scheduleNextMove)
                animator.isRunning(() -> {
                    if (nextMoveTouchPoint.equals(MIN_VALUE, MIN_VALUE)) { return; }

                    final int fromPosition1 = notifier.getPositionForId(itemId);

                    View child1 = recyclerView
                            .findChildViewUnder(nextMoveTouchPoint.x, nextMoveTouchPoint.y);
                    if (child1 != null) {
                        final int toPosition1 =
                                recyclerView.getChildViewHolder(child1).getAdapterPosition();
                        recyclerView.post(() -> notifier.onMove(fromPosition1, toPosition1));
                    }

                    // reset so we know to schedule listener again next time
                    clearMove();
                });
        }

        lastDragInfo = dragInfo;
    }

    @Override
    public void onDragEnter(DragInfo dragInfo, RecyclerView v) {
        draggingId = dragInfo.itemId;
        notifier.onDragEnter(notifier.getPositionForId(dragInfo.itemId), dragInfo.draggingView);
    }


    @Override
    public void onDragExit(DragInfo dragInfo, RecyclerView v) {
        draggingId = RecyclerView.NO_ID;
        notifier.onDragExit(notifier.getPositionForId(dragInfo.itemId), dragInfo.draggingView);
    }

    @Override
    public void onDrop(DragInfo dragInfo, RecyclerView v) {
        notifier.onDrop(dragInfo.itemId, dragInfo.draggingView);
    }

    @Override
    public boolean acceptDrop(DragInfo dragInfo, RecyclerView v) {
        return true;
    }

    @Override
    public long getDraggingId() { return draggingId; }

    @Override
    public void clearMove() {
        nextMoveTouchPoint.set(MIN_VALUE, MIN_VALUE);
    }

    private void handleScroll(RecyclerView rv) {
        if (scrollState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }
        clearMove();

        //handle long vertical or horizontal scroll for that is not Grid View
       /* if (rv.getLayoutManager().canScrollHorizontally()) {
            if (rv.canScrollHorizontally(-1) && dragInfo.shouldScrollLeft()) {
                rv.scrollBy(-SCROLL_AMOUNT, 0);
                clearMove();
            } else if (rv.canScrollHorizontally(1) && dragInfo.shouldScrollRight(rv.getWidth())) {
                rv.scrollBy(SCROLL_AMOUNT, 0);
                clearMove();
            }
        } else if (rv.getLayoutManager().canScrollVertically()) {
            if (rv.canScrollVertically(-1) && dragInfo.shouldScrollUp()) {
                rv.scrollBy(0, -SCROLL_AMOUNT);
                clearMove();
            } else if (rv.canScrollVertically(1) && dragInfo.shouldScrollDown(rv.getHeight())) {
                rv.scrollBy(0, SCROLL_AMOUNT);
                clearMove();
            }
        }*/
    }

    @Override
    public void onPageTransfer(DragInfo lastInfo, DragInfo newInfo) {
        notifier.onPageTransfer(lastInfo, newInfo);
    }
}
