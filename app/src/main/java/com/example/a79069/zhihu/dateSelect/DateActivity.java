package com.example.a79069.zhihu.dateSelect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.newsList.NewsListContract;
import com.example.a79069.zhihu.newsList.NewsListFragment;
import com.example.a79069.zhihu.newsList.NewsListPresenter;
import com.example.a79069.zhihu.util.ActivityUtils;

import java.util.Date;

/**
 * Created by 79069 on 2017/3/27.
 */

public class DateActivity extends AppCompatActivity {
    public static Intent newIntent(Context context){
        Intent intent = new Intent(context , DateActivity.class);

        return intent;
    }

    private DateContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null ) {
            fragment = DateFragment.newInstance();

            ActivityUtils.addFragmentToActivity(fragmentManager , fragment , R.id.fragment_container);
        }


        mPresenter = new DatePresenter(ActivityUtils.getAppRepository() , (DateContract.View) fragment);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
