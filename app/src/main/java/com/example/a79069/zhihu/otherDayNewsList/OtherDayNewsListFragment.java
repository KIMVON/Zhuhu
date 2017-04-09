package com.example.a79069.zhihu.otherDayNewsList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.example.a79069.zhihu.newsDetail.NewsDetailActivity;
import com.example.a79069.zhihu.newsList.NewsListFragment;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/27.
 */

public class OtherDayNewsListFragment extends Fragment implements OtherDayNewsListContract.View {
    private static final String OTHER_DATE_NEWS = "com.example.a79069.zhihu.otherDayNewsList.otherDateNews";

    public static Fragment newInstance(String date){
        Fragment fragment = new OtherDayNewsListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(OTHER_DATE_NEWS , date);
        fragment.setArguments(bundle);

        return fragment;
    }

    private OtherDayNewsListContract.Presenter mPresenter;

    private String mDate;

    private RecyclerView mRecyclerView;

    private OtherDayNewsListAdapter mAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Log.d(TAG, "handleMessage: "+ msg.obj);
                setAdapter((NewsSimpleList) msg.obj);

                mRefreshLayout.setRefreshing(false);
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDate = (String) getArguments().getSerializable(OTHER_DATE_NEWS);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_day_news_list, container , false);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_news_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshNews(mHandler , mDate);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onLoad(mHandler , mDate);
    }



    @Override
    public void setPresenter(OtherDayNewsListContract.Presenter presenter) {
        mPresenter = presenter;
    }



    @Override
    public void showNewsDetail(String address) {
        Intent intent = NewsDetailActivity.newIntent(getActivity() , address);
        Log.d(TAG, "showNewsDetail: "+address);
        startActivity(intent);
    }


    @Override
    public void setAdapter(NewsSimpleList newsSimpleList) {
        if (mAdapter == null){
            mAdapter = new OtherDayNewsListAdapter(newsSimpleList);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }



    /**
     * 适配器
     */
    private class OtherDayNewsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;

        private TextView mTitleTextView;

        private NewsSimple mNewsSimple;




        public OtherDayNewsListViewHolder(View itemView) {
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
            String image = imageURL.substring(2 , imageURL.length()-2).replaceAll("\\\\" , "");

            Glide.with(getActivity()).load(image).into(mImageView);
        }


        @Override
        public void onClick(View view) {
            showNewsDetail("http://news-at.zhihu.com/api/4/news/"+mNewsSimple.getId());
        }
    }

    private class OtherDayNewsListAdapter extends RecyclerView.Adapter<OtherDayNewsListViewHolder>{
        private NewsSimpleList mNewsSimpleList;
        private List<NewsSimple> mNewsSimples;
        public OtherDayNewsListAdapter(NewsSimpleList newsSimpleList){
            mNewsSimpleList = newsSimpleList;

            mNewsSimples = mNewsSimpleList.getNewsSimpleList();
        }


        @Override
        public OtherDayNewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news_list_item , parent , false);

            return new OtherDayNewsListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OtherDayNewsListViewHolder holder, int position) {
            NewsSimple newsSimple = mNewsSimples.get(position);
            String imageURL = newsSimple.getImage();
            String title = newsSimple.getTitle();

            holder.onBindView(imageURL , title , newsSimple);
        }

        @Override
        public int getItemCount() {
            return mNewsSimples.size();
        }
    }
}
