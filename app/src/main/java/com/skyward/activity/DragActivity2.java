package com.skyward.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.skyward.bean.PageData;
import com.skyward.views.DragViewPager;
import com.skyward.pagedragframe.R;
import com.skyward.test.bean.TestBean;
import com.skyward.test.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DragActivity2 extends AppCompatActivity {

    int[] color_array = {Color.DKGRAY, Color.YELLOW, Color.BLUE,
            Color.CYAN, Color.GRAY, Color.RED, Color.GREEN, Color.MAGENTA, Color.WHITE, Color.LTGRAY};
    Random r = new Random();
    private ArrayList<TestBean> testBeanList;
    private PageData<TestBean> pageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag2);
        initData();

        DragViewPager vp = (DragViewPager) this.findViewById(R.id.vp);

        MyAdapter myAdapter = new MyAdapter(this, pageData);
        //边界宽度定义
        vp.setLeftOutZone(100);
        vp.setRightOutZone(100);
        vp.setViewPagerHelper(myAdapter);
        vp.setAdapter(myAdapter);
    }

    private void initData() {
        testBeanList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            testBeanList.add(new TestBean(color_array[r.nextInt(color_array.length)], i));
        }
        pageData = new PageData<TestBean>(2, 5, testBeanList) {
            @Override
            public boolean areItemsTheSame(TestBean oldData, TestBean newData) {
                return oldData.hashCode() == newData.hashCode();
            }

            @Override
            public boolean areContentsTheSame(TestBean oldData, TestBean newData) {
                return oldData.color == newData.color;
            }

            @Override
            public Object getChangePayload(TestBean oldData, TestBean newData) {
                return newData.color;
            }

            @Override
            public int getDataPosition(List<TestBean> allData, TestBean newData) {
                return allData.indexOf(newData);
            }
        };
    }

    public void insert(View view) {
        final int pos = pageData.getAllData().size();
        pageData.insertData(0, new TestBean(color_array[r.nextInt(color_array.length)], pos));
    }

    public void remove(View view) {
        List<TestBean> allData = pageData.getAllData();
        TestBean remove = allData.get(r.nextInt(allData.size()));
        pageData.removeData(remove);
    }

    public void update(View view) {
        TestBean testBean = pageData.getAllData().get(1);

        int newColor = color_array[r.nextInt(color_array.length)];
        testBean.color = newColor;
        pageData.updateData(testBean, newColor);

//        int dataIndex = 100 + r.nextInt(100);
//        testBean.dataIndex = dataIndex;
//        pageData.updateData(testBean, dataIndex);
    }
}
