package com.example.a79069.zhihu.otherDayNewsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.dateSelect.DateContract;
import com.example.a79069.zhihu.dateSelect.DateFragment;
import com.example.a79069.zhihu.dateSelect.DatePresenter;
import com.example.a79069.zhihu.util.ActivityUtils;

/**
 * Created by 79069 on 2017/3/27.
 */

public class OtherDayNewsListActivity extends AppCompatActivity {
    private static final String DATE_SELECT = "com.example.a79069.zhihu.otherDayNewsList.dateSelect";

    public static Intent newIntent(Context context , String date){
        Intent intent = new Intent(context , OtherDayNewsListActivity.class);

        intent.putExtra(DATE_SELECT , date);

        return intent;
    }


    private OtherDayNewsListContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        String date = getIntent().getStringExtra(DATE_SELECT);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null ) {
            fragment = OtherDayNewsListFragment.newInstance(date);

            ActivityUtils.addFragmentToActivity(fragmentManager , fragment , R.id.fragment_container);
        }


        mPresenter = new OtherDayNewsListPresenter(ActivityUtils.getAppRepository() , (OtherDayNewsListContract.View) fragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
