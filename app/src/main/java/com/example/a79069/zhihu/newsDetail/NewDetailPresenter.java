package com.example.a79069.zhihu.newsDetail;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.a79069.zhihu.data.FavoriteNews;
import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/26.
 */

public class NewDetailPresenter implements NewsDetailContract.Presenter {
    private AppRepository mAppRepository;

    private NewsDetailContract.View mFragment;

    public NewDetailPresenter(AppRepository appRepository , NewsDetailContract.View fragment){
        mAppRepository = appRepository;

        mFragment = fragment;

        mFragment.setPresenter(this);
    }



    @Override
    public void onStart() {

    }


    @Override
    public void onLoad(String address , final Handler handler) {

        mAppRepository.getNewsDetail(address , new DataSource.NewsDetailCallback() {
            @Override
            public void onSuccess(NewsDetail newsDetail) {
                Message message = new Message();
                message.what = 2;

                message.obj = newsDetail;

                handler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void addToFavorites(String address, String title) {

        mAppRepository.addFavoriteNews(address, title, new DataSource.addFavoriteNewsCallback() {
            @Override
            public void onSuccess() {
                //调用View层方法显示
                mFragment.showAddFavoritesSuccess();
            }

            @Override
            public void onGiveUp() {
                //调用View层方法显示
                mFragment.showGiveUpAddFavorites();
            }

            @Override
            public void onFailed() {
                //调用View层方法显示
                mFragment.showAddFavoritesFailed();
            }
        });
    }
}
