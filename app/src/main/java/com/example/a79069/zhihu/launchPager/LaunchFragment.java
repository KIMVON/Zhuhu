package com.example.a79069.zhihu.launchPager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.newsList.NewsListActivity;

/**
 * Created by 79069 on 2017/3/30.
 */

public class LaunchFragment extends Fragment implements LaunchContract.View {
    public static LaunchFragment newIntance(){
        LaunchFragment fragment = new LaunchFragment();

        return fragment;
    }

    private LaunchContract.Presenter mPresenter;

    private ImageView mLaunchImageView;

    private TextView mAppName;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让ImageView扩展到状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch , container , false);

        mLaunchImageView = (ImageView) view.findViewById(R.id.launch_image);

        mAppName = (TextView) view.findViewById(R.id.app_name);

        ObjectAnimator scaleXAnimator = new ObjectAnimator().ofFloat(mLaunchImageView , "scaleX" , 1f , 1.04f);

        ObjectAnimator scaleYAnimator = new ObjectAnimator().ofFloat(mLaunchImageView, "scaleY" , 1f , 1.04f);

        ObjectAnimator alphaAnimator = new ObjectAnimator().ofFloat(mAppName , "alpha" , 0f , 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimator).with(scaleYAnimator).after(alphaAnimator);
        animatorSet.setDuration(3000);
        animatorSet.start();

        Glide.with(getActivity())
                .load("http://p2.zhimg.com/10/7b/107bb4894b46d75a892da6fa80ef504a.jpg")
                .into(mLaunchImageView);

        long time = 3300;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();

                getActivity().finish();
            }
        }, time);

        return view;
    }

    @Override
    public void setPresenter(LaunchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void startMainActivity() {
        Intent intent = NewsListActivity.newIntent(getActivity());

        startActivity(intent);
    }
}
