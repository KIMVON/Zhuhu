package com.example.a79069.zhihu.favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.launchPager.LaunchContract;
import com.example.a79069.zhihu.util.ActivityUtils;

/**
 * Created by 79069 on 2017/4/11.
 */

public class FavoritesActivity extends AppCompatActivity {
    public static Intent newIntent(Context context){
        Intent intent = new Intent(context , FavoritesActivity.class);

        return intent;
    }

    private FavoritesPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = FavoritesFragment.newInstance();

            ActivityUtils.addFragmentToActivity(fragmentManager , fragment , R.id.fragment_container);
        }

        mPresenter = new FavoritesPresenter(ActivityUtils.getAppRepository() , (FavoritesContract.View) fragment);




    }
}
