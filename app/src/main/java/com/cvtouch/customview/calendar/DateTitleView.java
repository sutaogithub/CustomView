package com.cvtouch.customview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
    private float mTitleSize;
    private int mTitltBackgroud;
    private int mTitleTextColor;
    private String[] mTitleText;
    private String[] mWeekText;
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
        initParams();
    }



    private void initParams() {

        mTitleText=new String[]{"月","日","周"};
        mWeekText=new String[]{"日","一","二","三","四","五","六"};
        mTitltBackgroud=0xfff9f9f9;
        mTitleSize=getTextSizeSp(20);
        mTitleTextColor= Color.BLACK;
    }

    /**
     * 设置日期文字
     * @param mTitleText 月，日，周 的文字，要长度为3，并按月，日，周的顺序
     */
    public void setTitleText(String[] mTitleText) {
        if(mTitleText==null||mTitleText.length!=3){
            return;
        }
        this.mTitleText = mTitleText;
    }

    /**
     * 设置周几的文字
     * @param mWeekText 长度必须为7，从周日开始,周六最后
     */
    public void setWeekText(String[] mWeekText) {
        if(mWeekText==null||mWeekText.length!=7){
            return;
        }
        this.mWeekText = mWeekText;
    }

    public void setTitltBackgroud(int mTitltBackgroud) {
        this.mTitltBackgroud = mTitltBackgroud;
    }

    public void setTitleSize(float mTitleSize) {
        this.mTitleSize = mTitleSize;
    }

    public void setTitleTextColor(int mTitleTextColor) {
        this.mTitleTextColor = mTitleTextColor;
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
        StringBuilder builder=new StringBuilder();
        builder.append(mMonth+1);
        builder.append(mTitleText[0]);
        builder.append(mDay);
        builder.append(mTitleText[1]);
        builder.append(" ");
        builder.append(mTitleText[2]);
        builder.append(mWeekText[dayOfWeek]);
        int fontWidth = (int) paint.measureText(builder.toString());
        int startX = (getWidth() - fontWidth)/2;
        int startY = (int) (mTitleHeight/2 - (paint.ascent() + paint.descent())/2);
        canvas.drawText(builder.toString(), startX, startY, paint);
    }



    public void setTitleDate(int year,int month,int day){
        mYear=year;
        mMonth=month;
        mDay=day;
        invalidate();
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }

}
