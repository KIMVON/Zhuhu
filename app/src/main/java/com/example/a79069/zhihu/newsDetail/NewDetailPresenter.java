package com.example.a79069.zhihu.newsDetail;

import android.os.Handler;
import android.os.Message;

import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;

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
    public void onLoad(String newId , final Handler handler) {
        mAppRepository.getNewsDetail(newId, new DataSource.NewsDetailCallback() {
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
}
