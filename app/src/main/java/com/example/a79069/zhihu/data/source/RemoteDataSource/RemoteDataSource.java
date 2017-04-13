package com.example.a79069.zhihu.data.source.RemoteDataSource;

import android.util.Log;

import com.example.a79069.zhihu.data.AllNewsComment;
import com.example.a79069.zhihu.data.NewsComment;
import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.NewsTheme;
import com.example.a79069.zhihu.data.source.DataSource;
import com.example.a79069.zhihu.util.HttpUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
     * 获取照片地址
     *
     * @param address
     * @param callback
     */
    public void getLaunchImage(String address, ImageURLCallback callback) {


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
                callback.onFailed();
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
        checkNotNull(address, "该地址为null");
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
     * 获取新闻主题
     *
     * @param callback
     */
    @Override
    public void getNewsTheme(final NewsThemeCallback callback) {
        checkNotNull(callback, "getNewsTheme失败了");

        String address = "http://news-at.zhihu.com/api/4/themes";

        HttpUtil.sendRequestWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();

                callback.onSuccess(parseNewsTheme(jsonData));
            }
        });

    }

    @Override
    public void getNewsThemeConent(String address, final NewsThemeContentCallback contentCallback) {
        checkNotNull(address);
        checkNotNull(contentCallback);

        HttpUtil.sendRequestWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                contentCallback.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                contentCallback.onSuccess(parseNewsThemeContent(response.body().string()));
            }
        });
    }

    @Override
    public void getFavoritesList(FavoritesNewsCallback callback) {

    }

    @Override
    public void addFavoriteNews(String address, String title, addFavoriteNewsCallback callback) {

    }


    /**
     * 获取所有评论
     *
     * @param address
     * @param callback
     */
    @Override
    public void getNewsComments(String address, final NewsCommentsCallback callback) {
        checkNotNull(address);
        checkNotNull(callback);


        HttpUtil.sendRequestWithOkHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onSuccess(parseNewsComment(response.body().string()));
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


    /**
     * 解析热点消息的JSON
     *
     * @param jsonData
     * @return
     */
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
                newsSimple.setImage(jsonObject.getString("thumbnail"));

                newsSimple.setShareUrl("url");

                newsSimples.add(newsSimple);
            }

            return newsSimpleList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析详细页面JSON
     *
     * @param jsonData
     * @return
     */
    public NewsDetail parseNewsDetail(String jsonData) {
        NewsDetail newsDetail = new NewsDetail();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            newsDetail.setBody(jsonObject.getString("body"));

            newsDetail.setShareURL(jsonObject.getString("share_url"));

            newsDetail.setImageSource(jsonObject.getString("image_source"));

            newsDetail.setImageURL(jsonObject.getString("image"));

            newsDetail.setTitle(jsonObject.getString("title"));

            newsDetail.setId(jsonObject.getString("id"));

            newsDetail.setCssURL(jsonObject.getString("css").substring(2, jsonObject.getString("css").length() - 2).replaceAll("\\\\", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsDetail;
    }


    /**
     * 解析评论列表JSON
     *
     * @param jsonData
     * @return
     */
    private List<NewsComment> parseNewsComment(String jsonData) {
        List<NewsComment> newsCommentList = new ArrayList<>();
        NewsComment newsComment = null;
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray array = object.getJSONArray("comments");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                newsComment = new NewsComment();

                newsComment.setAuthor(jsonObject.getString("author"));
                newsComment.setContent(jsonObject.getString("content"));
                newsComment.setAvatar(jsonObject.getString("avatar"));
                newsComment.setTime(jsonObject.getString("time"));
                newsComment.setId(jsonObject.getString("id"));
                newsComment.setLikes(jsonObject.getInt("likes"));

                newsCommentList.add(newsComment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newsCommentList;
    }

    /**
     * 解析新闻主题
     *
     * @param jsonData
     * @return
     */
    private List<NewsTheme> parseNewsTheme(String jsonData) {
        List<NewsTheme> newsThemeList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray jsonArray = object.getJSONArray("others");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                NewsTheme newsTheme = new NewsTheme();

                newsTheme.setHeaderImage(jsonObject.getString("thumbnail"));
                newsTheme.setDescription(jsonObject.getString("description"));
                newsTheme.setName(jsonObject.getString("name"));
                newsTheme.setId(jsonObject.getString("id"));

                newsThemeList.add(newsTheme);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsThemeList;
    }


    /**
     * 解析新闻主题内容
     *
     * @param jsonData
     * @return
     */
    private List<NewsSimple> parseNewsThemeContent(String jsonData) {
        List<NewsSimple> newsSimpleList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray jsonArray = object.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                NewsSimple newsSimple = new NewsSimple();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("images")) {
                    newsSimple.setImage(jsonObject.getString("images"));
                }
                newsSimple.setId(jsonObject.getString("id"));
                newsSimple.setTitle(jsonObject.getString("title"));

                newsSimpleList.add(newsSimple);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsSimpleList;
    }
}
