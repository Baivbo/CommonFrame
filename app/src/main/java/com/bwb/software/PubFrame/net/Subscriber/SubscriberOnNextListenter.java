package com.bwb.software.PubFrame.net.Subscriber;

/**
 * Created by baiweibo on 2018/11/13.
 */

//将onNext方法中的数据传递给activity，让activity去处理逻辑。
public interface SubscriberOnNextListenter<T> {
    void OnNext(T t);
}
