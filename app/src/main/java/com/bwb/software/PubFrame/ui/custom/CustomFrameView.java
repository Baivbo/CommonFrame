package com.bwb.software.PubFrame.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bwb.software.PubFrame.R;

/**
 * Created by baiweibo on 2018/11/16.
 */

public class CustomFrameView extends View{

    private Bitmap mBitmap;

    public CustomFrameView(Context context) {
        super(context);
        init();
    }


    public CustomFrameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.app_refresh_people_3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        }else {
            result = mBitmap.getWidth();
            if (MeasureSpec.AT_MOST == mode) {
                result = Math.min(size, result);
            }
        }
        return result;
    }
    private int measureHeight(int heightMeasureSpec){
        int result = 0;
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        }else {
            result = mBitmap.getHeight();
            if (MeasureSpec.AT_MOST == mode) {
                result = Math.min(size, result);
            }
        }
        return result;
    }
}
