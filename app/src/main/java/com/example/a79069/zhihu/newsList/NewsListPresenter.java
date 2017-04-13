package com.example.a79069.zhihu.newsList;

import android.os.Handler;
import android.os.Message;

import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.NewsTheme;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;

import java.util.List;


/**
 * Created by 79069 on 2017/3/24.
 */

public class NewsListPresenter implements NewsListContract.Presenter {
    static final int LOAD_TODAY_NEWS = 0;

    static final int LOAD_NEWS_THEME = 1;

    static final int LOAD_NEWS_THEME_CONTENT = 2;

    static final int REFRESH_NEWS = 3;


    private AppRepository mAppRepository;
    private NewsListContract.View mMainFragment;

    public NewsListPresenter(AppRepository appRepository, NewsListContract.View mainFragmentView) {

        mAppRepository = appRepository;

        mMainFragment = mainFragmentView;

        mMainFragment.setPresenter(this);
    }


    /**
     * 刷新新闻
     */
    @Override
    public void refreshNews(String address , final Handler handler) {
        mAppRepository.getNewsThemeConent(address , new DataSource.NewsThemeContentCallback() {
            @Override
            public void onSuccess(List<NewsSimple> newsSimpleList) {
                Message message = Message.obtain();
                message.obj = newsSimpleList;
                message.what = REFRESH_NEWS;

                handler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });
    }


    /**
     * 加载新闻列表内容
     *
     * @param handler
     */
    @Override
    public void onLoad(final Handler handler) {
        getTodayNews(handler);

        getNewsTheme(handler);
    }

    @Override
    public void getTodayNews(final Handler handler) {
        mAppRepository.getNews("http://news-at.zhihu.com/api/4/news/latest", new DataSource.NewsSimpleListCallback() {
            @Override
            public void onSuccess(NewsSimpleList newsSimpleList) {
                Message message = Message.obtain();
                message.obj = newsSimpleList;
                message.what = LOAD_TODAY_NEWS;

                handler.sendMessage(message);


            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void getNewsTheme(final Handler handler) {
        mAppRepository.getNewsTheme(new DataSource.NewsThemeCallback() {
            @Override
            public void onSuccess(List<NewsTheme> newsThemeList) {
                Message message = Message.obtain();
                message.obj = newsThemeList;
                message.what = LOAD_NEWS_THEME;

                handler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void getNewsThemeContent(String address , final Handler handler) {
        mAppRepository.getNewsThemeConent(address, new DataSource.NewsThemeContentCallback() {
            @Override
            public void onSuccess(List<NewsSimple> newsSimpleList) {
                Message message = Message.obtain();
                message.obj = newsSimpleList;
                message.what = LOAD_NEWS_THEME_CONTENT;

                handler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });
    }


    @Override
    public void onStart() {

    }

}
