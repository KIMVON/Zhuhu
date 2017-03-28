package com.example.a79069.zhihu.newsList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.dateSelect.DateActivity;
import com.example.a79069.zhihu.newsDetail.NewsDetailActivity;

import java.util.List;


import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/23.
 */

public class NewsListFragment extends Fragment implements NewsListContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    public static NewsListFragment newInstance(){

        return new NewsListFragment();

    }

    private NewsListContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private NewsListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Log.d(TAG, "handleMessage: "+ msg.obj);
                setAdapter((NewsSimpleList) msg.obj);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list , container , false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_news_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_pick_date);

        fab.setOnClickListener(this);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onLoad(mHandler);
    }


    /**
     * 初始化Adapter
     * @param newsSimpleList
     */
    @Override
    public void setAdapter(NewsSimpleList newsSimpleList){
        if (mAdapter == null){
            mAdapter = new NewsListAdapter(newsSimpleList);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 启动详细页面
     * @param newsId
     */
    @Override
    public void showNewsDetail(String newsId) {

        Intent intent = NewsDetailActivity.newIntent(getActivity() , newsId);

        startActivity(intent);
    }


    @Override
    public void showSearchActivity() {

    }


    @Override
    public void showMyFavoritesActivity() {

    }

    @Override
    public void showDateActivity() {
        Intent intent = DateActivity.newIntent(getActivity());

        startActivity(intent);
    }

    @Override
    public void setPresenter(NewsListContract.Presenter presenter) {
        mPresenter = presenter;
    }



    @Override
    public void onRefresh() {
        mPresenter.refreshNews(mHandler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_pick_date:
                showDateActivity();
                break;
        }
    }





    /**
     * 适配器
     */
    private class NewsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        private TextView mTitleTextView;

        private NewsSimple mNewsSimple;

        public NewsListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mImageView = (ImageView) itemView.findViewById(R.id.list_item_image);

            mTitleTextView = (TextView) itemView.findViewById(R.id.question_title);
        }


        /**
         * 绑定数据
         * @param imageURL
         * @param title
         * @param newsSimple
         */
        public void onBindView(String imageURL, String title, NewsSimple newsSimple){
            mNewsSimple = newsSimple;

            mTitleTextView.setText(title);
            String image = imageURL.substring(2 , imageURL.length()-2).replaceAll("\\\\" , "") ;
            Log.d(TAG, "onBindView: "+imageURL);
            Log.d(TAG, "onBindView: "+image);

            Glide.with(getActivity()).load(image).into(mImageView);
        }


        @Override
        public void onClick(View view) {
            showNewsDetail(mNewsSimple.getId());
        }
    }


    private class NewsListAdapter extends RecyclerView.Adapter<NewsListViewHolder>  {
        private NewsSimpleList mNewsSimpleListObject;
        private List<NewsSimple> mNewsSimpleList;

        public NewsListAdapter(NewsSimpleList newsSimpleList){
            mNewsSimpleListObject = newsSimpleList;

            mNewsSimpleList = mNewsSimpleListObject.getNewsSimpleList();

        }

        @Override
        public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news_list_item , parent , false);


            return new NewsListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsListViewHolder holder, int position) {
            NewsSimple newsSimple = mNewsSimpleList.get(position);
            String imageURL = newsSimple.getImage();
            String title = newsSimple.getTitle();

            holder.onBindView(imageURL , title , newsSimple);
        }

        @Override
        public int getItemCount() {
            return mNewsSimpleList.size();
        }

    }
}
