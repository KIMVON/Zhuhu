package com.example.a79069.zhihu.comments;

import android.view.ViewGroup;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;
import com.example.a79069.zhihu.data.NewsComment;

import java.util.List;

/**
 * Created by 79069 on 2017/4/9.
 */

public interface CommentsContract {
    interface Presenter extends BasePresenter{
        void getComment(String address);

    }

    interface View extends BaseView<Presenter>{

        void onLoadComments(List<NewsComment> newsCommentList , String judgement);

        void addComment(NewsComment newsComment , ViewGroup root);

        void onWriteComment();

        void closeActivity();

    }
}
