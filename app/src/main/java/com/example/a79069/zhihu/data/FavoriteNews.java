package com.example.a79069.zhihu.data;

import org.litepal.crud.DataSupport;

/**
 * Created by 79069 on 2017/4/11.
 */

public class FavoriteNews extends DataSupport {
    private int id;

    private String address;

    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
