package com.example.a79069.zhihu.data.source;

import android.util.Log;

import com.example.a79069.zhihu.data.NewsComment;
import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.LocalDataSource.LocalDataSource;
import com.example.a79069.zhihu.data.source.RemoteDataSource.RemoteDataSource;
import com.example.a79069.zhihu.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

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
     * 获取热点消息
     * @param callback
     */
    @Override
    public void getHotNewsList(final NewsSimpleListCallback callback) {
        mRemoteDataSource.getHotNewsList(new NewsSimpleListCallback() {
            @Override
            public void onSuccess(NewsSimpleList newsSimpleList) {

                callback.onSuccess(newsSimpleList);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    /**
     * 获取详细页面
     * @param address
     * @param callback
     */
    @Override
    public void getNewsDetail(String address, final NewsDetailCallback callback) {
        mRemoteDataSource.getNewsDetail(address, new NewsDetailCallback() {
            @Override
            public void onSuccess(NewsDetail newsDetail) {
                callback.onSuccess(newsDetail);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    /**
     * 获取所有评论
     * @param address
     * @param callback
     */
    @Override
    public void getNewsComments(String address, final NewsCommentsCallback callback) {
        mRemoteDataSource.getNewsComments(address, new NewsCommentsCallback() {
            @Override
            public void onSuccess(List<NewsComment> newsCommentList) {
                callback.onSuccess(newsCommentList);
            }

            @Override
            public void onFailed() {

            }
        });
    }

}
