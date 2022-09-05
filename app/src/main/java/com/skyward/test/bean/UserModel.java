package com.skyward.test.bean;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private static final UserModel sInstance = new UserModel();

    private UserModel() {
        initUsers();
    }

    public static UserModel get() {
        return sInstance;
    }


    private List<UserData> users = new ArrayList<>();

    public void initUsers() {
        users.clear();
        for (int i = 0; i < 30; i++) {
            users.add(new UserData("https://img2.baidu.com/it/u=645336210,1332246460&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=1082",
                    i+""));
        }
    }

    public List<UserData> getUsers() {
        return users;
    }
}
