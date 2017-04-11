package com.example.a79069.zhihu.favorites;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;
import com.example.a79069.zhihu.data.FavoriteNews;

import java.util.List;

/**
 * Created by 79069 on 2017/4/11.
 */

public interface FavoritesContract {
    interface Presenter extends BasePresenter{
        void getFavoritesList();
    }

    interface View extends BaseView<Presenter>{
        void closeActivity();

        void setAdapter(List<FavoriteNews> favoriteNewsList);

        void showNewsDetail(String address);
    }
}
