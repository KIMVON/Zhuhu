package com.example.a79069.zhihu.data;

import java.util.List;

/**
 * Created by 79069 on 2017/4/9.
 */

public class AllNewsComment {
    private List<NewsComment> mLongNewsCommentList;

    private List<NewsComment> mShortNewsCommentList;

    public List<NewsComment> getLongNewsCommentList() {
        return mLongNewsCommentList;
    }

    public void setLongNewsCommentList(List<NewsComment> longNewsCommentList) {
        mLongNewsCommentList = longNewsCommentList;
    }

    public List<NewsComment> getShortNewsCommentList() {
        return mShortNewsCommentList;
    }

    public void setShortNewsCommentList(List<NewsComment> shortNewsCommentList) {
        mShortNewsCommentList = shortNewsCommentList;
    }
}
