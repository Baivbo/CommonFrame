package com.bwb.software.PubFrame.net;

import com.bwb.software.PubFrame.date.MovieEntity;
import com.bwb.software.PubFrame.net.Scheduler.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.DefaultObserver;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baiweibo on 2018/11/7.
 */

public class HttpMethods {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private NetAPi netApi;

    //先构造私有的构造方法
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        netApi = retrofit.create(NetAPi.class);
    }

    //创建单例
    public static class SingleonHolder {
        private static final HttpMethods instance = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingleonHolder.instance;
    }


/*============================================================分割线=========================================================================*/

    /**
     *
     * @param start 起始位置
     * @param count 获取长度
     * @param subscriber 传递过来的观察者对象
     */
    public void getTopMovie(int start, int count, DefaultObserver<MovieEntity> subscriber) {
        netApi.getTopMovie(start, count)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(subscriber);
    }

}
