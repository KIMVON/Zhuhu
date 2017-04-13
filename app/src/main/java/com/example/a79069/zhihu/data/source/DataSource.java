package com.example.a79069.zhihu.data.source;


import com.example.a79069.zhihu.data.AllNewsComment;
import com.example.a79069.zhihu.data.FavoriteNews;
import com.example.a79069.zhihu.data.NewsComment;
import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.NewsTheme;

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

    interface ImageURLCallback {
        void onSuccess(String address);

        void onFailed();
    }


    interface NewsSimpleListCallback {
        void onSuccess(NewsSimpleList newsSimpleList);

        void onFailed();
    }

    interface NewsDetailCallback {
        void onSuccess(NewsDetail newsDetail);

        void onFailed();
    }

    interface NewsCommentsCallback {
        void onSuccess(List<NewsComment> newsCommentList);

        void onFailed();
    }

    interface FavoritesNewsCallback {
        void onSuccess(List<FavoriteNews> favoriteNewsList);

        void onFailed();
    }

    interface NewsThemeCallback{
        void onSuccess(List<NewsTheme> newsThemeList);

        void onFailed();
    }

    interface NewsThemeContentCallback {
        void onSuccess(List<NewsSimple> newsSimpleList);

        void onFailed();
    }


    interface addFavoriteNewsCallback {
        void onSuccess();

        void onGiveUp();

        void onFailed();
    }


    /**
     * 获取新闻列表
     *
     * @param callback
     */
    void getNews(String address, NewsSimpleListCallback callback);


    void getHotNewsList(final NewsSimpleListCallback callback);


    /**
     * 获取详细页面
     *
     * @param newsId
     * @param callback
     */
    void getNewsDetail(String newsId, NewsDetailCallback callback);


    /**
     * 获取新闻主题
     * @param callback
     */
    void getNewsTheme(NewsThemeCallback callback);


    /**
     * 获取详细的主题新闻内容（List）
     * @param address
     * @param contentCallback
     */
    void getNewsThemeConent(String address , NewsThemeContentCallback contentCallback);



    /**
     * 获取我的收藏
     *
     * @param callback
     */
    void getFavoritesList(FavoritesNewsCallback callback);


    /**
     * 添加到我的收藏
     *
     * @param address
     * @param title
     * @param callback
     */
    void addFavoriteNews(String address, String title, addFavoriteNewsCallback callback);


    /**
     * 获取所有评论（长短评论）
     *
     * @param address
     * @param callback
     */
    void getNewsComments(String address, NewsCommentsCallback callback);
}
