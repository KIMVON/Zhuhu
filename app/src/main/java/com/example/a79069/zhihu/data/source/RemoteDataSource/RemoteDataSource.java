package com.example.a79069.zhihu.data.source.RemoteDataSource;

import android.util.Log;

import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.DataSource;
import com.example.a79069.zhihu.util.HttpUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import static android.content.ContentValues.TAG;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 79069 on 2017/3/23.
 */

public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    private RemoteDataSource() {

    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }


    /**
     * 获取新闻列表
     *
     * @param callback
     */
    @Override
    public void getNews(String address, final NewsSimpleListCallback callback) {
        checkNotNull(callback);

        HttpUtil.sendRequestWithHttpURLConntection(address, new JSONCallback() {
            @Override
            public void onSuccess(String jsonData) {
                callback.onSuccess(parseJSONNewsList(jsonData));
            }

            @Override
            public void onFailed() {

            }
        });
    }


    /**
     * 获取热点消息
     *
     * @param callback
     */
    @Override
    public void getHotNewsList(final NewsSimpleListCallback callback) {
        checkNotNull(callback);

        HttpUtil.sendRequestWithHttpURLConntection("http://news-at.zhihu.com/api/3/news/hot", new JSONCallback() {
            @Override
            public void onSuccess(String jsonData) {

                callback.onSuccess(parseHotNewsList(jsonData));
            }

            @Override
            public void onFailed() {

            }
        });
    }


    /**
     * 获取详细页面
     *
     * @param address
     * @param callback
     */
    @Override
    public void getNewsDetail(String address, final NewsDetailCallback callback) {
        checkNotNull(address , "该地址为null");
        checkNotNull(callback);

        HttpUtil.sendRequestWithHttpURLConntection(address, new JSONCallback() {
            @Override
            public void onSuccess(String jsonData) {
                callback.onSuccess(parseNewsDetail(jsonData));
            }

            @Override
            public void onFailed() {

            }
        });
    }


    /**
     * 解析JSON
     *
     * @param jsonData
     * @return
     */
    public NewsSimpleList parseJSONNewsList(String jsonData) {
        NewsSimpleList newsSimpleList = new NewsSimpleList();
        List<NewsSimple> newsSimples = newsSimpleList.getNewsSimpleList();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            newsSimpleList.setDate(jsonObject.getString("date"));
            JSONArray jsonArray = jsonObject.getJSONArray("stories");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);


                String id = object.getString("id");
                final String imagesURL = object.getString("images");
                String title = object.getString("title");


                NewsSimple newsSimple = new NewsSimple();
                newsSimple.setTitle(title);
                newsSimple.setId(id);
                newsSimple.setImage(imagesURL);

                newsSimples.add(newsSimple);

            }
            return newsSimpleList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NewsSimpleList parseHotNewsList(String jsonData) {
        NewsSimpleList newsSimpleList = new NewsSimpleList();
        List<NewsSimple> newsSimples = newsSimpleList.getNewsSimpleList();


        try {
            JSONObject jsonObjectAll = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObjectAll.getJSONArray("recent");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                NewsSimple newsSimple = new NewsSimple();
                newsSimple.setTitle(jsonObject.getString("title"));
                newsSimple.setId(jsonObject.getString("news_id"));
                newsSimple.setImage("thumbnail");
                newsSimple.setShareUrl("url");

                newsSimples.add(newsSimple);
            }

            return newsSimpleList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NewsDetail parseNewsDetail(String jsonData) {
        NewsDetail newsDetail = new NewsDetail();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            newsDetail.setShareURL(jsonObject.getString("share_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsDetail;
    }
}
