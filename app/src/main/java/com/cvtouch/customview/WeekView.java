package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/13
 */
public class WeekView extends View{
    private final int DEFAULT_HEIGHT=20;
    private final int DEFAULT_WIDTH=300;
    private final DisplayMetrics mDisplayMetrics;
    private float mWeekTextSize=35;
    private int mWeekTextColor= Color.BLACK;
    private String[] mWeekText=new String[]{"日","一","二","三","四","五","六"};
    private int mBackground=0xfff9f9f9;

    public WeekView(Context context) {
        this(context,null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        drawBackGround(canvas,paint);
        drawWeek(canvas,paint);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) (mDisplayMetrics.density * DEFAULT_HEIGHT);
        }
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) (mDisplayMetrics.density * DEFAULT_WIDTH);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private void drawBackGround(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mBackground);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
    }
    private void drawWeek(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(mWeekTextSize);
        paint.setColor(mWeekTextColor);
        int columnWidth = getWidth() / 7;
        for(int i=0;i < mWeekText.length;i++){
            String text = mWeekText[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth)/2;
            int startY =(int) (getHeight()/2 - (paint.ascent() + paint.descent())/2);
            canvas.drawText(text, startX, startY, paint);
        }
    }
}
