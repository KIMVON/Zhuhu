package com.example.a79069.zhihu.newsList;

import android.os.Handler;
import android.os.Message;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;


/**
 * Created by 79069 on 2017/3/24.
 */

public class NewsListPresenter implements NewsListContract.Presenter {

    private AppRepository mAppRepository;
    private NewsListContract.View mMainFragment;

    public NewsListPresenter(AppRepository appRepository , NewsListContract.View mainFragmentView){

        mAppRepository = appRepository;

        mMainFragment = mainFragmentView;

        mMainFragment.setPresenter(this);
    }


    /**
     * 刷新新闻
     */
    @Override
    public void refreshNews(final Handler handler) {
        mAppRepository.getNews("http://news-at.zhihu.com/api/4/news/latest" , new DataSource.NewsSimpleListCallback() {
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


    /**
     * 加载新闻列表内容
     * @param handler
     */
    @Override
    public void onLoad(final Handler handler) {
        mAppRepository.getNews("http://news-at.zhihu.com/api/4/news/latest" , new DataSource.NewsSimpleListCallback() {
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
    public void onStart() {

    }

}
