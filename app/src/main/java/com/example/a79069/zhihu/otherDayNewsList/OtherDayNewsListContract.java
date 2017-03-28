package com.example.a79069.zhihu.otherDayNewsList;

import android.os.Handler;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;
import com.example.a79069.zhihu.data.NewsSimpleList;

/**
 * Created by 79069 on 2017/3/27.
 */

public interface OtherDayNewsListContract {
    interface Presenter extends BasePresenter{
        void refreshNews(Handler handler , String date);



        void onLoad(Handler handler , String date);

    }

    interface View extends BaseView<Presenter>{
        void showNewsDetail(String newsId);

        void setAdapter(NewsSimpleList newsSimpleList);

    }
}
