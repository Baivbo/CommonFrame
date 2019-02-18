package com.bwb.software.PubFrame.example;

import android.util.Log;

import com.bwb.software.PubFrame.base.BaseActivity;
import com.bwb.software.PubFrame.date.MovieEntity;
import com.bwb.software.PubFrame.net.HttpMethods;
import com.bwb.software.PubFrame.net.Subscriber.ProgressSubscriber;
import com.bwb.software.PubFrame.net.Subscriber.SubscriberOnNextListenter;

/**
 * Created by baiweibo on 2018/11/16.
 */

public class NetExample extends BaseActivity {
    @Override
    public void initView() {

    }

    @Override
    public void initDate() {
        HttpMethods.getInstance().getTopMovie(0, 10, new ProgressSubscriber<MovieEntity>(NetExample.this, new SubscriberOnNextListenter<MovieEntity>() {
            @Override
            public void OnNext(MovieEntity movieEntity) {
                Log.d("返回结果", movieEntity.toString());
            }
        }));
    }

    @Override
    protected int getLayout() {
        return 0;
    }
}
