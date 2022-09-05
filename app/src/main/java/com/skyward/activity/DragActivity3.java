package com.skyward.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.skyward.bean.PageData;
import com.skyward.views.DragViewPager;
import com.skyward.pagedragframe.R;
import com.skyward.test.adapter.PictureAdapter;
import com.skyward.test.bean.UserData;
import com.skyward.test.bean.UserModel;

import java.util.List;
import java.util.Random;

public class DragActivity3 extends AppCompatActivity {

    Random r = new Random();
    private List<UserData> dataArrayList;
    private PageData<UserData> pageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag2);
        initData();

        DragViewPager vp = (DragViewPager) this.findViewById(R.id.vp);

        PictureAdapter myAdapter = new PictureAdapter(this, pageData);
        //边界宽度定义
        vp.setLeftOutZone(100);
        vp.setRightOutZone(100);
        vp.setViewPagerHelper(myAdapter);
        vp.setAdapter(myAdapter);
    }

    private void initData() {
        dataArrayList = UserModel.get().getUsers();
        pageData = new PageData<UserData>(2, 5, dataArrayList) {
            @Override
            public boolean areItemsTheSame(UserData oldData, UserData newData) {
                return oldData.hashCode() == newData.hashCode();
            }

            @Override
            public boolean areContentsTheSame(UserData oldData, UserData newData) {
                return oldData.equals(newData);
            }

            @Override
            public Object getChangePayload(UserData oldData, UserData newData) {
                return newData;
            }

            @Override
            public int getDataPosition(List<UserData> allData, UserData newData) {
                return allData.indexOf(newData);
            }
        };
    }

    public void insert(View view) {
        final int pos = pageData.getAllData().size();
        pageData.insertData(0, new UserData("" + pos));
    }

    public void remove(View view) {
        List<UserData> allData = pageData.getAllData();
        UserData remove = allData.get(r.nextInt(allData.size()));
        pageData.removeData(remove);
    }

    public void update(View view) {
        UserData testBean = pageData.getAllData().get(1);

        testBean.imgUrl = "https://img1.baidu.com/it/u=171792433,926642297&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=711";
        pageData.updateData(testBean, testBean);

//        int dataIndex = 100 + r.nextInt(100);
//        testBean.dataIndex = dataIndex;
//        pageData.updateData(testBean, dataIndex);
    }
}
