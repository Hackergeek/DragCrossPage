package com.skyward.test.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyward.adapter.DragPageAdapter;
import com.skyward.bean.PageData;
import com.skyward.pagedragframe.R;
import com.skyward.test.bean.TestBean;

import java.util.List;

public class MyAdapter extends DragPageAdapter<TestBean> {
    public MyAdapter(Context context, PageData<TestBean> pageData) {
        super(context, pageData, true);
    }

    @Override
    public ItemAdapter generateItemRecyclerAdapter(List<TestBean> pageData, int pageIndex) {
        return new ItemAdapter(pageData, pageIndex);
    }

    private class ItemAdapter extends ItemDragAdapter<ItemViewHolder> {

        ItemAdapter(List<TestBean> list, int pageIndex) {
            super(list, pageIndex);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false);
            return new ItemViewHolder(inflate);
        }

        @Override
        public void onBindItemViewHolder(ItemViewHolder holder, int position) {
            TestBean testBean = data.get(position);
            holder.backView.setBackgroundColor(testBean.color);
            holder.textView.setText("" + testBean.dataIndex);
        }

        @Override
        public void onBindItemViewHolder(ItemViewHolder holder, int position, List<Object> payloads) {
            if (payloads != null && !payloads.isEmpty()) {
                Integer color = (Integer) payloads.get(0);
                holder.backView.setBackgroundColor(color);

//                Integer dataIndex = (Integer) payloads.get(0);
//                holder.textView.setText("" + dataIndex);
            } else {
                onBindViewHolder(holder, position);
            }
        }

        @Override
        public long getStableItemId(int position) {
            return data.get(position).id;
        }

        @Override
        public int getPositionForId(long itemId) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                int positionItemId = data.get(i).id;
                if (positionItemId == itemId) {
                    return i;
                }
            }
            return RecyclerView.NO_POSITION;
        }

    }

    private static class ItemViewHolder extends DragViewHolder {

        private final View backView;
        private final TextView textView;

        ItemViewHolder(View itemView) {
            super(itemView);
            backView = itemView.findViewById(R.id.recycler_item);
            textView = itemView.findViewById(R.id.tv_test);
        }
    }
}
