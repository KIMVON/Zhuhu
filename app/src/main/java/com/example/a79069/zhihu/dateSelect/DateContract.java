package com.example.a79069.zhihu.dateSelect;

import com.example.a79069.zhihu.BasePresenter;
import com.example.a79069.zhihu.BaseView;

import java.util.Date;

/**
 * Created by 79069 on 2017/3/27.
 */

public interface DateContract {
    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{
        void showOtherDayNewsListActivity(Date date);

    }
}
