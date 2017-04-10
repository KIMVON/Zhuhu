package com.example.a79069.zhihu.comments;

import android.os.Message;

import com.example.a79069.zhihu.data.NewsComment;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 79069 on 2017/4/9.
 */

public class CommentsPresenter implements CommentsContract.Presenter {


    private AppRepository mAppRepository;

    private CommentsContract.View mCommentsFragment;


    public CommentsPresenter(AppRepository appRepository, CommentsContract.View commentsFragment) {
        checkNotNull(appRepository);
        checkNotNull(commentsFragment);

        mAppRepository = appRepository;

        mCommentsFragment = commentsFragment;

        mCommentsFragment.setPresenter(this);
    }


    @Override
    public void getComment(final String address) {
        mAppRepository.getNewsComments(address, new DataSource.NewsCommentsCallback() {
            @Override
            public void onSuccess(List<NewsComment> newsCommentList) {
                String[] temp = address.split("/");
                String judgement = temp[temp.length-1];

                mCommentsFragment.onLoadComments(newsCommentList , judgement);

            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void onStart() {

    }
}
