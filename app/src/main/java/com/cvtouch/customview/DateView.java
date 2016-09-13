package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/10
 */
public class DateView extends View {

    private static final float DEFAULT_LINE_WIDTH =0.5f;
    private final int DEFAULT_HEIGHT=300;
    private final int DEFAULT_WIDTH=300;
    private final float DEFAULT_CIRCLE_RADIUS=20;
    private final float DEFAULT_WEEKDAY_TEXT_SIZE=15;
    private  DisplayMetrics mDisplayMetrics;
    private float mWeekDayRowHeight;
    private float mColumnSize;
    private int mSelectColumn,mSelectRow;
    private int[] mDayNumsAMonth=new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
    private int mNowYear;
    private int mNowMon;
    private int mSelectYear;
    private int mSelectMon;
    private int mSelectDay;
    private Calendar mCalendar;
    private int mFirstDayWeekNumber;
    private int mLastDayWeekNumber;
    private int mRowNum;
    private int mCircleRadius;
    private float mWeekDayTextSize;
    private int mLastMonthTextColor=Color.GRAY;
    private int mCircleColor=Color.BLACK;
    private int mWeekDaySelectColor=Color.WHITE;
    private int mWeekDayColor=Color.BLACK;
    private int mWeekDayBackground= Color.WHITE;
    private int mDivideLineColor=Color.GRAY;
    private float mDivideLineStroke;
    private int mWeekDayTextYOffset=-30;
    private OnDateSelectListener mListener;
    private boolean hasVerticalDivideLine=true;
    private boolean hasHorizontalDivideLine=true;
    public interface OnDateSelectListener{
        void onSelected(int year,int month,int day);
    }
    public void setDateSelectListener(OnDateSelectListener listener){
        mListener=listener;
    }
    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mCalendar=Calendar.getInstance();
        mNowYear =mCalendar.get(Calendar.YEAR);
        mNowMon =mCalendar.get(Calendar.MONTH);
        mCircleRadius= (int) (mDisplayMetrics.density*DEFAULT_CIRCLE_RADIUS);
        mWeekDayTextSize=(int) (mDisplayMetrics.density*DEFAULT_WEEKDAY_TEXT_SIZE);
        mDivideLineStroke=(int) (mDisplayMetrics.density*DEFAULT_LINE_WIDTH);
        getRowNum();
    }

    public void setDate(int year,int month){
        mNowYear=year;
        mNowMon=month;
        getRowNum();
        invalidate();
    }
    public void setSelectDate(int year,int month,int day){
        mSelectYear=year;
        mSelectMon=month;
        mSelectDay=day;
        invalidate();
    }


    private void getRowNum() {
        int weekNumber =getDayOfWeek(mNowYear, mNowMon,1);
        int dayOfMon=getMonthDays(mNowYear,mNowMon);
        //超过35天就超过5行
        if(weekNumber+dayOfMon>35){
            mRowNum=6;
        }else {
            mRowNum=5;
        }
        mWeekDayRowHeight =((float) getHeight())/mRowNum;
    }


    public DateView(Context context) {
        this(context,null);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        drawBackGround(canvas,paint);
        drawWeekDay(canvas,paint);
        drawDivideLine(canvas,paint);

    }

    private void drawDivideLine(Canvas canvas,Paint paint) {
        paint.reset();
        paint.setColor(mDivideLineColor);
        paint.setStrokeWidth(mDivideLineStroke);
        if(hasHorizontalDivideLine){
            //画横线
            for(int i=0;i<=mRowNum;i++){
                canvas.drawLine(0,i*mWeekDayRowHeight,getWidth(),i*mWeekDayRowHeight,paint);
            }
            canvas.drawLine(0,getHeight()-mDisplayMetrics.density*1,getWidth(),getHeight()-mDisplayMetrics.density*1,paint);
        }
        if(hasVerticalDivideLine){
            //画竖线
            for(int i=0;i<=7;i++){
                canvas.drawLine(i*mColumnSize,0,i*mColumnSize,getHeight(),paint);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mColumnSize=(float) getWidth()/7;
        mWeekDayRowHeight =(float) getHeight()/mRowNum;
    }

    private void drawBackGround(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mWeekDayBackground);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
    }

    public int getMonthDays(int year,int month){
        if((month==2)&&((year%4)==0)&&(year%100)!=0||(year%400==0)){
           return mDayNumsAMonth[month]+1;
        }else{
            return mDayNumsAMonth[month];
        }
    }
    public int getDayOfWeek(int year,int month,int day){
        mCalendar.set(year,month,day);
        return mCalendar.get(Calendar.DAY_OF_WEEK)-1;
    }
    private void drawWeekDay(Canvas canvas, Paint paint) {
        int monthADay=getMonthDays(mNowYear, mNowMon);
        mFirstDayWeekNumber =getDayOfWeek(mNowYear, mNowMon,1);
        mLastDayWeekNumber=getDayOfWeek(mNowYear, mNowMon,monthADay);
        paint.reset();
        paint.setTextSize(mWeekDayTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //绘制上个月的剩余的部分
        paint.setColor(mLastMonthTextColor);
        int lastMonthDays;
        if(mNowMon !=0)
             lastMonthDays=getMonthDays(mNowYear, mNowMon -1);
        else
            lastMonthDays=getMonthDays(mNowYear,11);
        for(int day = lastMonthDays,column = mFirstDayWeekNumber -1; column>=0;column--,day--){
            String dayString =day+"";
            int startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2);
            int startY = (int) (mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, paint);
        }
        //绘制当月的日期
        for(int day = 0;day < monthADay;day++) {
            String dayString = (day + 1) + "";
            int column = (day + mFirstDayWeekNumber) % 7;
            int row = (day + mFirstDayWeekNumber) / 7;
            //画选中的日期的背景圆
            if((mSelectDay==day+1)&&(mSelectMon==mNowMon)&&(mSelectYear==mNowYear)){
                float cx=mColumnSize*column+mColumnSize/2;
                float cy=mWeekDayRowHeight*row+mWeekDayRowHeight/2;
                paint.setColor(mCircleColor);
                canvas.drawCircle(cx,cy+mWeekDayTextYOffset,mCircleRadius,paint);
                paint.setColor(mWeekDaySelectColor);
            }else {
                paint.setColor(mWeekDayColor);
            }
                //居中显示的关键代码
            int startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2);
            int startY = mWeekDayTextYOffset+(int) (mWeekDayRowHeight * row + mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, paint);

        }
    }



    private float mDownx,mDowny;
    private int CANCEL_SELECT_TRIGGER =10;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                 mDownx=event.getX();
                 mDowny=event.getY();
                 mSelectColumn= (int) (mDownx/mColumnSize);
                 mSelectRow= (int) ((mDowny)/mWeekDayRowHeight);
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(event.getX()-mDownx)> CANCEL_SELECT_TRIGGER||Math.abs(event.getY()-mDowny)> CANCEL_SELECT_TRIGGER){
                    break;
                }else {
                    if(!(mSelectRow<0||(mSelectRow==0&&mSelectColumn<mFirstDayWeekNumber)||(mSelectRow==mRowNum&&mSelectColumn>mLastDayWeekNumber))){
                        mSelectYear=mNowYear;
                        mSelectMon=mNowMon;
                        mSelectDay=(mSelectRow*7+mSelectColumn)-mFirstDayWeekNumber+1;
                        if(mListener!=null){
                            mListener.onSelected(mSelectYear,mSelectMon,mSelectDay);
                        }
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    public int getYear(){
        return mNowYear;
    }
    public int getMonth(){
        return mNowMon;
    }
}
