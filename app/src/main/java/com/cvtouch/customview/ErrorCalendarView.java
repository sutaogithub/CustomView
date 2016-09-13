package com.cvtouch.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
 * @date 2016/9/12
 */
public class ErrorCalendarView extends View{
    private final int DEFAULT_HEIGHT=300;
    private final int DEFAULT_WIDTH=300;
    private final int DEFAULT_DAY_TEXT_Y_OFFSET=13;
    private final int MONTH_CHANGE_TRIGGER_DISTANCE=3;
    private final int DEFAULE_SELECT_RECT_HEIGHT=4;
    private DisplayMetrics mDisplayMetrics;
    private int mTitleHeight;
    private int mWeekHeight;
    private int mWeekStart;
    private int mWeekDayRowHeight;
    private int mWeekDayStart;
    //列宽用float，不然由于余数导致有小缝隙
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
    private float mSelectRectHeight;
    private float mDayYOffset;
    private int mCircleRadius=50;
    private int mTitleSize=70;
    private int mTitltBackgroud=0xfff9f9f9;
    private int mTitleTextColor= Color.BLACK;
    private String[] mTitleText=new String[]{"月","日","周"};
    private String[] mWeekText=new String[]{"日","一","二","三","四","五","六"};
    private float mWeekTextSize=35;
    private int mWeekTextColor=Color.BLACK;
    private float mWeekDayTextSize=30;
    private int mNotThisMonthColor =0xf0eff5;
    private int mSelectRectColor =Color.BLACK;
    private int mWeekDaySelectColor=Color.WHITE;
    private int mWeekDayColor=Color.BLACK;
    private int mWeekDayBackground= Color.WHITE;
    private int mDivideLineColor=Color.GRAY;
    private float mDivideLineStroke=1;
    private float mTitleHeightRatio=1.0f/7;
    private boolean mAllowTouchChangeMonth=true;
    private int mXOffset;
    private float mMaxOffset;
    private float mMinOffset;
    public ErrorCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mDayYOffset=mDisplayMetrics.density*DEFAULT_DAY_TEXT_Y_OFFSET;
        mSelectRectHeight=mDisplayMetrics.density*DEFAULE_SELECT_RECT_HEIGHT;
        mCalendar=Calendar.getInstance();
        mNowYear =mCalendar.get(Calendar.YEAR);
        mNowMon =mCalendar.get(Calendar.MONTH)+1;
        mSelectYear=mNowYear;
        mSelectMon=mNowMon;
        mSelectDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        updateWeekNumber();
    }

    private void updateWeekNumber() {
        int monthADay=getMonthDays(mNowYear, mNowMon);
        mFirstDayWeekNumber =getDayOfWeek(mNowYear, mNowMon,1);
        mLastDayWeekNumber=getDayOfWeek(mNowYear, mNowMon,monthADay);
    }


    private void getRowNum(int year,int month) {
        int weekNumber =getDayOfWeek(year, month,1);
        int dayOfMon=getMonthDays(year,month);
        //超过35天就超过5行
        if(weekNumber+dayOfMon>35){
            mRowNum=6;
        }else {
            mRowNum=5;
        }
        mWeekDayRowHeight = (getHeight()-mTitleHeight-mWeekHeight)/mRowNum;
    }


    public ErrorCalendarView(Context context) {
        super(context,null);
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
        drawTitle(canvas,paint);
        drawWeek(canvas,paint);
        //画当月
        getRowNum(mNowYear,mNowMon);
        drawWeekDay(canvas,paint, mXOffset,mNowYear,mNowMon);
        drawDivideLine(canvas,paint, mXOffset);
        drawDivideLine(canvas,paint, mXOffset);
        //画上个月
        int preMonthOffset= mXOffset -getWidth();
        if(mNowMon==1){
            getRowNum(mNowYear-1,12);
            drawWeekDay(canvas,paint, preMonthOffset,mNowYear-1,12);
        } else{
            getRowNum(mNowYear,mNowMon-1);
            drawWeekDay(canvas,paint, preMonthOffset,mNowYear,mNowMon-1);
        }
        drawDivideLine(canvas,paint, preMonthOffset);
        //画下个月
        int nextMonthOffset= mXOffset +getWidth();
        if(mNowMon==12){
            getRowNum(mNowYear+1,1);
            drawWeekDay(canvas,paint, nextMonthOffset,mNowYear+1,1);
        } else{
            getRowNum(mNowYear,mNowMon+1);
            drawWeekDay(canvas,paint, nextMonthOffset,mNowYear,mNowMon+1);
        }
        drawDivideLine(canvas,paint, nextMonthOffset);
    }

    private void drawDivideLine(Canvas canvas,Paint paint,int xOffset) {
        paint.reset();
        paint.setColor(mDivideLineColor);
        paint.setStrokeWidth(mDivideLineStroke);
        //画横线
        for(int i=0;i<mRowNum;i++){
            canvas.drawLine(xOffset,mWeekDayStart+i*mWeekDayRowHeight,xOffset+getWidth(),mWeekDayStart+i*mWeekDayRowHeight,paint);
        }
        //画最后一条横线,因为误差最后一条线可能没有画出来,所以再画
        canvas.drawLine(xOffset,getHeight()-1,xOffset+getWidth(),getHeight()-1,paint);
        //画竖线
        for(int i=0;i<=7;i++){
            canvas.drawLine(xOffset+i*mColumnSize,mWeekDayStart,xOffset+i*mColumnSize,getHeight(),paint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mColumnSize=(float) getWidth()/7;
        mTitleHeight= (int) (getHeight()*mTitleHeightRatio);
        mWeekHeight=mTitleHeight/2;
        mWeekStart=mTitleHeight;
        mWeekDayStart=mTitleHeight+mWeekHeight;
        mMaxOffset = getWidth();
        mMinOffset = -getWidth();
    }

    private void drawBackGround(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mTitltBackgroud);
        canvas.drawRect(0,0,getWidth(),mTitleHeight+mWeekHeight,paint);
        paint.setColor(mWeekDayBackground);
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
    private void drawWeekDay(Canvas canvas, Paint paint,int xOffset,int year,int month) {
        int monthADay=getMonthDays(year, month);
        int firstWeekNumber =getDayOfWeek(year, month,1);
        int lastDayWeekNumber=getDayOfWeek(year, month,monthADay);
        paint.reset();
        paint.setTextSize(mWeekDayTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //绘制上个月的剩余的部分
        paint.setColor(mNotThisMonthColor);
        for(int column = firstWeekNumber -1; column>=0;column--){
            canvas.drawRect(xOffset+column*mColumnSize,mWeekDayStart,xOffset+column*mColumnSize+mColumnSize,mWeekDayStart+mWeekDayRowHeight,paint);
        }
        //最后一行的剩余几个空格
        for(int column=lastDayWeekNumber+1;column<7;column++){
            canvas.drawRect(xOffset+column*mColumnSize,mWeekDayStart+(mRowNum-1)*mWeekDayRowHeight,xOffset+column*mColumnSize+mColumnSize,mWeekDayStart+(mRowNum-1)*mWeekDayRowHeight+mWeekDayRowHeight,paint);
        }
        paint.setColor(mWeekDayColor);
        //绘制当月的日期
        for(int day = 0;day < monthADay;day++) {
            String dayString = (day + 1) + "";
            int column = (day + firstWeekNumber) % 7;
            int row = (day + firstWeekNumber) / 7;
            //画选中的日期的背景圆
            if((mSelectDay==day+1)&&(mSelectMon==month)&&(mSelectYear==year)){
                paint.setColor(mSelectRectColor);
                canvas.drawRect(xOffset+mColumnSize*column,mWeekDayStart+mWeekDayRowHeight*row,xOffset+mColumnSize*column+mColumnSize,mWeekDayStart+mWeekDayRowHeight*row+mSelectRectHeight,paint);
                paint.setColor(mWeekDayColor);
            }
            //居中显示的关键代码
            float startX = mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2;
            float startY = mWeekDayStart+ (mWeekDayRowHeight * row + mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, xOffset+startX, startY-mDayYOffset, paint);
        }
    }

    private void drawTitle(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setColor(mTitleTextColor);
        paint.setTextSize(mTitleSize);
        int dayOfWeek=getDayOfWeek(mSelectYear, mSelectMon,mSelectDay);
        String title= mSelectMon +mTitleText[0]+mSelectDay+mTitleText[1]+" "+mTitleText[2]+mWeekText[dayOfWeek];
        int fontWidth = (int) paint.measureText(title);
        int startX = (getWidth() - fontWidth)/2;
        int startY = (int) (mTitleHeight/2 - (paint.ascent() + paint.descent())/2);
        canvas.drawText(title, startX, startY, paint);
    }

    private void drawWeek(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(mWeekTextSize);
        paint.setColor(mWeekTextColor);
        int columnWidth = getWidth() / 7;
        for(int i=0;i < mWeekText.length;i++){
            String text = mWeekText[i];
            int fontWidth = (int) paint.measureText(text);
            float startX = columnWidth * i + (columnWidth - fontWidth)/2;
            float startY = mWeekStart+ (mWeekHeight/2 - (paint.ascent() + paint.descent())/2);
            canvas.drawText(text, startX, startY, paint);
        }
    }

    private float mXDown, mYDown,mXChange,mXFirstDown;
    private final int SELECT_TRIGGER=8;
    private boolean isSelect;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXDown =event.getX();
                mXFirstDown=event.getX();
                mYDown =event.getY();
                mSelectColumn= (int) (mXDown /mColumnSize);
                mSelectRow= (int) ((mYDown -mWeekDayStart)/mWeekDayRowHeight);
                isSelect=true;
            case MotionEvent.ACTION_MOVE:
                mXChange=event.getX()-mXDown;
                if(Math.abs(event.getX()-mXFirstDown)>SELECT_TRIGGER){
                    isSelect=false;
                }
                if(mXChange>0){
                    mXOffset = mXOffset +mXChange<=mMaxOffset? mXOffset +=mXChange: mXOffset;
                }else {
                    mXOffset = mXOffset +mXChange>=mMinOffset? mXOffset +=mXChange: mXOffset;
                }
                mXDown=event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(isSelect){
                    if(!(mSelectRow<0||(mSelectRow==0&&mSelectColumn<mFirstDayWeekNumber)||(mSelectRow==mRowNum&&mSelectColumn>mLastDayWeekNumber))){
                        mSelectYear=mNowYear;
                        mSelectMon=mNowMon;
                        mSelectDay=(mSelectRow*7+mSelectColumn)-mFirstDayWeekNumber+1;
                    }
                }else {
                    int dx= (int) (event.getX()-mXFirstDown);
                    if(Math.abs(dx)>getWidth()/MONTH_CHANGE_TRIGGER_DISTANCE&&mAllowTouchChangeMonth){
                        if(dx>0){
                            showPreMonth();
                        }else {
                            showNextMonth();
                        }
                    }else{
                        backToNowMonth();
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    private void backToNowMonth() {
        ValueAnimator animator = ValueAnimator.ofInt(mXOffset,0);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mXOffset = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


    private void showPreMonth() {
        ValueAnimator animator = ValueAnimator.ofInt(mXOffset,getWidth());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mXOffset = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(mNowMon ==1){
                    mNowMon =12;
                    mNowYear--;
                }else {
                    mNowMon--;
                }
                updateWeekNumber();
                mXOffset=0;
            }
        });
        animator.start();

    }
    private void showNextMonth() {
        ValueAnimator animator = ValueAnimator.ofInt(mXOffset,-getWidth());
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mXOffset = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(mNowMon ==12){
                    mNowMon =1;
                    mNowYear++;
                }else {
                    mNowMon++;
                }
                updateWeekNumber();
                mXOffset=0;
            }
        });

        animator.start();
    }
}
