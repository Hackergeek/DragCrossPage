package com.skyward.adapter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.skyward.ViewPagerHelper;
import com.skyward.bean.DragInfo;
import com.skyward.bean.PageData;
import com.skyward.shadowbuilder.ScaleViewShadowBuilder;
import com.skyward.views.DragRecyclerView;
import com.skyward.views.DragViewPager;

import java.util.List;

public abstract class DragPageAdapter<Data> extends RecyclerViewPagerAdapter<Data> implements ViewPagerHelper<DragViewPager.DragListener> {
    public DragPageAdapter(Context context, PageData<Data> pageData, boolean initViewsImmediately) {
        super(context, pageData, initViewsImmediately);
    }

    @NonNull
    @Override
    public RecyclerView onCreatePage(FrameLayout frameLayout, int pageIndex) {
        Context context = frameLayout.getContext();
        DragRecyclerView dragRecyclerView = new DragRecyclerView(context);
        RecyclerView.Adapter adapter = generateItemRecyclerAdapter(mPageData.getPageData(pageIndex), pageIndex);
        dragRecyclerView.setAdapter(adapter);
        if (adapter instanceof ItemDragAdapter) {
            dragRecyclerView.setRecyclerDragListener((ItemDragAdapter) adapter);
        }
        dragRecyclerView.setLayoutManager(generateItemLayoutManager(context, mPageData, pageIndex));
        return dragRecyclerView;
    }

    public abstract class ItemDragAdapter<VH extends DragViewHolder> extends ItemPageAdapter<VH> implements DragRecyclerView.RecyclerDragListener<Data> {
        private long draggingItemId = RecyclerView.NO_ID;

        public ItemDragAdapter(List<Data> list, int pageIndex) {
            super(list, pageIndex);
            setHasStableIds(true);
        }

        @Override
        public final long getItemId(int position) {
            return getStableItemId(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public final void onBindViewHolder(VH holder, int position) {
            if (getItemId(position) == draggingItemId) {
                holder.itemView.setVisibility(View.INVISIBLE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
            onBindItemViewHolder(holder, position);
        }

        @Override
        public void onDragStart(final long draggingItemId, final RecyclerView recyclerView) {
            ItemDragAdapter.this.draggingItemId = draggingItemId;

            recyclerView.post(() -> {
                final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(draggingItemId);
                if (viewHolder != null) {
                    viewHolder.itemView.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public void onDragEnter(long draggingItemId, RecyclerView currentRecyclerView, RecyclerView lastRecyclerView, int vectorOutZone, int[] coordinate) {
            ItemDragAdapter.this.draggingItemId = draggingItemId;
        }

        @Override
        public void onDragExit(long draggingItemId, RecyclerView currentRecyclerView, RecyclerView nextRecyclerView, int vectorOutZone, int[] coordinate) {
            ItemDragAdapter.this.draggingItemId = RecyclerView.NO_ID;
        }

        @Override
        public void onDragEnd(final long draggingItemId, final RecyclerView recyclerView, int[] coordinate) {
            ItemDragAdapter.this.draggingItemId = RecyclerView.NO_ID;

            recyclerView.post(() -> {
                final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(draggingItemId);
                if (viewHolder != null) {
                    viewHolder.itemView.setVisibility(View.VISIBLE);
                }
            });
            mPageData.updateAllDataByPage();
        }

        @Override
        public void onMove(int fromPosition, int toPosition) {
            if (fromPosition == RecyclerView.NO_POSITION || toPosition == RecyclerView.NO_POSITION) {
                return;
            }
            if (fromPosition == toPosition) {
                return;
            }
            mPageData.onMove(pageIndex, fromPosition, toPosition);
        }

        @Override
        public Data removePosition(int position) {
            return mPageData.removePosition(pageIndex, position);
        }

        @Override
        public void addItemToPosition(int position, Data object) {
            mPageData.addItemToPosition(pageIndex, position, object);
        }

        @Override
        public int getLastAdapterPosition() {
            return data.size() - 1;
        }

        /**
         * @see #setHasStableIds(boolean)
         */
        public abstract long getStableItemId(int position);

        public abstract void onBindItemViewHolder(VH holder, int position);
    }

    @Nullable
    @Override
    public DragRecyclerView getCurrentItem(int position) {
        return position >= views.size() ? null : (DragRecyclerView) views.get(position).getChildAt(0);
    }

    public static class DragViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private View.OnLongClickListener onLongClickListener;
        public DragViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (onLongClickListener != null) {
                if (onLongClickListener.onLongClick(v)) {
                    return true;
                }
            }
            ViewParent parent = v.getParent();
            if (parent instanceof DragRecyclerView) {
                DragRecyclerView dragRecyclerView = (DragRecyclerView) parent;
                final PointF lastTouchPoint = dragRecyclerView.getLastTouchPoint();
                int x = (int) (lastTouchPoint.x - v.getX());
                int y = (int) (lastTouchPoint.y - v.getY());
                View.DragShadowBuilder shadowBuilder = makeDragShadowBuilder(v, new Point(x, y));
                DragInfo dragInfo = makeDragInfo(getItemId(), shadowBuilder);
                v.startDrag(null, shadowBuilder, dragInfo, 0);
            }
            return false;
        }

        public DragInfo makeDragInfo(long itemId, View.DragShadowBuilder shadowBuilder) {
            Point shadowSize = new Point();
            Point shadowTouchPoint = new Point();
            shadowBuilder.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
            DragInfo dragInfo = new DragInfo();
            dragInfo.itemId = itemId;
            dragInfo.shadowSize.set(shadowSize.x, shadowSize.y);
            dragInfo.shadowTouchPoint.set(shadowTouchPoint.x, shadowTouchPoint.y);
            return dragInfo;
        }

        public View.DragShadowBuilder makeDragShadowBuilder(View itemView, Point touchPoint) {
            return new ScaleViewShadowBuilder(itemView, touchPoint);
        }

        public void setItemOnLongClickListener(View.OnLongClickListener listener) {
            onLongClickListener = listener;
        }
    }

}
