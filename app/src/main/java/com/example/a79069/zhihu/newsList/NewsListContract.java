package com.example.a79069.zhihu.newsList;

import android.os.Handler;
import android.widget.ImageView;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.NewsTheme;

import java.util.List;

/**
 * Created by 79069 on 2017/3/23.
 */

public interface NewsListContract {
    interface Presenter extends BasePresenter{
        void refreshNews(String address , Handler handler);

        void onLoad(Handler handler);

        void getTodayNews(Handler handler);

        void getNewsTheme(Handler handler);

        void getNewsThemeContent(String address , final Handler handler);
    }


    interface View extends BaseView<Presenter>{
        void showNewsDetail(String newsId);

        void showSearchActivity();

        void showLoginPopWindow();

        void showMyFavoritesActivity();

        void startViewFlipperAnimation();

        void addImageViewToViewFlipper(String url);

        void showDateActivity();

        void setAdapter(List<NewsSimple> newsSimpleList);

        void onClickHomeBtn();

        void setSlidingAdapter(List<NewsTheme> newsThemeList);

        void startService();

        void initSlidingMenu();

        void initSlidingAndDragListView();

    }
}
