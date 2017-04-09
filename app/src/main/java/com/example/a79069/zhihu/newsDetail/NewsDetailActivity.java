package com.example.a79069.zhihu.newsDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.util.ActivityUtils;

/**
 * Created by 79069 on 2017/3/26.
 */

public class NewsDetailActivity extends AppCompatActivity {
    private static final String NEWS_DETAIL_ADDRESS = "com.example.a79069.zhihu.newsDetail.address";

    public static Intent newIntent(Context context, String address) {


        Intent intent = new Intent(context, NewsDetailActivity.class);

        intent.putExtra(NEWS_DETAIL_ADDRESS, address);

        return intent;
    }

    private NewsDetailContract.Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 如果在这里调用要这样，但是我选择在Fragment那里调用
         */
        //让ImageView扩展到状态栏，一定要在setContentView之前调用
//        if (Build.VERSION.SDK_INT>=21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        setContentView(R.layout.fragment_container);


        String address = getIntent().getStringExtra(NEWS_DETAIL_ADDRESS);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = NewsDetailFragment.newInstance(address);

            ActivityUtils.addFragmentToActivity(fragmentManager, fragment, R.id.fragment_container);
        }

        mPresenter = new NewDetailPresenter(ActivityUtils.getAppRepository(), (NewsDetailContract.View) fragment);





    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
