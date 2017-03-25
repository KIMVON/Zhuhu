package com.example.a79069.zhihu.mainTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;


import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/23.
 */

public class MainFragment extends Fragment implements MainContract.View {
    public static MainFragment newInstance(){

        return new MainFragment();

    }

    private MainContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private NewsListAdapter mAdapter;




    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                Log.d(TAG, "handleMessage: "+ msg.obj);
                setAdapter((NewsSimpleList) msg.obj);
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_news_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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


    @Override
    public void showNewsDetail(String newsId) {

    }

    @Override
    public void showSearchActivity() {

    }

    @Override
    public void showMyFavoritesActivity() {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }



    /**
     * 适配器
     */

    private class NewsListViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;

        private TextView mTitleTextView;

        public NewsListViewHolder(View itemView) {
            super(itemView);



            mImageView = (ImageView) itemView.findViewById(R.id.list_item_image);

            mTitleTextView = (TextView) itemView.findViewById(R.id.question_title);
        }


        /**
         * 绑定数据
         * @param imageURL
         * @param title
         */
        public void onBindView(String imageURL , String title){

            mTitleTextView.setText(title);
            String image = imageURL.substring(2 , imageURL.length()-2).replaceAll("\\\\" , "") ;
            Log.d(TAG, "onBindView: "+imageURL);
            Log.d(TAG, "onBindView: "+image);

            Glide.with(getActivity()).load(image.toString()).into(mImageView);

        }
    }


    private class NewsListAdapter extends RecyclerView.Adapter<NewsListViewHolder>{
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

            holder.onBindView(imageURL , title);
        }

        @Override
        public int getItemCount() {
            return mNewsSimpleList.size();
        }
    }
}
