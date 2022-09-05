package com.skyward.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skyward.adapter.DragPageAdapter;
import com.skyward.bean.PageData;
import com.skyward.pagedragframe.R;
import com.skyward.test.bean.UserData;
import com.bumptech.glide.Glide;

import java.util.List;

public class PictureAdapter extends DragPageAdapter<UserData> {
    public PictureAdapter(Context context, PageData<UserData> pageData) {
        super(context, pageData, true);
    }

    @Override
    public ItemAdapter generateItemRecyclerAdapter(List<UserData> pageData, int pageIndex) {
        return new ItemAdapter(pageData, pageIndex);
    }

    private class ItemAdapter extends ItemDragAdapter<ItemViewHolder> {

        ItemAdapter(List<UserData> list, int pageIndex) {
            super(list, pageIndex);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drag_picture, parent, false);
            return new ItemViewHolder(inflate);
        }

        @Override
        public void onBindItemViewHolder(ItemViewHolder holder, int position) {
            UserData testBean = data.get(position);
            Glide.with(holder.backView).load(testBean.imgUrl).into(holder.backView);
            holder.textView.setText(testBean.name);
        }

        @Override
        public void onBindItemViewHolder(ItemViewHolder holder, int position, List<Object> payloads) {
            if (payloads != null && !payloads.isEmpty()) {
                UserData testBean = (UserData) payloads.get(0);
                Glide.with(holder.backView).load(testBean.imgUrl).into(holder.backView);
                holder.textView.setText(testBean.name);
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

        private final ImageView backView;
        private final TextView textView;

        ItemViewHolder(View itemView) {
            super(itemView);
            backView = itemView.findViewById(R.id.recycler_item);
            textView = itemView.findViewById(R.id.tv_test);
        }
    }
}
