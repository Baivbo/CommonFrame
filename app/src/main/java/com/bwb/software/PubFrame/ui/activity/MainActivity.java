package com.bwb.software.PubFrame.ui.activity;

import com.bwb.software.PubFrame.R;
import com.bwb.software.PubFrame.base.BaseActivity;
import com.bwb.software.PubFrame.date.EventEntity.TestEvent;
import com.bwb.software.PubFrame.net.Exception.NetExceptionUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        //判断网络状态
        int netType = NetExceptionUtils.getAPNType(this);
        if (netType == 0) {
            Crouton.makeText(this, "当前无网络", Style.INFO).show();
        } else if (netType == 1) {
            Crouton.makeText(this, "当前为WIFI连接状态", Style.INFO).show();
        } else if (netType == 2) {
            Crouton.makeText(this, "当前为移动、电信3G连接状态", Style.INFO).show();
        } else if (netType == 3) {
            Crouton.makeText(this, "当前为移动、电信2G连接状态", Style.INFO).show();
        } else if (netType == 4) {
            Crouton.makeText(this, "当前为4G连接状态", Style.INFO).show();
        }
    }

    @Override
    public void initDate() {
        showPhpToast("This is Toast", R.mipmap.ic_toast);


    }

    @OnClick(R.id.tx_bt)
    public void onViewClicked() {
        EventBus.getDefault().postSticky(new TestEvent("EventBus跳转传值"));
        startActivity(LoginActivity.class, false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusTest(TestEvent event){
        showLongToast(event.getMes());
    }*/
}
