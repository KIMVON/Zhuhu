package com.example.a79069.zhihu.newsDetail;

import android.os.Handler;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;

/**
 * Created by 79069 on 2017/3/26.
 */

public interface NewsDetailContract {
    interface Presenter extends BasePresenter{
        void onLoad(String address , Handler handler);

        void addToFavorites(String address , String title);


    }

    interface View extends BaseView<Presenter>{
        void showPopWindow();

        void showCommentActivity();

        void showAddFavoritesSuccess();

        void showAddFavoritesFailed();

        void showGiveUpAddFavorites();

        void showToast(int resource);
    }
}
