package com.example.a79069.zhihu.favorites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.data.FavoriteNews;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.newsDetail.NewsDetailActivity;

import java.util.List;

/**
 * Created by 79069 on 2017/4/11.
 */

public class FavoritesFragment extends Fragment implements FavoritesContract.View {
    public static Fragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();

        return fragment;
    }

    private FavoritesContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private FavoritesNewsAdapter mAdapter;

    private LinearLayout mFavoritesContainer;

    private TextView mTextView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorites_news, container, false);

        mFavoritesContainer = (LinearLayout) view.findViewById(R.id.my_favorites_container);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.favorites_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void closeActivity() {
        getActivity().finish();
    }

    /**
     * 初始化、加载适配器
     *
     * @param favoriteNewsList
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setAdapter(List<FavoriteNews> favoriteNewsList) {
        if (favoriteNewsList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);

            mTextView = new TextView(getActivity());

            mTextView.setText("什么鬼也没有");
            mTextView.setTextSize(50);
            mTextView.setPadding(0,30,0,0);
            mTextView.setTextColor(Color.BLACK);
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            mFavoritesContainer.addView(mTextView);
        } else {
            if (mTextView != null) {
                mFavoritesContainer.removeView(mTextView);
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new FavoritesNewsAdapter(favoriteNewsList);

            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * 启动news的详细页面
     *
     * @param address
     */
    @Override
    public void showNewsDetail(String address) {
        Intent intent = NewsDetailActivity.newIntent(getActivity(), address);

        startActivity(intent);
    }


    @Override
    public void setPresenter(FavoritesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    /**
     * 适配器
     */
    private class FavoritesNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNewsTitleTextView;

        private String address;

        public FavoritesNewsViewHolder(View itemView) {
            super(itemView);

            mNewsTitleTextView = (TextView) itemView.findViewById(R.id.news_title);

            itemView.setOnClickListener(this);
        }

        public void onBind(FavoriteNews favoriteNews) {
            mNewsTitleTextView.setText(favoriteNews.getTitle());

            address = favoriteNews.getAddress();
        }

        @Override
        public void onClick(View view) {
            showNewsDetail(address);
        }
    }

    private class FavoritesNewsAdapter extends RecyclerView.Adapter<FavoritesNewsViewHolder> {
        private List<FavoriteNews> mFavoriteNewsList;

        public FavoritesNewsAdapter(List<FavoriteNews> favoriteNewsList) {
            mFavoriteNewsList = favoriteNewsList;
        }

        @Override
        public FavoritesNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_favorites_news_list_item, parent, false);

            return new FavoritesNewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FavoritesNewsViewHolder holder, int position) {
            holder.onBind(mFavoriteNewsList.get(position));
        }

        @Override
        public int getItemCount() {
            return mFavoriteNewsList.size();
        }
    }
}
