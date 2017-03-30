package com.example.a79069.zhihu.launchPager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch , container , false);

        mLaunchImageView = (ImageView) view.findViewById(R.id.launch_image);

        ObjectAnimator scaleXAnimator = new ObjectAnimator().ofFloat(mLaunchImageView , "scaleX" , 1f , 1.04f);

        ObjectAnimator scaleYAnimator = new ObjectAnimator().ofFloat(mLaunchImageView, "scaleY" , 1f , 1.04f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimator).with(scaleYAnimator);
        animatorSet.setDuration(3000);
        animatorSet.start();

        Glide.with(getActivity())
                .load("http://p2.zhimg.com/10/7b/107bb4894b46d75a892da6fa80ef504a.jpg")
                .into(mLaunchImageView);

        long time = 3000;

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
