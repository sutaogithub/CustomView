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
public class CalendarView extends View {

    private final int DEFAULT_HEIGHT=300;
    private final int DEFAULT_WIDTH=300;
    private  DisplayMetrics mDisplayMetrics;
    private int mTitleHeight;
    private int mWeekHeight;
    private int mWeekStart;
    private int mWeekDayRowHeight;
    private int mWeekDayStart;
    private String[] mWeekText=new String[]{"日","一","二","三","四","五","六"};
    private int mColumnSize;
    private int mSelectColumn,mSelectRow;
    private int mTitltColor=0xfff9f9f9;
    private int mTitleSize=70;
    private int[] mDayNumsAMonth=new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
    private boolean isSelectDate=false;
    private int mNowYear;
    private int mNowMon;
    private int mSelectYear;
    private int mSelectMon;
    private int mSelectDay;
    private Calendar mCalendar;
    private int mFirstDayWeekNumber;
    private int mLastDayWeekNumber;
    private float mWeekTextSize=35;
    private int mRowNum;


    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mCalendar=Calendar.getInstance();
        mNowYear =mCalendar.get(Calendar.YEAR);
        mNowMon =mCalendar.get(Calendar.MONTH)+1;
        mSelectYear=mNowYear;
        mSelectMon=mNowMon;
        mSelectDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        decideRowNum();

    }

    private void decideRowNum() {
        int weekNumber =getDayOfWeek(mNowYear, mNowMon,1);
        int dayOfMon=getMonthDays(mNowYear,mNowMon);
        if(weekNumber+dayOfMon>35){
            mRowNum=6;
        }else {
            mRowNum=5;
        }
        mWeekDayRowHeight =(getHeight()-mTitleHeight-mWeekHeight)/mRowNum;
    }


    public CalendarView(Context context) {
        super(context,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = mDisplayMetrics.densityDpi * DEFAULT_HEIGHT/160;
        }
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mDisplayMetrics.densityDpi * DEFAULT_WIDTH/160;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackGround(canvas);
        drawTitle(canvas);
        drawWeek(canvas);
        drawWeekDay(canvas);
        drawDivideLine(canvas);

    }

    private void drawDivideLine(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        for(int i=0;i<mRowNum;i++){
            canvas.drawLine(0,mWeekDayStart+i*mWeekDayRowHeight,getWidth(),mWeekDayStart+i*mWeekDayRowHeight,paint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mColumnSize=getWidth()/7;
        mTitleHeight=getHeight()/7;
        mWeekHeight=mTitleHeight/2;
        mWeekDayRowHeight =(getHeight()-mTitleHeight-mWeekHeight)/mRowNum;
        mWeekStart=mTitleHeight;
        mWeekDayStart=mTitleHeight+mWeekHeight;
    }

    private void drawBackGround(Canvas canvas) {
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mTitltColor);
        canvas.drawRect(0,0,getWidth(),mTitleHeight+mWeekHeight,paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,mTitleHeight+mWeekHeight,getWidth(),getHeight(),paint);
    }

    public int getMonthDays(int year,int month){
        if((month==2)&&((year%4)==0)&&(year%100)!=0||(year%400==0)){
           return mDayNumsAMonth[month-1]+1;
        }else{
            return mDayNumsAMonth[month-1];
        }
    }
    public int getDayOfWeek(int year,int month,int day){
        mCalendar.set(year,month-1,day);
        return mCalendar.get(Calendar.DAY_OF_WEEK)-1;
    }
    private void drawWeekDay(Canvas canvas) {
        int monthADay=getMonthDays(mNowYear, mNowMon);
        mFirstDayWeekNumber =getDayOfWeek(mNowYear, mNowMon,1);
        mLastDayWeekNumber=getDayOfWeek(mNowYear, mNowMon,monthADay);
        Paint paint =new Paint();
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //绘制上个月的剩余的部分
        paint.setColor(Color.GRAY);
        int lastMonthDays;
        if(mNowMon !=1)
             lastMonthDays=getMonthDays(mNowYear, mNowMon -1);
        else
            lastMonthDays=getMonthDays(mNowYear,12);
        for(int day = lastMonthDays, j = 0, column = mFirstDayWeekNumber -1; j< mFirstDayWeekNumber; j++,column--,day--){
            String dayString =day+"";
            int startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2);
            int startY = mWeekDayStart+(int) (mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, paint);
        }
        //绘制当月的日期
        for(int day = 0;day < monthADay;day++) {
            String dayString = (day + 1) + "";
            int column = (day + mFirstDayWeekNumber) % 7;
            int row = (day + mFirstDayWeekNumber) / 7;
            //画选中的日期的背景圆
            if((mSelectDay==day+1)&&(mSelectMon==mNowMon)&&(mSelectYear==mNowYear)){
                int cx=mColumnSize*column+mColumnSize/2;
                int cy=mWeekDayStart+mWeekDayRowHeight*row+mWeekDayRowHeight/2;
                paint.setColor(Color.BLACK);
                canvas.drawCircle(cx,cy,50,paint);
                paint.setColor(Color.WHITE);
            }else {
                paint.setColor(Color.BLACK);
            }
                //居中显示的关键代码
            int startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2);
            int startY = mWeekDayStart+(int) (mWeekDayRowHeight * row + mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, paint);

        }
    }

    private void drawTitle(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(mTitleSize);
        int dayOfWeek=getDayOfWeek(mSelectYear, mSelectMon,mSelectDay);
        String title= mSelectMon +"月"+mSelectDay+"日 "+"周"+mWeekText[dayOfWeek];
        int fontWidth = (int) paint.measureText(title);
        int startX = (getWidth() - fontWidth)/2;
        int startY = (int) (mTitleHeight/2 - (paint.ascent() + paint.descent())/2);
        canvas.drawText(title, startX, startY, paint);
    }

    private void drawWeek(Canvas canvas) {
        Paint paint=new Paint();
        paint.setTextSize(mWeekTextSize);
        int columnWidth = getWidth() / 7;
        for(int i=0;i < mWeekText.length;i++){
            String text = mWeekText[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth)/2;
            int startY = mWeekStart+(int) (mWeekHeight/2 - (paint.ascent() + paint.descent())/2);
            paint.setColor(Color.BLACK);
            canvas.drawText(text, startX, startY, paint);
        }
    }

    private float mDownx,mDowny,mXChange;
    private final int MONTH_CHANGE_TRIGGER_DISTANCE=15;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                 mDownx=event.getX();
                 mDowny=event.getY();
                 mSelectColumn= (int) (mDownx/mColumnSize);
                 mSelectRow= (int) ((mDowny-mWeekDayStart)/mWeekDayRowHeight);
            case MotionEvent.ACTION_MOVE:
                mXChange=mDownx-event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(Math.abs(mXChange)>MONTH_CHANGE_TRIGGER_DISTANCE){
                    if(mXChange<0){
                        showPreMonth();
                    }else {
                        showNextMonth();
                    }
                }else {
                    if(mSelectRow<0||(mSelectRow==0&&mSelectColumn<mFirstDayWeekNumber)||(mSelectRow==4&&mSelectColumn>mLastDayWeekNumber)){
                    }
                    else {
                        mSelectYear=mNowYear;
                        mSelectMon=mNowMon;
                        mSelectDay=(mSelectRow*7+mSelectColumn)-mFirstDayWeekNumber+1;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    private void showPreMonth() {
        if(mNowMon ==1){
            mNowMon =12;
            mNowYear--;

        }else {
            mNowMon--;
        }
        decideRowNum();
        invalidate();
    }
    private void showNextMonth() {
        if(mNowMon ==12){
            mNowMon =1;
            mNowYear++;

        }else {
            mNowMon++;
        }
        decideRowNum();
        invalidate();
    }
}
