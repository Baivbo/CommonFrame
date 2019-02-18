package com.bwb.software.PubFrame.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bwb.software.PubFrame.R;

/**
 * Created by baiweibo on 2018/11/16.
 */

public class CustomZoomView extends View {

    private Bitmap goods;//包裹
    private Bitmap people;//人物
    private Bitmap peopleWithGoods;//测量view宽高
    private int measuredWidth;  //测量后的宽
    private int measuredHeight; //测量后的高
    private float mCurrentProgress; //进度值
    private int mCurrentAlpha; //透明度
    private Paint mPaint;//画笔
    private Bitmap scaledPeople; //缩放后的人物
    private Bitmap scaledGoods; //缩放后的包裹


    public CustomZoomView(Context context) {
        super(context);
        init();
    }


    public CustomZoomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    private void init() {
        goods = BitmapFactory.decodeResource(getResources(), R.mipmap.app_refresh_goods_0);

        people = BitmapFactory.decodeResource(getResources(), R.mipmap.app_refresh_people_0);

        peopleWithGoods = BitmapFactory.decodeResource(getResources(), R.mipmap.app_refresh_people_3);

        mPaint = new Paint();
        //设置透明实现渐变效果
        mPaint.setAlpha(0);
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    //测量宽度
    private int measureWidth(int widthMeasureSpe) {
        int result = 0;
        int size = MeasureSpec.getSize(widthMeasureSpe);
        int mode = MeasureSpec.getMode(widthMeasureSpe);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        } else {
            result = peopleWithGoods.getWidth();
            if (MeasureSpec.AT_MOST == mode) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    //测量高度
    private int measureHeight(int heightMeasureSpe) {
        int result = 0;
        int size = MeasureSpec.getSize(heightMeasureSpe);
        int mode = MeasureSpec.getMode(heightMeasureSpe);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        } else {
            result = peopleWithGoods.getHeight();
            if (MeasureSpec.AT_MOST == mode) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    /**
     * 拿到测量后的宽和高
     *
     * @param w    宽
     * @param h    高
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        measuredWidth = w;
        measuredHeight = h;

        //根据测量后的宽高对人物做一个缩放
        scaledPeople = Bitmap.createScaledBitmap(people, measuredWidth, measuredHeight, true);
        //根据测量后的宽高对包裹做一个缩放
        scaledGoods = Bitmap.createScaledBitmap(goods,scaledPeople.getWidth()*10/27,scaledPeople.getHeight()/5,true);
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        //缩放
        canvas.scale(mCurrentProgress,mCurrentProgress,measuredWidth-scaledGoods.getWidth()/2,measuredHeight/2);
        mPaint.setAlpha(mCurrentAlpha);
        canvas.drawBitmap(scaledGoods,measuredWidth-scaledGoods.getWidth(),measuredHeight/2-scaledGoods.getHeight()/2,mPaint);
        canvas.restore();

        canvas.save();
        canvas.scale(mCurrentProgress,mCurrentProgress,0,measuredHeight/2);
        mPaint.setAlpha(mCurrentAlpha);
        canvas.drawBitmap(scaledPeople,0,0,mPaint);
        canvas.restore();
    }

    /**
     * 根据进度值对图片进行缩放
     * @param currentProgress
     */
    public void setmCurrentProgress(float currentProgress){
        this.mCurrentProgress=currentProgress;
        this.mCurrentAlpha= (int) (currentProgress*255);
    }
}
