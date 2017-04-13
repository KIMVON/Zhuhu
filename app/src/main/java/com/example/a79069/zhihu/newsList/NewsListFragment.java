package com.example.a79069.zhihu.newsList;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.app.MyService;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.NewsTheme;
import com.example.a79069.zhihu.dateSelect.DateActivity;
import com.example.a79069.zhihu.favorites.FavoritesActivity;
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

    public static NewsListFragment newInstance() {

        return new NewsListFragment();

    }

    private NewsListContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private NewsListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SlidingMenu mSlidingMenu;

    private SlideAndDragListView<NewsTheme> mStringSlideAndDragListView;

    private StringDateAdapter mStringDateAdapter;

    private List<NewsTheme> mNewsThemeList;

    private ViewFlipper mViewFlipper;

    private PopupWindow mPopupWindow;

    private LinearLayout mLoginLinearLayoutBtn;

    private LinearLayout mFavoritesBtn;

    private LinearLayout mMessagesBtn;

    private LinearLayout mSettingBtn;

    private TextView mTitleTextView;

    private FloatingActionButton mFloatingActionButton;

    //侧滑菜单里面回到首页的按钮
    private LinearLayout mHomeBtn;

    //主题id
    private String mThemeId;

    //判断是首页还是其他主题
    private boolean isTodayNews;

    //开始的X坐标 因为只是横向滑动
    private float startX;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ((NewsListPresenter) mPresenter).LOAD_TODAY_NEWS) {

                List<NewsSimple> list = ((NewsSimpleList) msg.obj).getNewsSimpleList();

                setAdapter(list);

                /**
                 * 加载ViewFlipper
                 */
                mViewFlipper.removeAllViews();
                for (int i = 0; i < 4; i++) {
                    int randomInt = (int) (Math.random() * list.size());

                    addImageViewToViewFlipper(list.get(randomInt).getImage());
                }

                startViewFlipperAnimation();

            } else if (msg.what == ((NewsListPresenter) mPresenter).LOAD_NEWS_THEME) {
                mNewsThemeList = (List<NewsTheme>) msg.obj;
                setSlidingAdapter((List<NewsTheme>) msg.obj);
            } else if (msg.what == ((NewsListPresenter) mPresenter).LOAD_NEWS_THEME_CONTENT) {

                setAdapter((List<NewsSimple>) msg.obj);

                mViewFlipper.stopFlipping();

            }else if (msg.what == ((NewsListPresenter) mPresenter).REFRESH_NEWS){
                List<NewsSimple> list = (List<NewsSimple>) msg.obj;

                mAdapter = null;

                setAdapter(list);

                Log.d(TAG, "handleMessage: ++++++++++++++++++");

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置默认值为true
        isTodayNews = true;

        setHasOptionsMenu(true);

        //启动服务
        startService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home_btn);

        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);

        mViewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);

        //初始化和设置SlidingMenu
        initSlidingMenu();

        //初始化SlidingAndDragListView
        initSlidingAndDragListView();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_news_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(this);


        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pick_date);
        mFloatingActionButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart();
        mPresenter.onLoad(mHandler);

    }


    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mSlidingMenu.showMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化主界面的Adapter
     *
     * @param newsSimpleList
     */
    @Override
    public void setAdapter(List<NewsSimple> newsSimpleList) {
        if (mAdapter == null) {
            mAdapter = new NewsListAdapter(newsSimpleList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 点击侧滑菜单的按钮点击事件
     */
    @Override
    public void onClickHomeBtn() {
        //让FloatingActionButton显现出来
        mFloatingActionButton.setVisibility(View.VISIBLE);

        //设置标题为今日热点
        mTitleTextView.setText(R.string.today_news);

        //必须设置为null
        mAdapter = null;

        //设置isTodayNews为true
        isTodayNews = true;

        //显示今日热点
        mPresenter.getTodayNews(mHandler);

        //关闭侧滑菜单
        mSlidingMenu.showContent();
    }


    /**
     * 初始化侧滑菜单ListView Adapter
     *
     * @param newsThemeList
     */
    @Override
    public void setSlidingAdapter(final List<NewsTheme> newsThemeList) {
        if (mStringDateAdapter == null) {
            mStringDateAdapter = new StringDateAdapter(getActivity(), newsThemeList);

            mStringSlideAndDragListView.setAdapter(mStringDateAdapter);
        } else {
            mStringDateAdapter.notifyDataSetChanged();
        }


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
                mStringDateAdapter.notifyDataSetChanged();

            }
        }, mNewsThemeList);

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

        mSlidingMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        //设置侧滑菜单布局
        mSlidingMenu.setMenu(R.layout.view_sliding_menu);

        mLoginLinearLayoutBtn = (LinearLayout) mSlidingMenu.findViewById(R.id.login_linearlayout_btn);

        mFavoritesBtn = (LinearLayout) mSlidingMenu.findViewById(R.id.favorites_btn);

        mMessagesBtn = (LinearLayout) mSlidingMenu.findViewById(R.id.messages_btn);

        mSettingBtn = (LinearLayout) mSlidingMenu.findViewById(R.id.settings_btn);

        mHomeBtn = (LinearLayout) mSlidingMenu.findViewById(R.id.home_page_btn);

        mLoginLinearLayoutBtn.setOnClickListener(this);

        mFavoritesBtn.setOnClickListener(this);
        mMessagesBtn.setOnClickListener(this);
        mSettingBtn.setOnClickListener(this);

        mHomeBtn.setOnClickListener(this);

        mSlidingMenu.setFitsSystemWindows(true);
    }

    /**
     * 初始化SlidingAndDragListView
     */
    @Override
    public void initSlidingAndDragListView() {
        mStringSlideAndDragListView = (SlideAndDragListView<NewsTheme>) mSlidingMenu.findViewById(R.id.slide_and_drag_listview);


        //第1个参数表示在拖拽的时候 item 的背景是否透明，
        // 第2个参数表示滑动item是否能滑的过头，像弹簧那样(true表示过头，就像Gif中显示的那样；false表示不过头，就像Android QQ中的那样)
        Menu menu = new Menu(true, true, 0);

        mStringSlideAndDragListView.setMenu(menu);

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
                    default:
                        return Menu.ITEM_NOTHING;
                }
                return 0;
            }
        });


        //侧滑菜单的item项的点击事件
        mStringSlideAndDragListView.setOnListItemClickListener(new SlideAndDragListView.OnListItemClickListener() {
            @Override
            public void onListItemClick(View v, int position) {
                NewsTheme newsTheme = mNewsThemeList.get(position);
                mThemeId = newsTheme.getId();
                String description = newsTheme.getDescription();
                String headerImage = newsTheme.getHeaderImage();
                String titleName = newsTheme.getName();

                //设置头部的Action的名字
                mTitleTextView.setText(titleName);

                //关闭侧滑菜单
                mSlidingMenu.showContent();
                //隐藏右下角的FloatingActionButton
                mFloatingActionButton.setVisibility(View.GONE);

                //这里必须把mAdapter设置为nll
                mAdapter = null;

                //把isTodayNews设置为false
                isTodayNews = false;

                mPresenter.getNewsThemeContent("http://news-at.zhihu.com/api/4/theme/" + mThemeId, mHandler);

                //清除ViewFlipper里面的所有image
                mViewFlipper.removeAllViews();

                ImageView imageView = new ImageView(getActivity());
                //铺满屏幕
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(getActivity()).load(headerImage).into(imageView);

                mViewFlipper.addView(imageView);
            }
        });


        //设置长按监听
        mStringSlideAndDragListView.setOnListItemLongClickListener(new SlideAndDragListView.OnListItemLongClickListener() {
            @Override
            public void onListItemLongClick(View view, int position) {
            }
        });

    }


    /**
     * 启动详细页面
     *
     * @param address
     */
    @Override
    public void showNewsDetail(String address) {

        Intent intent = NewsDetailActivity.newIntent(getActivity(), address);

        startActivity(intent);
    }


    @Override
    public void showSearchActivity() {

    }


    /**
     * 启动登陆页面
     */
    @Override
    public void showLoginPopWindow() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_user_login_pop_window, (ViewGroup) this.getView(), false);

        ImageView backtrackBtn = (ImageView) view.findViewById(R.id.backtrack_btn);
        Button loginByZhihuBtn = (Button) view.findViewById(R.id.login_by_zhihu);
        ImageView loginBySinaWeiboBtn = (ImageView) view.findViewById(R.id.sina_weibo_btn);
        ImageView loginByTecentWeiboBtn = (ImageView) view.findViewById(R.id.tecent_weibo_btn);

        //防止冲突写在里面
        backtrackBtn.setOnClickListener(this);
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


        mPopupWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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


    /**
     * 启动我的收藏页面
     */
    @Override
    public void showMyFavoritesActivity() {
        Intent intent = FavoritesActivity.newIntent(getActivity());

        startActivity(intent);
    }


    /**
     * 启动ViewFlipper
     */
    @Override
    public void startViewFlipperAnimation() {
        /**
         * 设置自动切换
         */
        //设置动画效果
        //进来的时候的动画
        mViewFlipper.setInAnimation(getActivity(), R.anim.left_in);
        //出去的时候的动画
        mViewFlipper.setOutAnimation(getActivity(), R.anim.left_out);
        //设置视图切换的时间间隔
        mViewFlipper.setFlipInterval(3000);
        //开始播放
        mViewFlipper.startFlipping();
    }

    /**
     * 增加图片到ViewFlipper
     *
     * @param url
     */
    @Override
    public void addImageViewToViewFlipper(String url) {
        ImageView imageView = new ImageView(getActivity());
        //设置ImageView铺满
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getActivity()).load(url.substring(2, url.length() - 2).replaceAll("\\\\", "")).into(imageView);
        //动态导入的方式为ViewFlipper加入子View
        mViewFlipper.addView(imageView);
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
        if (isTodayNews){
            mPresenter.refreshNews("http://news-at.zhihu.com/api/4/news/latest" ,mHandler);
        }else {
            mPresenter.refreshNews("http://news-at.zhihu.com/api/4/theme/" + mThemeId ,mHandler);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_pick_date:
                showDateActivity();
                break;
            case R.id.login_linearlayout_btn:
                showLoginPopWindow();
                break;
            case R.id.backtrack_btn:
                mPopupWindow.dismiss();
                break;


            //SlidingMenu里面的点击事件
            case R.id.favorites_btn:
                showMyFavoritesActivity();
                break;
            case R.id.messages_btn:
                break;
            case R.id.settings_btn:
                break;

            //SlidingMenu里面的首页Button
            case R.id.home_page_btn:
                onClickHomeBtn();
                break;


            case R.id.login_by_zhihu:
                break;
            case R.id.sina_weibo_btn:
                break;
            case R.id.tecent_weibo_btn:
                break;
        }
    }


    /**
     * 侧滑菜单适配器
     */
    private class ViewHolder {
        TextView title;
    }

    private class StringDateAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        private List<NewsTheme> mDataList;

        public StringDateAdapter(Context context, List<NewsTheme> dataList) {
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

            if (view == null) {
                view = inflater.inflate(R.layout.view_slide_and_drag_list_view_item, viewGroup, false);
                viewHolder = new ViewHolder();

                viewHolder.title = (TextView) view.findViewById(R.id.simple_list_title);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.title.setText(mDataList.get(i).getName());
            return view;
        }
    }


    /**
     * 主页面的适配器
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
         *
         * @param imageURL
         * @param title
         * @param newsSimple
         */
        public void onBindView(String imageURL, String title, NewsSimple newsSimple) {
            mNewsSimple = newsSimple;

            mTitleTextView.setText(title);

            if (imageURL != null) {
                String image = imageURL.substring(2, imageURL.length() - 2).replaceAll("\\\\", "");
                Glide.with(getActivity()).load(image).into(mImageView);
            }

        }


        @Override
        public void onClick(View view) {
            showNewsDetail("http://news-at.zhihu.com/api/4/news/" + mNewsSimple.getId());
        }
    }

    private class NewsListAdapter extends RecyclerView.Adapter<NewsListViewHolder> {
        private List<NewsSimple> mNewsSimpleList;

        public NewsListAdapter(List<NewsSimple> newsSimpleList) {
            mNewsSimpleList = newsSimpleList;

        }

        @Override
        public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news_list_item, parent, false);


            return new NewsListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsListViewHolder holder, int position) {
            NewsSimple newsSimple = mNewsSimpleList.get(position);
            String imageURL = newsSimple.getImage();
            String title = newsSimple.getTitle();

            holder.onBindView(imageURL, title, newsSimple);
        }

        @Override
        public int getItemCount() {
            return mNewsSimpleList.size();
        }

    }
}
