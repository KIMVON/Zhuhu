package com.example.a79069.zhihu.data.source;

import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.LocalDataSource.LocalDataSource;
import com.example.a79069.zhihu.data.source.RemoteDataSource.RemoteDataSource;
import com.example.a79069.zhihu.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 79069 on 2017/3/23.
 */

public class AppRepository implements DataSource {
    private static AppRepository INSTANCE;

    private DataSource mLocalDataSource;
    private DataSource mRemoteDataSource;

    private AppRepository(DataSource localDataSource, DataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static AppRepository getInstance(DataSource localDataSource, DataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }


    /**
     * 获取新闻列表
     * @param callback
     */
    @Override
    public void getNews(String address , final NewsSimpleListCallback callback) {
        mRemoteDataSource.getNews(address , new NewsSimpleListCallback() {
            @Override
            public void onSuccess(NewsSimpleList newsSimpleList) {

                callback.onSuccess(newsSimpleList);

            }

            @Override
            public void onFailed() {
                callback.onFailed();
            }
        });
    }

    /**
     * 获取详细页面
     * @param newsId
     * @param callback
     */
    @Override
    public void getNewsDetail(String newsId, final NewsDetailCallback callback) {
        mRemoteDataSource.getNewsDetail(newsId, new NewsDetailCallback() {
            @Override
            public void onSuccess(NewsDetail newsDetail) {

                callback.onSuccess(newsDetail);
            }

            @Override
            public void onFailed() {

            }
        });
    }

}
