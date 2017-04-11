package com.example.a79069.zhihu.data.source.LocalDataSource;

import android.util.Log;

import com.example.a79069.zhihu.data.FavoriteNews;
import com.example.a79069.zhihu.data.NewsComment;
import com.example.a79069.zhihu.data.source.DataSource;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by 79069 on 2017/3/23.
 */

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;

    private LocalDataSource(){
        LitePal.getDatabase();
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
    public void getHotNewsList(NewsSimpleListCallback callback) {

    }

    @Override
    public void getNewsDetail(String newsId , NewsDetailCallback callback) {

    }


    /**
     * 获取我的收藏列表
     * @param callback
     */
    @Override
    public void getFavoritesList(FavoritesNewsCallback callback) {
        List<FavoriteNews> favoriteNewsList = DataSupport.findAll(FavoriteNews.class);

        callback.onSuccess(favoriteNewsList);
    }

    /**
     * 添加到我的收藏
     * @param address
     * @param title
     * @param callback
     */
    @Override
    public void addFavoriteNews(String address, String title, addFavoriteNewsCallback callback) {
        //检查当前数据库是否还有要添加到我的收藏的元素
        //如果有，则删除，并且输出已经取消收藏
        //如果没有，就添加到数据库
        List<FavoriteNews> favoriteNewsList = DataSupport.select("address").where("address = ?" , address).find(FavoriteNews.class);

        if (favoriteNewsList.size() > 0){
            DataSupport.deleteAll(FavoriteNews.class , "address = ?" , address);
            callback.onGiveUp();
        }else {
            FavoriteNews favoriteNews = new FavoriteNews();
            favoriteNews.setTitle(title);
            favoriteNews.setAddress(address);
            boolean saveOrNot = favoriteNews.save();
            if (saveOrNot){
                callback.onSuccess();
            }else {
                //存储失败时调用
                callback.onFailed();
            }
        }
    }


    @Override
    public void getNewsComments(String address, NewsCommentsCallback callback) {

    }

}
