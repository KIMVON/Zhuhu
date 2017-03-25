package com.example.a79069.zhihu.mainTask;

import android.os.Handler;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;
import com.example.a79069.zhihu.data.NewsSimpleList;

/**
 * Created by 79069 on 2017/3/23.
 */

public interface MainContract {
    interface Presenter extends BasePresenter{
        void refreshNews();



        void onLoad(Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void showNewsDetail(String newsId);

        void showSearchActivity();

        void showMyFavoritesActivity();

        void setAdapter(NewsSimpleList newsSimpleList);



    }
}
