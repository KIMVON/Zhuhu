package com.example.a79069.zhihu.dateSelect;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.otherDayNewsList.OtherDayNewsListActivity;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 79069 on 2017/3/27.
 */

public class DateFragment extends Fragment implements DateContract.View {

    public static Fragment newInstance(){
        Fragment fragment = new DateFragment();

        return fragment;
    }


    private DateContract.Presenter mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_date , container , false);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.news_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Calendar nextDay = Calendar.getInstance();
        nextDay.add(Calendar.DAY_OF_YEAR, 1);

        CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        Date birthday = new Date(113, 4, 19);
        Date today = new Date();



        /**
         init(Date selectedDate, Date minDate, Date maxDate) {...}
         selectedDate 当前选中日期
         minDate 对早可选日期 （包含）
         maxDate 最晚可选日期（不包含）
         calender.init
         **/
        calendar.init(birthday, nextDay.getTime())
                .withSelectedDate(today);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                showOtherDayNewsListActivity(date);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });


        return view;
    }


    @Override
    public void showOtherDayNewsListActivity(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        String dateString = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        Intent intent = OtherDayNewsListActivity.newIntent(getActivity() , dateString);
        startActivity(intent);
    }


    @Override
    public void setPresenter(DateContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
