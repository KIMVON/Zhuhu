package com.example.a79069.zhihu.mainTask;

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

public class MainActivity extends AppCompatActivity {

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null ) {
            fragment = MainFragment.newInstance();

            ActivityUtils.addFragmentToActivity(fragmentManager , fragment , R.id.fragment_container);
        }
        mPresenter = new MainPresenter(ActivityUtils.getAppRepository() , (MainContract.View) fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
