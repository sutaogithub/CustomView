package com.cvtouch.customview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/19
 */
public class MeetingDateView extends DateView{
    //日期相对于Y方向的偏移，在现实会议数据的时候，等于向上偏移一点
    private float mWeekDayTextYOffset;
    private int[] mMeetingData;
    private String mMeetingString;
    private float mStringMeetingYOffset;
    private int mNotThisMonthColor;
    private int mSelectRectColor;
    private float mSelectRectHeight;
    private int mMeetingTextColor;
    private float mMeetingTextSize;
    public MeetingDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }


    public MeetingDateView(Context context) {
        this(context,null);

    }

    public void setMeetingTextSize(float mMeetingTextSize) {
        this.mMeetingTextSize = mMeetingTextSize;

    }

    public void setWeekDayTextYOffset(float mWeekDayTextYOffset) {
        this.mWeekDayTextYOffset = mWeekDayTextYOffset;

    }

    public void setStringMeetingYOffset(float mStringMeetingYOffset) {
        this.mStringMeetingYOffset = mStringMeetingYOffset;

    }

    public void setMeetingString(String mStringMeeting) {
        this.mMeetingString = mStringMeeting;

    }

    public void setSelectRectColor(int mSelectRectColor) {
        this.mSelectRectColor = mSelectRectColor;

    }

    public void setSelectRectHeight(float mSelectRectHeight) {
        this.mSelectRectHeight = mSelectRectHeight;
    }

    public void setMeetingTextColor(int mMeetingTextColor) {
        this.mMeetingTextColor = mMeetingTextColor;

    }

    @Override
    protected void initParams() {
        super.initParams();
        mMeetingData=new int[32];
        mMeetingData[1]=2;
        mMeetingString ="场";
        mMeetingTextSize=getTextSizeSp(15);
        mWeekDayTextSize=getTextSizeSp(10);
        mMeetingTextColor=Color.RED;
        mWeekdayTextColor=Color.GRAY;
        mSelectRectColor=Color.BLACK;
        mNotThisMonthColor= Color.GRAY;
    }


    public void setNotThisMonthColor(int mNotThisMonthColor) {
        this.mNotThisMonthColor = mNotThisMonthColor;

    }

    /**
     * 设置几号有多少场会议,注意日期从0开始
     * @param map
     */
    public void setMeetingData(int[] map){
        if(map==null||map.length<31){
            throw new IllegalArgumentException("Array's length must longer than 31,because a month has 31 day at most");
        }
        this.mMeetingData=map;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWeekDayTextYOffset=mWeekDayRowHeight/3;
        mStringMeetingYOffset=mWeekDayRowHeight/4;
        mSelectRectHeight=mWeekDayRowHeight/13;
    }


    @Override
    protected void drawWeekDay(Canvas canvas, Paint paint) {
        int monthADay=getMonthDays(mNowYear, mNowMon);
        mFirstDayWeekNumber =getDayOfWeek(mNowYear, mNowMon,1);
        mLastDayWeekNumber=getDayOfWeek(mNowYear, mNowMon,monthADay);
        paint.reset();
        paint.setTextSize(mWeekDayTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //绘制上个月的剩余的部分
        paint.setColor(mNotThisMonthColor);
        for(int column = mFirstDayWeekNumber -1; column>=0;column--){
            canvas.drawRect(column*mColumnSize,0,column*mColumnSize+mColumnSize,mWeekDayRowHeight,paint);
        }
        //最后一行的剩余几个空格
        for(int column=mLastDayWeekNumber+1;column<7;column++){
            canvas.drawRect(column*mColumnSize,(mRowNum-1)*mWeekDayRowHeight,column*mColumnSize+mColumnSize,(mRowNum-1)*mWeekDayRowHeight+mWeekDayRowHeight,paint);
        }
        paint.setColor(mWeekdayTextColor);
        //绘制当月的日期
        for(int day = 0;day < monthADay;day++) {
            String dayString = (day + 1) + "";
            int column = (day + mFirstDayWeekNumber) % COLUMN_NUM;
            int row = (day + mFirstDayWeekNumber) / COLUMN_NUM;
            //画选中的日期的背景圆
            if((mSelectDay==day+1)&&(mSelectMon==mNowMon)&&(mSelectYear==mNowYear)){
                paint.setColor(mSelectRectColor);
                canvas.drawRect(mColumnSize*column,mWeekDayRowHeight*row,mColumnSize*column+mColumnSize,mWeekDayRowHeight*row+mSelectRectHeight,paint);
            }
            //画日期
            paint.setTextSize(mWeekDayTextSize);
            paint.setColor(Color.GRAY);
            int startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2);
            int startY = (int) (mWeekDayRowHeight * row + mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, startX, startY-mWeekDayTextYOffset, paint);
            //会议场数
            if(mMeetingData!=null&&mMeetingData[day]!=0){
                paint.setTextSize(mMeetingTextSize);
                paint.setColor(mMeetingTextColor);
                String str=mMeetingData[day]+"";
                startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(str)) / 2);
                canvas.drawText(mMeetingData[day]+"", startX, startY, paint);
                startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(mMeetingString)) / 2);
                canvas.drawText(mMeetingString, startX, startY+mStringMeetingYOffset, paint);
            }
        }
    }
}
