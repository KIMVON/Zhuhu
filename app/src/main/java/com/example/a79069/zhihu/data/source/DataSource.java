package com.example.a79069.zhihu.data.source;


import com.example.a79069.zhihu.data.AllNewsComment;
import com.example.a79069.zhihu.data.NewsComment;
import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimpleList;

import java.util.List;

import okhttp3.Callback;

/**
 * Created by 79069 on 2017/3/23.
 */

public interface DataSource {
    interface JSONCallback {
        void onSuccess(String jsonData);

        void onFailed();
    }

    interface ImageURLCallback{
        void onSuccess(String address);

        void onFailed();
    }


    interface NewsSimpleListCallback{
        void onSuccess(NewsSimpleList newsSimpleList);

        void onFailed();
    }

    interface NewsDetailCallback{
        void onSuccess(NewsDetail newsDetail);

        void onFailed();
    }

    interface NewsCommentsCallback{
        void onSuccess(List<NewsComment> newsCommentList);

        void onFailed();
    }




    /**
     * 获取新闻列表
     * @param callback
     */
    void getNews(String address , NewsSimpleListCallback callback);


    void getHotNewsList(final NewsSimpleListCallback callback);


    /**
     * 获取详细页面
     * @param newsId
     * @param callback
     */
    void getNewsDetail(String newsId , NewsDetailCallback callback);


    /**
     * 获取所有评论（长短评论）
     * @param address
     * @param callback
     */
    void getNewsComments(String address , NewsCommentsCallback callback);
}
