package com.example.a79069.zhihu.newsDetail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.data.NewsDetail;

import java.lang.reflect.InvocationTargetException;

import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/26.
 */

public class NewsDetailFragment extends Fragment implements NewsDetailContract.View {
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

    private ImageView mArticleImage;

    private TextView mArticleTitle;

    private WebView mWebView;

    private NestedScrollView mNewsNestedScrollView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 2){
                NewsDetail newsDetail = (NewsDetail) msg.obj;


                mArticleTitle.setText(newsDetail.getTitle());
                Glide.with(getActivity()).load(newsDetail.getImageURL()).into(mArticleImage);

                /**
                 * 添加CSS样式
                 *
                 * 截取了前面图片部分
                 */
                String tempUrl= "<html>\n" +
                        "<head>\n" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/webview.css\" />\n" +
                        "</head>\n" +
                        "<body>\n" +
                        newsDetail.getBody().replaceFirst("" , "").substring(91) + "\n" +
                        "</body>\n" +
                        "</html>";
                mWebView.loadDataWithBaseURL(null , tempUrl  , "text/html", "utf-8", null);
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //让ImageView扩展到状态栏
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        mNewsAddress = (String) getArguments().getSerializable(NEWS_DETAIL_ADDRESS);


        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNewsNestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        mNewsNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>460){
                    if (Build.VERSION.SDK_INT>=21){
                        getActivity().getWindow().setStatusBarColor(Color.BLACK);
                    }
                }else {
                    if (Build.VERSION.SDK_INT>=21){
                        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }
                }
            }
        });


        mArticleImage = (ImageView) view.findViewById(R.id.article_image);
        mArticleTitle = (TextView) view.findViewById(R.id.article_title);

        mWebView = (WebView) view.findViewById(R.id.news_detail_web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new WebViewClient());


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onLoad(mNewsAddress , mHandler);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void setPresenter(NewsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
