package com.example.a79069.zhihu.util;


import com.example.a79069.zhihu.data.source.DataSource;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 79069 on 2017/3/23.
 */

public class HttpUtil {
    /**
     * 用HttpURLConntection请求返回JSON
     * @param address
     * @param callback
     */
    public static void sendRequestWithHttpURLConntection(final String address , final DataSource.JSONCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder respone = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        respone.append(line);
                    }

                    if (callback != null){
                        callback.onSuccess(respone.toString());
                    }else {
                        callback.onFailed();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    connection.disconnect();
                }

            }
        }).start();
    }

    /**
     * 用OkHttp请求返回JSON
     * @param address
     * @param callback
     */
    public static void sendRequestWithOkHttp(String address , Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }



    public static String sendRequestWithOkHttp(String address){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
