package com.example.a79069.zhihu.launchPager;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;
import com.example.a79069.zhihu.newsList.NewsListContract;

/**
 * Created by 79069 on 2017/3/30.
 */

public interface LaunchContract {
    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{
        void startMainActivity();
    }
}
