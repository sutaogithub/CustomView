package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Calendar;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/13
 */
public class DateTitleView extends View{

    private final int DEFAULT_HEIGHT=50;
    private final int DEFAULT_WIDTH=300;
    private final DisplayMetrics mDisplayMetrics;
    private int mTitleSize=70;
    private int mTitltBackgroud=0xfff9f9f9;
    private int mTitleTextColor= Color.BLACK;
    private String[] mTitleText=new String[]{"月","日","周"};
    private String[] mWeekText=new String[]{"日","一","二","三","四","五","六"};
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mTitleHeight;

    public DateTitleView(Context context) {
        this(context,null);
    }
    public DateTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
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
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTitleHeight= getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        drawBackGround(canvas,paint);
        drawTitle(canvas,paint);

    }
    private void drawBackGround(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mTitltBackgroud);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
    }
    public int getDayOfWeek(int year,int month,int day){
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,month,day);
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }
    private void drawTitle(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setColor(mTitleTextColor);
        paint.setTextSize(mTitleSize);
        int dayOfWeek=getDayOfWeek(mYear, mMonth,mDay);
        String title= (mMonth+1) +mTitleText[0]+mDay+mTitleText[1]+" "+mTitleText[2]+mWeekText[dayOfWeek];
        int fontWidth = (int) paint.measureText(title);
        int startX = (getWidth() - fontWidth)/2;
        int startY = (int) (mTitleHeight/2 - (paint.ascent() + paint.descent())/2);
        canvas.drawText(title, startX, startY, paint);
    }



    public void setTitleDate(int year,int month,int day){
        mYear=year;
        mMonth=month;
        mDay=day;
        invalidate();
    }
}
