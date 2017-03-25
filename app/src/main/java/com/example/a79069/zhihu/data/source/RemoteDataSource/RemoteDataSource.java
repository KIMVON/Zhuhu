package com.example.a79069.zhihu.data.source.RemoteDataSource;

import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.DataSource;
import com.example.a79069.zhihu.util.HttpUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by 79069 on 2017/3/23.
 */

public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    private RemoteDataSource(){

    }

    public static RemoteDataSource getInstance(){
        if (INSTANCE == null){
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }


    /**
     * 获取新闻列表
     * @param callback
     */
    @Override
    public void getNews(final NewsSimpleListCallback callback) {
        checkNotNull(callback);

        HttpUtil.sendRequestWithHttpURLConntection("http://news-at.zhihu.com/api/4/news/latest", new JSONCallback() {
            @Override
            public void onSuccess(String jsonData) {
                callback.onSuccess(parseJSONString(jsonData));
            }

            @Override
            public void onFailed() {

            }
        });
    }


    /**
     * 获取详细页面
     * @param newsId
     * @param callback
     */
    @Override
    public void getNewsDetail(String newsId ,JSONCallback callback) {
        checkNotNull(newsId);
        checkNotNull(callback);

        HttpUtil.sendRequestWithHttpURLConntection("http://news-at.zhihu.com/api/4/news/"+newsId , callback);
    }


    /**
     * 解析JSON
     * @param jsonData
     * @return
     */
    public NewsSimpleList parseJSONString(String jsonData) {
        NewsSimpleList newsSimpleList = new NewsSimpleList();
        List<NewsSimple> newsSimples = newsSimpleList.getNewsSimpleList();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            newsSimpleList.setDate(jsonObject.getString("date"));
            JSONArray jsonArray = jsonObject.getJSONArray("stories");

            for (int i=0 ; i < jsonArray.length() ; i++){
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


}
