package com.bwb.software.PubFrame.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwb.software.PubFrame.R;
import com.bwb.software.PubFrame.callback.IBaseView;

import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by baiweibo on 2018/11/7.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView{

    private static final String TAG = AppCompatActivity.class.getSimpleName();
    @Nullable
    private Toast mToast;
    private Toolbar mToolbar;
    private LayoutInflater mInflater;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //是否显示title
        if (!isNoTitle()) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        //获取xml布局文件
        if (getLayout() != 0) {
            setContentView(getLayout());
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mInflater = getLayoutInflater();

        //初始化toobar
        if (isNoTitle()) {
            initToolbar(mToolbar);
        }

        //可删除类中自动生成的onCreat方法
        ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        initDate();
    }



    //已在styles文件中设置  暂不调用
    public void setTranslucentStatus() {
        /**
         * 状态栏透明
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    //Toast
    public void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            if (mToast == null)
                mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.setText(message);
            mToast.show();
        }
    }

    public void showLongToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            if (mToast == null)
                mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            mToast.setText(message);
            mToast.show();
        }
    }

    public void showCenterToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            if (mToast == null)
                mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
            mToast.setText(message);
            mToast.show();
        }
    }

    public void showPhpToast(String message,int drawable) {
        if (!TextUtils.isEmpty(message)) {
            if (mToast == null)
                mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageCodeProject = new ImageView(getApplicationContext());
            imageCodeProject.setImageResource(drawable);
            toastView.setBackgroundColor(Color.TRANSPARENT);
            toastView.addView(imageCodeProject, 0);

            mToast.setText(message);
            mToast.show();
        }
    }

    //顶部Toast
    public void showTobToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Crouton.makeText(this,message, Style.INFO).show();
        }
    }

    //启动Activity

    /**
     *
     * @param clazz  目标类名
     * @param isFinish 当前类是否销毁
     */
    public void startActivity(Class clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        if (isFinish) {
            finish();
        }
    }

    protected void init(Bundle savedInstanceState) {

    }

    protected void initToolbar(Toolbar toolbar) {

        if (toolbar == null)
            return;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        //使用自定义的toolbar
        if (!getDefaultToolbar()) {
            int customView = getToolbatCustomView();
            if (customView != 0) {
                View view = getInflateView(getToolbatCustomView());
                Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                toolbar.addView(view, layoutParams);
                title = (TextView) view.findViewById(R.id.toolbar);
                String toolbarTitle = getToolbarTitle();
                if (null != title && !TextUtils.isEmpty(toolbarTitle)) {
                    title.setText(toolbarTitle);
                }
            }

        } else {
            title = (TextView) toolbar.findViewById(R.id.toolbar_title);
            String toolbarTitle = getToolbarTitle();
            if (null != title && !TextUtils.isEmpty(toolbarTitle)) {
                title.setText(toolbarTitle);
            }
        }

        //是否显示返回按钮  默认fales
        if (hasBackButton()) {
            toolbar.setNavigationIcon(R.mipmap.activity_return);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    protected boolean hasBackButton() {
        return false;
    }

    //设置toobar标题
    protected String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    protected View getInflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    protected int getToolbatCustomView() {
        return 0;
    }

    protected boolean getDefaultToolbar() {
        return true;
    }

    protected abstract int getLayout();

    protected boolean isNoTitle() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    //为TextView设置
    public void setTVtext(TextView tv, String msg) {
        if (tv != null) {
            if (msg != null && !msg.equals("")) {
                if (msg.equals("0") || msg.equals("0.0")) {
                    tv.setText("--");
                } else {
                    tv.setText(msg);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
