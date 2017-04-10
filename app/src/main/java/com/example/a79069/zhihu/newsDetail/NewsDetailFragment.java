package com.example.a79069.zhihu.newsDetail;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.comments.CommentsActivity;
import com.example.a79069.zhihu.data.NewsDetail;

import java.lang.reflect.InvocationTargetException;

import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/26.
 */

public class NewsDetailFragment extends Fragment implements NewsDetailContract.View, View.OnClickListener {
    private static final String NEWS_DETAIL_ADDRESS = "com.example.a79069.zhihu.newsDetail.fragment.address";

    public static Fragment newInstance(String address) {
        NewsDetailFragment fragment = new NewsDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_DETAIL_ADDRESS, address);
        fragment.setArguments(bundle);

        return fragment;
    }

    private NewsDetailContract.Presenter mPresenter;

    private String mNewsAddress;

    private String mNewsID;

    private ImageView mArticleImage;

    private TextView mArticleTitle;

    private WebView mWebView;

    private NestedScrollView mNewsNestedScrollView;

    private ImageView mBacktrackBtn;

    private ImageView mSupportBtn;

    private ImageView mPopBtn;

    private ImageView mCommentsBtn;

    private PopupWindow mPopupWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 2) {
                NewsDetail newsDetail = (NewsDetail) msg.obj;


                mArticleTitle.setText(newsDetail.getTitle());
                Glide.with(getActivity()).load(newsDetail.getImageURL()).into(mArticleImage);

                /**
                 * 添加CSS样式
                 *
                 * 截取了前面图片部分
                 */
                String tempUrl = "<html>\n" +
                        "<head>\n" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/webview.css\" />\n" +
                        "</head>\n" +
                        "<body>\n" +
                        newsDetail.getBody().replaceFirst("", "").substring(91) + "\n" +
                        "</body>\n" +
                        "</html>";
                mWebView.loadDataWithBaseURL(null, tempUrl, "text/html", "utf-8", null);
            }

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //让ImageView扩展到状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        mNewsAddress = (String) getArguments().getSerializable(NEWS_DETAIL_ADDRESS);

        mNewsID = mNewsAddress.substring(mNewsAddress.lastIndexOf("/")+1);

        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mArticleImage = (ImageView) view.findViewById(R.id.article_image);
        mArticleTitle = (TextView) view.findViewById(R.id.article_title);

        mWebView = (WebView) view.findViewById(R.id.news_detail_web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new WebViewClient());

        mNewsNestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        mNewsNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > mArticleImage.getHeight()) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        getActivity().getWindow().setStatusBarColor(Color.BLACK);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 21) {
                        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        mBacktrackBtn = (ImageView) view.findViewById(R.id.backtrack_btn);
        mSupportBtn = (ImageView) view.findViewById(R.id.support_btn);
        mPopBtn = (ImageView) view.findViewById(R.id.pop_btn);
        mCommentsBtn = (ImageView) view.findViewById(R.id.comments_btn);

        mBacktrackBtn.setOnClickListener(this);
        mSupportBtn.setOnClickListener(this);
        mPopBtn.setOnClickListener(this);
        mCommentsBtn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onLoad(mNewsAddress, mHandler);
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                getActivity().finish();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //底部菜单的Button
            case R.id.backtrack_btn:
                getActivity().finish();
                break;
            case R.id.support_btn:
                break;
            case R.id.pop_btn:
                showPopWindow();
                break;
            case R.id.comments_btn:
                showCommentActivity();
                break;

            //PopWindow里面的Button、ImageView
            case R.id.share_wechat:
                break;
            case R.id.share_wechat_moments:
                break;
            case R.id.share_qq:
                break;
            case R.id.share_weibo:
                break;
            case R.id.share_copy:
                break;
            case R.id.share_youdao:
                break;
            case R.id.share_email:
                break;
            case R.id.share_message:
                break;


            case R.id.favorites_btn:
                break;
            case R.id.cancel_btn:
                mPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void showPopWindow() {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_pop_window, null);

        ImageView weChatBtn = (ImageView) view.findViewById(R.id.share_wechat);
        ImageView weChatMomentBtn = (ImageView) view.findViewById(R.id.share_wechat_moments);
        ImageView QQBtn = (ImageView) view.findViewById(R.id.share_qq);
        ImageView weiBoBtn = (ImageView) view.findViewById(R.id.share_weibo);
        ImageView copyBtn = (ImageView) view.findViewById(R.id.share_copy);
        ImageView youDaoBtn = (ImageView) view.findViewById(R.id.share_youdao);
        ImageView emailBtn = (ImageView) view.findViewById(R.id.share_email);
        ImageView messagesBtn = (ImageView) view.findViewById(R.id.share_message);

        Button favoritesBtn = (Button) view.findViewById(R.id.favorites_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        weChatBtn.setOnClickListener(this);
        weChatMomentBtn.setOnClickListener(this);
        QQBtn.setOnClickListener(this);
        weiBoBtn.setOnClickListener(this);
        copyBtn.setOnClickListener(this);
        youDaoBtn.setOnClickListener(this);
        emailBtn.setOnClickListener(this);
        messagesBtn.setOnClickListener(this);

        favoritesBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        /**
         * 已经被遗弃了
         * //获取屏幕高度
         WindowManager windowManager = getActivity().getWindowManager();
         int width = windowManager.getDefaultDisplay().getWidth();
         int height = windowManager.getDefaultDisplay().getHeight();
         */

        /**
         * 最新的：除获得屏幕的宽和高外还可以获得屏幕的密度。
         */
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthScreen = dm.widthPixels;
        int heightScreen = dm.heightPixels;

        //设置屏幕的高度和宽度
        mPopupWindow = new PopupWindow(view, widthScreen, heightScreen * 8/15);

        //如果不设置背景颜色的话，无法是pop dimiss掉。
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);


        /** * 设置popwindow的弹出的位置. *
         1：首先要判断是否有navigation bar。如果有的的话，要把他们的高度给加起来。 * *
         2：showAtLocation（）；是pop相对于屏幕而言的。 * *
         3：如果是 pop.showAsDropDown();则是相对于你要点击的view的位置。设置的坐标。
         */
//        pop.showAtLocation(this.getView(), Gravity.BOTTOM, 0, 2*view.getHeight());
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
    public void showCommentActivity() {
        Intent intent = CommentsActivity.newIntent(getActivity() , mNewsID);

        startActivity(intent);
    }
}

