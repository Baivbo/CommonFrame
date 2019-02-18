package com.bwb.software.PubFrame.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.bwb.software.PubFrame.R;
import com.bwb.software.PubFrame.base.BaseActivity;
import com.bwb.software.PubFrame.date.EventEntity.TestEvent;
import com.bwb.software.PubFrame.ui.custom.CustomZoomView;
import com.bwb.software.PubFrame.ui.custom.CustomFrameView;
import com.bwb.software.PubFrame.ui.custom.dialog.AdvertDialog;
import com.bwb.software.PubFrame.ui.custom.dialog.TextDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by baiweibo on 2018/11/7.
 */

public class LoginActivity extends BaseActivity {

    private SeekBar bar;
    private CustomZoomView mLoadDrawView1;
    private CustomFrameView mLoadDrawView2;

    private AnimationDrawable secondAnimation;

    Button dialogbt,dialogbt2,dialogbt3;
    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        bar=findViewById(R.id.seekbar);
        mLoadDrawView1=findViewById(R.id.loadDrawView);
        mLoadDrawView2=findViewById(R.id.loadDrawView2);
        dialogbt=findViewById(R.id.button);
        dialogbt2=findViewById(R.id.button2);
        dialogbt3=findViewById(R.id.button3);
    }

    @Override
    public void initDate() {
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float currentProgress= (float) seekBar.getProgress()/(float) seekBar.getMax();
                mLoadDrawView1.setmCurrentProgress(currentProgress);
                mLoadDrawView1.invalidate();


                if(seekBar.getProgress()==seekBar.getMax()){
                    mLoadDrawView2.setBackgroundResource(R.drawable.view_load_animation);
                    mLoadDrawView1.setVisibility(View.GONE);
                    mLoadDrawView2.setVisibility(View.VISIBLE);
                    secondAnimation= (AnimationDrawable) mLoadDrawView2.getBackground();
                    secondAnimation.start();
                }else {
                    mLoadDrawView1.setVisibility(View.VISIBLE);
                    mLoadDrawView2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            if(seekBar.getProgress()!=seekBar.getMax()){
                mLoadDrawView1.setVisibility(View.VISIBLE);
                mLoadDrawView2.setVisibility(View.GONE);
            }

            }
        });

        dialogbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("photourl","https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp");
                AdvertDialog dialog=new AdvertDialog();
                dialog.setArguments(bundle);
                dialog.setDimAmout(0.8f).show(getFragmentManager(),"dialog_fr");

            }
        });

        dialogbt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TextDialog().show(getFragmentManager(),"dialog_tx");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(secondAnimation!=null){
            secondAnimation.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(secondAnimation!=null){
            secondAnimation.stop();
        }

    }

    @Override
    protected String getToolbarTitle() {
        return "尼古拉斯赵四";
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void EventBusTest(TestEvent event) {
        showLongToast(event.getMes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
