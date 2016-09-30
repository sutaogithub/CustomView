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
 * @brief 会议一页日历
 * @date 2016/9/19
 */
public class MeetingDateView extends DateView{
    //日期相对于Y方向的偏移，在现实会议数据的时候，等于向上偏移一点
    private float mWeekDayTextYOffset;
    //"场"字相对于中心的y方向偏移距离
    private float mStringMeetingYOffset;
    private int[] mMeetingData;
    private String mMeetingString;
    private int mNotThisMonthColor;
    private int mSelectRectColor;
    private float mSelectRectHeight;
    private int mMeetingTextColor;
    private float mMeetingTextSize;
    private String[] mDayText;
    public MeetingDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }


    public MeetingDateView(Context context) {
        this(context,null);

    }

    /**
     * 设置会议部分文字大小
     * @param mMeetingTextSize
     */
    public void setMeetingTextSize(float mMeetingTextSize) {
        this.mMeetingTextSize = mMeetingTextSize;

    }

    /**
     * 设置日期相对于Y轴的偏移
     * @param mWeekDayTextYOffset
     */
    public void setWeekdayTextYOffset(float mWeekDayTextYOffset) {
        this.mWeekDayTextYOffset = mWeekDayTextYOffset;

    }

    /**
     * 设置“场”字相对于中心的偏移
     * @param mStringMeetingYOffset
     */
    public void setMeetingStringYOffset(float mStringMeetingYOffset) {
        this.mStringMeetingYOffset = mStringMeetingYOffset;

    }

    /**
     * 设置会议单位的文字，默认“场”字
     * @param mStringMeeting
     */
    public void setMeetingString(String mStringMeeting) {
        this.mMeetingString = mStringMeeting;

    }

    /**
     * 设置备选中日期上方的矩形的颜色
     * @param mSelectRectColor
     */
    public void setSelectRectColor(int mSelectRectColor) {
        this.mSelectRectColor = mSelectRectColor;

    }

    /**
     * 设置被选中的日期上方黑色矩形的高度
     * @param mSelectRectHeight
     */
    public void setSelectRectHeight(float mSelectRectHeight) {
        this.mSelectRectHeight = mSelectRectHeight;
    }

    /**
     * 设置会议部分文字的颜色
     * @param mMeetingTextColor
     */
    public void setMeetingTextColor(int mMeetingTextColor) {
        this.mMeetingTextColor = mMeetingTextColor;

    }

    @Override
    protected void initParams() {
        super.initParams();
        mMeetingString ="场";
        mMeetingTextSize=getTextSizeSp(15);
        mWeekDayTextSize=getTextSizeSp(10);
        mMeetingTextColor=Color.RED;
        mWeekdayTextColor=Color.GRAY;
        mSelectRectColor=Color.BLACK;
        mNotThisMonthColor= Color.GRAY;
        mDayText=new String[31];
        for(int i=0;i<mDayText.length;i++){
            mDayText[i]=(i+1)+"";
        }
    }

    /**
     * 设置不是本月的空格的颜色
     * @param mNotThisMonthColor
     */
    public void setNotThisMonthColor(int mNotThisMonthColor) {
        this.mNotThisMonthColor = mNotThisMonthColor;
    }

    /**
     * 设置几号有多少场会议,注意日期从0开始
     * @param map
     */
    public void setMeetingData(int[] map){
        if(map==null||map.length<31){
            throw new IllegalArgumentException("Arrays null or Array's length must longer than 31,because a month has 31 day at most");
        }
        this.mMeetingData=map;
        invalidate();
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWeekDayTextYOffset=mWeekDayRowHeight/4;
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
            String dayString = mDayText[day];
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
            canvas.drawText(dayString, startX, mWeekDayRowHeight*row+mWeekDayTextYOffset, paint);
            //会议场数
            if(mMeetingData!=null&&mMeetingData[day]!=0){
                paint.setTextSize(mMeetingTextSize);
                paint.setColor(mMeetingTextColor);
                String str=mMeetingData[day]+"";
                startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(str)) / 2);
                canvas.drawText(str, startX, startY, paint);
                startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(mMeetingString)) / 2);
                canvas.drawText(mMeetingString, startX, startY+mStringMeetingYOffset, paint);
            }
        }
    }
}
