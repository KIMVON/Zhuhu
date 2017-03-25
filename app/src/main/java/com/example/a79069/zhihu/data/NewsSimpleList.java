package com.example.a79069.zhihu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 79069 on 2017/3/23.
 */

public class NewsSimpleList {
    private String date;

    private List<NewsSimple> NewsSimpleList = new ArrayList<>();



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NewsSimple> getNewsSimpleList() {
        return NewsSimpleList;
    }

    public void setNewsSimpleList(List<NewsSimple> newsSimpleList) {
        NewsSimpleList = newsSimpleList;
    }
}
