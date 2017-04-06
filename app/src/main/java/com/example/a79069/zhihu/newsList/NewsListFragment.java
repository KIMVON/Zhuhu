package com.example.a79069.zhihu.newsList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.app.MyService;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.dateSelect.DateActivity;
import com.example.a79069.zhihu.newsDetail.NewsDetailActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;



import static android.content.ContentValues.TAG;

/**
 * Created by 79069 on 2017/3/23.
 */

public class NewsListFragment extends Fragment implements NewsListContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String TITLE = "com.example.a79069.zhihu.newsList.map_key_title";


    public static NewsListFragment newInstance(){

        return new NewsListFragment();

    }

    private NewsListContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private NewsListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SlidingMenu mSlidingMenu;

    private SlideAndDragListView<String> mStringSlideAndDragListView;

    private StringDateAdapter mStringDateAdapter;

    private List<String> mStringDateList;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){

                setAdapter((NewsSimpleList) msg.obj);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //初始化SimpleListData
        initDate();

        //启动服务
        startService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list , container , false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home_btn);

        actionBar.setTitle(R.string.today_news);

        //初始化和设置SlidingMenu
        initSlidingMenu();

        //初始化SlidingAndDragListView
        initSlidingAndDragListView();


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


    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mSlidingMenu.showMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
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
     * 启动服务
     */
    @Override
    public void startService() {
        Intent intent = MyService.newIntent(getActivity());

        getActivity().startService(intent);
    }


    /**
     * 初始化和设置SlidingMenu
     */
    @Override
    public void initSlidingMenu() {
        mSlidingMenu = new SlidingMenu(getActivity());

        mSlidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        mSlidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_width);

        mSlidingMenu.attachToActivity(getActivity() , SlidingMenu.SLIDING_CONTENT);
        //设置侧滑菜单布局
        mSlidingMenu.setMenu(R.layout.view_sliding_menu);
    }

    /**
     * 初始化SlidingAndDragListView
     */
    @Override
    public void initSlidingAndDragListView() {
        mStringSlideAndDragListView = (SlideAndDragListView<String>) mSlidingMenu.findViewById(R.id.slide_and_drag_listview);


        Menu menu = new Menu(true, true, 0);//第1个参数表示在拖拽的时候 item 的背景是否透明，第2个参数表示滑动item是否能滑的过头，像弹簧那样(true表示过头，就像Gif中显示的那样；false表示不过头，就像Android QQ中的那样)
        menu.addItem(new MenuItem.Builder().setWidth(90)//单个菜单button的宽度
                .setBackground(new ColorDrawable(Color.RED))//设置菜单的背景
                .setText("One")//set text string
                .setTextColor(Color.GRAY)//set text color
                .setTextSize(20)//set text size
                .build());
        menu.addItem(new MenuItem.Builder().setWidth(120)
                .setBackground(new ColorDrawable(Color.BLACK))
                .setDirection(MenuItem.DIRECTION_RIGHT)//设置方向 (默认方向为DIRECTION_LEFT)
                .build());
//set in sdlv
        mStringSlideAndDragListView.setMenu(menu);

        mStringDateAdapter = new StringDateAdapter(getActivity() , mStringDateList);

        mStringSlideAndDragListView.setAdapter(mStringDateAdapter);



        mStringSlideAndDragListView.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
            @Override
            public void onSlideOpen(View view, View parentView, int position, int direction) {

            }

            @Override
            public void onSlideClose(View view, View parentView, int position, int direction) {

            }
        });



        mStringSlideAndDragListView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        switch (buttonPosition) {
                            case 0://One
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0://icon
                                return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        break;
                    default :
                        return Menu.ITEM_NOTHING;
                }
                return 0;
            }
        });



        //设置长按监听
        mStringSlideAndDragListView.setOnListItemLongClickListener(new SlideAndDragListView.OnListItemLongClickListener() {
            @Override
            public void onListItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "长按list" + position, Toast.LENGTH_SHORT).show();
            }
        });

        //设置拖放监听，一定要设置了长按监听，设置拖放监听才有效
        mStringSlideAndDragListView.setOnDragListener(new SlideAndDragListView.OnDragListener() {
            @Override
            public void onDragViewStart(int position) {

            }

            @Override
            public void onDragViewMoving(int position) {

            }

            @Override
            public void onDragViewDown(int position) {

            }
        } , mStringDateList);


    }


    /**
     * 启动详细页面
     * @param address
     */
    @Override
    public void showNewsDetail(String address) {

        Intent intent = NewsDetailActivity.newIntent(getActivity() , address);

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
     * 初始化数据
     */
    private void initDate(){
        mStringDateList = new ArrayList<>();
        mStringDateList.add("设计模式");
        mStringDateList.add("电影日报");
        mStringDateList.add("日常心理学");
        mStringDateList.add("用户推荐日报");
        mStringDateList.add("大公司日报");
        mStringDateList.add("不许无聊");
    }


    /**
     * 适配器
     */
    private class ViewHolder{
        TextView title;
    }
    private class StringDateAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        private List<String> mDataList;

        public StringDateAdapter(Context context , List<String> dataList){
            inflater = LayoutInflater.from(context);
            mDataList = dataList;
        }


        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;

            if(view == null){
                view = inflater.inflate(R.layout.view_slide_and_drag_list_view_item , viewGroup , false);
                viewHolder = new ViewHolder();

                viewHolder.title = (TextView) view.findViewById(R.id.simple_list_title);

                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.title.setText(mDataList.get(i));
            return view;
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


            Glide.with(getActivity()).load(image).into(mImageView);
        }


        @Override
        public void onClick(View view) {
            showNewsDetail("http://news-at.zhihu.com/api/4/news/" + mNewsSimple.getId());
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
