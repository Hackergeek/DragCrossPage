package com.skyward.test.adapter;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyward.pagedragframe.R;
import com.skyward.drag.adapter.BaseDragPageAdapter;
import com.skyward.drag.DragInfo;
import com.skyward.drag.DragListenerDispatcher;
import com.skyward.test.bean.TestBean;

import java.util.List;

public class GridPagerAdapter extends BaseDragPageAdapter<TestBean> {

    public GridPagerAdapter(Context context, List<TestBean> list, DragListenerDispatcher<ViewPager, DragInfo> pagerListener) {
        super(context, 2, 5, list, pagerListener);
    }

    @Override
    public BaseDragPageAdapter.DragAdapter generateItemAdapter(List<TestBean> data, int pageIndex) {
        return new ItemAdapter(data, pageIndex);
    }

    class ItemAdapter extends DragAdapter<PageViewHolder> {

        public ItemAdapter(List<TestBean> list, int pageIndex) {
            super(list, pageIndex);
        }

        @Override
        public int getPositionForId(long id) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i).hashCode() == id) {
                    return i;
                }
            }
            return RecyclerView.NO_POSITION;
        }

        @Override
        public long getItemId(int position) {
            return getData().get(position).hashCode();
        }

        @Override
        public PageViewHolder onCreateViewHolder(ViewGroup parent, int viewType, int pageIndex) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false);

            return new PageViewHolder(inflate, pageIndex);
        }

        @Override
        public void onBindViewHolder(PageViewHolder holder, int position, int pageIndex) {
            holder.itemView.findViewById(R.id.recycler_item).setBackgroundColor(
                    getData().get(position).color);
            ((TextView)holder.itemView.findViewById(R.id.tv_test))
                    .setText("" + getData().get(position).dataIndex);
        }
    }

}
