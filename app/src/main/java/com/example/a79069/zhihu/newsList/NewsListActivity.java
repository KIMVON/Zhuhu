package com.example.a79069.zhihu.newsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.util.ActivityUtils;

/**
 * Created by 79069 on 2017/3/23.
 */

public class NewsListActivity extends AppCompatActivity {

    private NewsListContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null ) {
            fragment = NewsListFragment.newInstance();

            ActivityUtils.addFragmentToActivity(fragmentManager , fragment , R.id.fragment_container);
        }
        mPresenter = new NewsListPresenter(ActivityUtils.getAppRepository() , (NewsListContract.View) fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
