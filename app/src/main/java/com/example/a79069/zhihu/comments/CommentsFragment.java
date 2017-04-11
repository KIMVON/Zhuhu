package com.example.a79069.zhihu.comments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.data.AllNewsComment;
import com.example.a79069.zhihu.data.NewsComment;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 79069 on 2017/4/9.
 */

public class CommentsFragment extends Fragment implements CommentsContract.View, View.OnClickListener {
    private static final String TAG = "CommentsFragment";


    private static final String NEWS_ID = "com.example.a79069.zhihu.comments.fragment.news_id";

    private static final String LONG_COMMENTS = "long-comments";

    private static final int LONG_COMMENTS_WHAT = 0;

    private static final String SHORT_COMMENTS = "short-comments";

    private static final int SHORT_COMMENTS_WHAT = 1;

    public static CommentsFragment newInstance(String id) {
        CommentsFragment fragment = new CommentsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_ID, id);
        fragment.setArguments(bundle);

        return fragment;
    }


    private CommentsContract.Presenter mPresenter;

    private String mId;

    private Toolbar mToolbar;

    private ActionBar mActionBar;

    private TextView mTitleString;

    private TextView mLongCommentsTextView;

    private LinearLayout mLongCommentsContainer;

    private TextView mShortCommentsTextView;

    private LinearLayout mShortCommentsContainer;

    private ImageView mGoBackBtn;

    private LinearLayout mWriterCommentsBtn;

    private AllNewsComment mAllNewsComment;

    private List<NewsComment> mLongCommentList;

    private List<NewsComment> mShortCommentList;

    private PopupWindow mPopupWindow;


    /**
     * 主线程
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == LONG_COMMENTS_WHAT) {
                mLongCommentList = mAllNewsComment.getLongNewsCommentList();
                
                if (mLongCommentList.size() > 0) {
                    mLongCommentsTextView.setVisibility(View.VISIBLE);
                    mLongCommentsContainer.setVisibility(View.VISIBLE);
                    mLongCommentsTextView.setText(mLongCommentList.size() + "条长评");

                    for (int i = 0; i < mLongCommentList.size(); i++) {
                        addComment(mLongCommentList.get(i), mLongCommentsContainer);
                    }
                } else {
                    mLongCommentsTextView.setVisibility(View.GONE);
                    mLongCommentsContainer.setVisibility(View.GONE);
                }

            }


            if (msg.what == SHORT_COMMENTS_WHAT) {
                mShortCommentList = mAllNewsComment.getShortNewsCommentList();

                if (mShortCommentList.size() > 0) {
                    mShortCommentsTextView.setVisibility(View.VISIBLE);
                    mShortCommentsContainer.setVisibility(View.VISIBLE);

                    mShortCommentsTextView.setText(mShortCommentList.size() + "条短评");

                    for (int i = 0; i < mShortCommentList.size(); i++) {
                        addComment(mShortCommentList.get(i), mShortCommentsContainer);
                    }
                } else {
                    mShortCommentsTextView.setVisibility(View.GONE);
                    mShortCommentsContainer.setVisibility(View.GONE);
                }
            }

            if (mLongCommentList != null && mShortCommentList != null) {
                mTitleString.setText((mLongCommentList.size() + mShortCommentList.size()) + "条评论");
            }

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAllNewsComment = new AllNewsComment();

        mId = (String) getArguments().getSerializable(NEWS_ID);


        //启动两个线程去访问
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPresenter.getComment("http://news-at.zhihu.com/api/4/story/" + mId + "/long-comments");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPresenter.getComment("http://news-at.zhihu.com/api/4/story/" + mId + "/short-comments");
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_comments, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDefaultDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);

//        mActionBar.setTitle("");

        mTitleString = (TextView) view.findViewById(R.id.comment_title_string);

        mLongCommentsTextView = (TextView) view.findViewById(R.id.long_comments);
        mLongCommentsContainer = (LinearLayout) view.findViewById(R.id.long_comments_container);

        mShortCommentsTextView = (TextView) view.findViewById(R.id.short_comments);
        mShortCommentsContainer = (LinearLayout) view.findViewById(R.id.short_comments_container);

        mGoBackBtn = (ImageView) view.findViewById(R.id.backtrack_btn);
        mWriterCommentsBtn = (LinearLayout) view.findViewById(R.id.write_comment);


        mGoBackBtn.setOnClickListener(this);
        mWriterCommentsBtn.setOnClickListener(this);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 必须同步，加载返回来的评论List
     * @param newsCommentList
     * @param judgement
     */
    @Override
    public synchronized void onLoadComments(List<NewsComment> newsCommentList, String judgement) {
        if (judgement.equals(LONG_COMMENTS)) {
            mAllNewsComment.setLongNewsCommentList(newsCommentList);

        } else if (judgement.equals(SHORT_COMMENTS)) {
            mAllNewsComment.setShortNewsCommentList(newsCommentList);
        } else {
            //??????
        }

        Message message = Message.obtain();

        if (judgement.equals(LONG_COMMENTS)) {
            message.what = LONG_COMMENTS_WHAT;
        } else {
            message.what = SHORT_COMMENTS_WHAT;
        }

        mHandler.sendMessage(message);

    }


    /**
     * 添加Comment到container
     * @param newsComment
     * @param root
     */
    @Override
    public void addComment(NewsComment newsComment, ViewGroup root) {
        //这里必须要添加false，否者系统会认为view已经有parentView了，必须移除
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_news_comment_item, root,false);
        CircleImageView headerImage = (CircleImageView) view.findViewById(R.id.user_header_image);
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        TextView supportCount = (TextView) view.findViewById(R.id.support_count);
        TextView commentContent = (TextView) view.findViewById(R.id.comment_content);
        TextView commentTime = (TextView) view.findViewById(R.id.comment_time);

        Glide.with(view.getContext()).load(newsComment.getAvatar()).into(headerImage);
        userName.setText(newsComment.getAuthor());
        supportCount.setText(newsComment.getLikes()+"");
        commentContent.setText(newsComment.getContent());
        commentTime.setText(newsComment.getTime());

        root.addView(view);
    }

    @Override
    public void onWriteComment() {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.view_user_login_pop_window , (ViewGroup) this.getView() , false);

        ImageView backtrackBtn = (ImageView) view.findViewById(R.id.backtrack_btn);
        Button loginByZhihuBtn = (Button) view.findViewById(R.id.login_by_zhihu);
        ImageView loginBySinaWeiboBtn = (ImageView) view.findViewById(R.id.sina_weibo_btn);
        ImageView loginByTecentWeiboBtn = (ImageView) view.findViewById(R.id.tecent_weibo_btn);

        //防止冲突写在里面
        backtrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        loginByZhihuBtn.setOnClickListener(this);
        loginBySinaWeiboBtn.setOnClickListener(this);
        loginByTecentWeiboBtn.setOnClickListener(this);

        /**
         * 最新的：除获得屏幕的宽和高外还可以获得屏幕的密度。
         */
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthScreen = dm.widthPixels;
        int heightScreen = dm.heightPixels;

        //设置屏幕的高度和宽度  屏幕适配 heightScreen * 9/15  heightScreen * 8/15
        mPopupWindow = new PopupWindow(view, widthScreen, heightScreen);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);


        mPopupWindow.showAtLocation(getView() , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置 背景的颜色为 0.5f 的透明度
        getView().setAlpha(0.8f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //当popwindow消失的时候，恢复背景的颜色。 backgroundAlpha(1.0f);
                getView().setAlpha(1.0f);
            }
        });

    }

    @Override
    public void closeActivity() {
        getActivity().finish();
    }

    @Override
    public void setPresenter(CommentsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backtrack_btn:
                closeActivity();
                break;
            case R.id.write_comment:
                onWriteComment();
                break;
        }
    }
}
