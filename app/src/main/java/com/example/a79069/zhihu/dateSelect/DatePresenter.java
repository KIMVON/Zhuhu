package com.example.a79069.zhihu.dateSelect;

import com.example.a79069.zhihu.data.source.AppRepository;

/**
 * Created by 79069 on 2017/3/27.
 */

public class DatePresenter implements DateContract.Presenter {
    private AppRepository mAppRepository;

    private DateContract.View mDateFragment;


    public DatePresenter(AppRepository appRepository , DateContract.View dateFragment){
        mAppRepository = appRepository;

        mDateFragment = dateFragment;

        mDateFragment.setPresenter(this);
    }


    @Override
    public void onStart() {

    }
}
