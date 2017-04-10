package com.example.a79069.zhihu.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.util.ActivityUtils;

/**
 * Created by 79069 on 2017/4/9.
 */

public class CommentsActivity extends AppCompatActivity {
    private static final String NEWS_ID = "com.example.a79069.zhihu.comments.activity.news_id";

    private CommentsPresenter mCommentsPresenter;

    private String mNewsID;


    public static Intent newIntent(Context context , String id){
        Intent intent = new Intent(context , CommentsActivity.class);
        intent.putExtra(NEWS_ID , id);

        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mNewsID = getIntent().getStringExtra(NEWS_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = CommentsFragment.newInstance(mNewsID);

            ActivityUtils.addFragmentToActivity(fragmentManager , fragment , R.id.fragment_container);
        }

        mCommentsPresenter = new CommentsPresenter(ActivityUtils.getAppRepository() , (CommentsContract.View) fragment);
    }



}
