package com.example.a79069.zhihu.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a79069.zhihu.R;
import com.example.a79069.zhihu.data.NewsDetail;
import com.example.a79069.zhihu.data.NewsSimple;
import com.example.a79069.zhihu.data.NewsSimpleList;
import com.example.a79069.zhihu.data.source.AppRepository;
import com.example.a79069.zhihu.data.source.DataSource;
import com.example.a79069.zhihu.newsDetail.NewsDetailActivity;
import com.example.a79069.zhihu.util.ActivityUtils;

import java.util.List;
import java.util.Random;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getService;

/**
 * Created by 79069 on 2017/3/28.
 */

public class MyService extends Service {
    public static Intent newIntent(Context context){
        Intent intent = new Intent(context , MyService.class);

        return intent;
    }


    private static final int NOTIFICATION_ID_TO_ACTIVITY = 0;

    private NewsBinder mNewsBinder = new NewsBinder();

    private AppRepository mAppRepository;

    private List<NewsSimple> mNewsSimpleList;

    private NewsSimple mNewsSimple;

    private NotificationManager mNotificationManager;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                NewsSimpleList newsSimpleList = (NewsSimpleList) msg.obj;

                mNewsSimpleList = newsSimpleList.getNewsSimpleList();


                int randomInt = (int) (Math.random() * mNewsSimpleList.size());


                mNewsSimple = mNewsSimpleList.get(randomInt);

                showNotification(mNewsSimple);
            }

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mAppRepository = ActivityUtils.getAppRepository();
        mAppRepository.getHotNewsList(new DataSource.NewsSimpleListCallback() {
            @Override
            public void onSuccess(NewsSimpleList newsSimpleList) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = newsSimpleList;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailed() {

            }
        });


        /**
         * 定时启动后台
         */
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int timeDur = 1000*60*3;
        long duration = SystemClock.elapsedRealtime() + timeDur;
        Intent intentService = new Intent(this , MyService.class);
        PendingIntent pendingIntent = getService(this , 0 , intentService , 0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , duration , pendingIntent);


        return super.onStartCommand(intent, flags, startId);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mNewsBinder;
//        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
    }

    public void showNotification(NewsSimple newsSimple){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = NewsDetailActivity.newIntent(this , "http://news-at.zhihu.com/api/2/news/"+ mNewsSimple.getId());

        /**
         * http://blog.sina.com.cn/s/blog_60469fe80101egib.html
         * 用PendingIntent传送数据丢失解决办法
         *
         * int FLAG_CANCEL_CURRENT：如果该PendingIntent已经存在，则在生成新的之前取消当前的。
         * int FLAG_NO_CREATE：如果该PendingIntent不存在，直接返回null而不是创建一个PendingIntent.
         * int FLAG_ONE_SHOT:该PendingIntent只能用一次，在send()方法执行后，自动取消。
         * int FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据。
         * 我们需要把最后一个参数改为PendingIntent.FLAG_UPDATE_CURRENT,这样在启动的Activity里就可以用接收Intent传送数据的方法正常接收。
         *
         */
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , intent ,  FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(newsSimple.getTitle())
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeFile(newsSimple.getImage()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();


        mNotificationManager.notify(NOTIFICATION_ID_TO_ACTIVITY , notification);
    }


    public class NewsBinder extends Binder{

    }

}
