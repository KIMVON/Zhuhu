package com.example.a79069.zhihu.favorites;

import com.example.a79069.zhihu.data.FavoriteNews;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;

import java.util.List;

/**
 * Created by 79069 on 2017/4/11.
 */

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private AppRepository mAppRepository;

    private FavoritesContract.View mFragment;

    public FavoritesPresenter(AppRepository appRepository, FavoritesContract.View fragment) {
        mAppRepository = appRepository;

        mFragment = fragment;

        mFragment.setPresenter(this);
    }


    /**
     * 获取收藏列表
     */
    @Override
    public void getFavoritesList() {
        mAppRepository.getFavoritesList(new DataSource.FavoritesNewsCallback() {
            @Override
            public void onSuccess(List<FavoriteNews> favoriteNewsList) {
                mFragment.setAdapter(favoriteNewsList);
            }

            @Override
            public void onFailed() {

            }
        });
    }


    @Override
    public void onStart() {
        getFavoritesList();
    }
}
