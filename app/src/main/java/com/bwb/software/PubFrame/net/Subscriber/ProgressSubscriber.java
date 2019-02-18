package com.bwb.software.PubFrame.net.Subscriber;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.bwb.software.PubFrame.net.Exception.NetExceptionUtils;
import com.bwb.software.PubFrame.net.Exception.RxExceptionUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by baiweibo on 2018/11/13.
 */

/**
 * //设置加载框
 * RxJava2.1.3中Subscriber改成了DefaultOberver了,里面也有onStart()方法。
 * @param <T>
 */
public class ProgressSubscriber<T> extends DefaultObserver<T> implements ProgressCancelListener{

    private ProgressDialogHandler mProgressDialogHandler;
    private SubscriberOnNextListenter mSubscriberOnNextListenter;
    private Context context;

    public ProgressSubscriber(Context context,SubscriberOnNextListenter mSubscriberOnNextListenter) {
        this.mSubscriberOnNextListenter = mSubscriberOnNextListenter;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListenter.OnNext(t);
    }

    @Override
    public void onError(Throwable e) {
        int netType= NetExceptionUtils.getAPNType(context);
        if(netType==0){
            Crouton.makeText((Activity) context,"当前无网络", Style.INFO).show();
        }else
            Crouton.makeText((Activity) context,RxExceptionUtils.exceptionHandler(e), Style.INFO).show();

        dismissProgressDialog();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {

    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }
}
