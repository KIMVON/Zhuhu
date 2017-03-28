package com.example.a79069.zhihu.otherDayNewsList;

import android.os.Handler;
import android.os.Message;

import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;

/**
 * Created by 79069 on 2017/3/27.
 */

public class OtherDayNewsListPresenter implements OtherDayNewsListContract.Presenter {

    private AppRepository mAppRepository;

    private OtherDayNewsListContract.View mOtherDayNewsListFragment;

    public OtherDayNewsListPresenter(AppRepository appRepository , OtherDayNewsListContract.View otherDayNewsListFragment){
        mAppRepository = appRepository;

        mOtherDayNewsListFragment = otherDayNewsListFragment;

        mOtherDayNewsListFragment.setPresenter(this);
    }

    @Override
    public void onStart() {

    }



    @Override
    public void refreshNews(final Handler handler, String date) {
        mAppRepository.getNews("http://news-at.zhihu.com/api/4/news/before/" + date, new DataSource.NewsSimpleListCallback() {
            @Override
            public void onSuccess(NewsSimpleList newsSimpleList) {
                Message message = Message.obtain();
                message.obj = newsSimpleList;
                message.what = 1;

                handler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void onLoad(final Handler handler , String date) {
        mAppRepository.getNews("http://news-at.zhihu.com/api/4/news/before/"+date, new DataSource.NewsSimpleListCallback() {
            @Override
            public void onSuccess(NewsSimpleList newsSimpleList) {
                Message message = Message.obtain();
                message.obj = newsSimpleList;
                message.what = 1;

                handler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });
    }
}
