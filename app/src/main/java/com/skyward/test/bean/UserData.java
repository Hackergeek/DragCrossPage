package com.skyward.test.bean;

import java.util.Objects;

public class UserData {
    public String imgUrl;

    public String name;
    public final int id;

    public UserData(String imgUrl, String name) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.id = hashCode();
    }

    public UserData(String name) {
        this("https://img2.baidu.com/it/u=645336210,1332246460&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=1082",
                name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(imgUrl, userData.imgUrl) && Objects.equals(name, userData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imgUrl, name);
    }
}
