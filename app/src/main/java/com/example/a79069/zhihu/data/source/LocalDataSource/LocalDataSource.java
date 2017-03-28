package com.example.a79069.zhihu.data.source.LocalDataSource;

import com.example.a79069.zhihu.data.source.DataSource;


/**
 * Created by 79069 on 2017/3/23.
 */

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;

    private LocalDataSource(){

    }

    public static LocalDataSource getInstance(){
        if (INSTANCE == null){
            INSTANCE = new LocalDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getNews(String address , NewsSimpleListCallback callback) {
        callback.onFailed();
    }

    @Override
    public void getNewsDetail(String newsId , NewsDetailCallback callback) {

    }

}
