package com.example.a79069.zhihu.newsDetail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.data.NewsDetail;

import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/26.
 */

public class NewsDetailFragment extends Fragment implements NewsDetailContract.View {
    private static final String NEWS_DETAIL_ID = "com.example.a79069.zhihu.newsDetail.fragment.id";

    public static Fragment newInstance(String id) {
        NewsDetailFragment fragment = new NewsDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_DETAIL_ID, id);
        fragment.setArguments(bundle);

        return fragment;
    }

    private NewsDetailContract.Presenter mPresenter;

    private String mNewsId;

    private WebView mWebView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 2){
                NewsDetail newsDetail = (NewsDetail) msg.obj;

                mWebView.loadUrl(newsDetail.getShareURL());
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewsId = (String) getArguments().getSerializable(NEWS_DETAIL_ID);

        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = (WebView) view.findViewById(R.id.news_detail_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onLoad(mNewsId, mHandler);
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
