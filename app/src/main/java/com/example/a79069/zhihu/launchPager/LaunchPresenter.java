package com.example.a79069.zhihu.launchPager;

import com.example.a79069.zhihu.data.source.AppRepository;

/**
 * Created by 79069 on 2017/3/30.
 */

public class LaunchPresenter implements LaunchContract.Presenter {
    private AppRepository mAppRepository;

    private LaunchContract.View mFragment;

    public LaunchPresenter(AppRepository appRepository , LaunchContract.View fragment){
        mAppRepository = appRepository;

        mFragment = fragment;

        mFragment.setPresenter(this);
    }


    @Override
    public void onStart() {

    }
}
