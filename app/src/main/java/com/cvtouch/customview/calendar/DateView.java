package com.cvtouch.customview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 一
 * @date 2016/9/10
 */
public class DateView extends View {

    //默认的分割线宽度
    protected final float DEFAULT_LINE_WIDTH =0.5f;
    //在wrap_content时的高度dp
    protected final int DEFAULT_HEIGHT=300;
    //在wrap_content时的宽度dp
    protected final int DEFAULT_WIDTH=300;
    //列的数量，一周7天，共7列
    protected final int COLUMN_NUM=7;
    //用户做滑动时，取消选中事件的，最小滑动距离
    protected final int CANCEL_SELECT_TRIGGER =10;
    //默认的选中圆的大小
    protected final float DEFAULT_CIRCLE_RADIUS=20;
    //默认的日期的字体大小
    protected final float DEFAULT_WEEKDAY_TEXT_SIZE=15;
    protected DisplayMetrics mDisplayMetrics;
    //一行的高度
    protected float mWeekDayRowHeight;
    //一列的宽度
    protected float mColumnSize;
    //被选择的列和行号
    protected int mSelectColumn,mSelectRow;
    //一个月有多少天,不包括闰年
    protected int[] mDayNumsAMonth;
    //当前年份
    protected int mNowYear;
    //当前月份
    protected int mNowMon;
    //选择的年
    protected int mSelectYear;
    //选择的月
    protected int mSelectMon;
    //选择的天
    protected int mSelectDay;
    //提供日期
    protected Calendar mCalendar;
    //一个月第一天星期几
    protected int mFirstDayWeekNumber;
    //一个月最后一天星期几
    protected int mLastDayWeekNumber;
    //行的数量，一般是5行或者6行
    protected int mRowNum;
    //圆的半径
    protected float mCircleRadius;
    //日期的字体大小
    protected float mWeekDayTextSize;
    //上个月的日子的字体
    protected int mLastMonthTextColor;
    //选中圆的颜色
    protected int mCircleColor;
    //日期被选中后的颜色
    protected int mWeekdaySelectColor;
    //本月日期的颜色
    protected int mWeekdayTextColor;
    //日期背景
    protected int mWeekDayBackground;
    //分割线的颜色
    protected int mDivideLineColor;
    //分割线的宽度
    protected float mDivideLineStroke;
    //日期选中的监听
    protected OnDateSelectListener mListener;
    //控制是否有竖直的分割线
    protected boolean hasVerticalDivideLine=true;
    //控制是否有水平的分割线
    protected boolean hasHorizontalDivideLine=true;
    //点击x，y
    protected float mDownx,mDowny;
    //日期的字符，避免重复创建
    private String[] mDayText;

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
        initParams();
    }

    protected void initParams() {
        mNowYear =mCalendar.get(Calendar.YEAR);
        mNowMon =mCalendar.get(Calendar.MONTH);
        mDayNumsAMonth=new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
        mCircleRadius= (int) (mDisplayMetrics.density*DEFAULT_CIRCLE_RADIUS);
        mWeekDayTextSize=getTextSizeSp(DEFAULT_WEEKDAY_TEXT_SIZE);
        mDivideLineStroke=(int) (mDisplayMetrics.density*DEFAULT_LINE_WIDTH);
        hasVerticalDivideLine=true;
        hasHorizontalDivideLine=true;
        mLastMonthTextColor=Color.GRAY;
        mCircleColor=Color.BLACK;
        mWeekdaySelectColor =Color.WHITE;
        mWeekdayTextColor=Color.BLACK;
        mWeekDayBackground= Color.WHITE;
        mDivideLineColor=Color.GRAY;
        getRowNum();
        mDayText=new String[31];
        for(int i=0;i<mDayText.length;i++){
            mDayText[i]=(i+1)+"";
        }
    }

    public void setHorizontalDivideLine(boolean hasHorizontalDivideLine) {
        this.hasHorizontalDivideLine = hasHorizontalDivideLine;
    }

    public void setVerticalDivideLine(boolean hasVerticalDivideLine) {
        this.hasVerticalDivideLine = hasVerticalDivideLine;
    }
    public void setDivideLineStroke(float mDivideLineStroke) {
        this.mDivideLineStroke = mDivideLineStroke;
    }

    public void setDivideLineColor(int mDivideLineColor) {
        this.mDivideLineColor = mDivideLineColor;
    }

    public void setWeekdayBackground(int mWeekDayBackground) {
        this.mWeekDayBackground = mWeekDayBackground;
    }

    public void setWeekdayTextColor(int mWeekDayColor) {
        this.mWeekdayTextColor = mWeekDayColor;
    }

    public void setWeekdaySelectColor(int mWeekDaySelectColor) {
        this.mWeekdaySelectColor = mWeekDaySelectColor;
    }

    public void setCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    public void setLastMonthTextColor(int mLastMonthTextColor) {
        this.mLastMonthTextColor = mLastMonthTextColor;
        invalidate();
    }

    public void setWeekdayTextSize(float mWeekDayTextSize) {
        this.mWeekDayTextSize = mWeekDayTextSize;
    }

    public void setCircleRadius(float mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置日期
     * @param year 年
     * @param month 月 从0开始
     */
    public void setDate(int year,int month){
        mNowYear=year;
        mNowMon=month;
        getRowNum();
        invalidate();
    }

    /**
     * 设置选中的日期
     * @param year
     * @param month 从0开始
     * @param day
     */
    public void setSelectDate(int year,int month,int day){
        mSelectYear=year;
        mSelectMon=month;
        mSelectDay=day;
        invalidate();
    }


    /**
     * 求出显示整个月要都多少行
     */
    protected void getRowNum() {
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

    protected void drawDivideLine(Canvas canvas,Paint paint) {
        paint.reset();
        paint.setColor(mDivideLineColor);
        paint.setStrokeWidth(mDivideLineStroke);
        if(hasHorizontalDivideLine){
            //画横线
            for(int i=0;i<=mRowNum;i++){
                canvas.drawLine(0,i*mWeekDayRowHeight,getWidth(),i*mWeekDayRowHeight,paint);
            }
            canvas.drawLine(0,getHeight()-mDivideLineStroke,getWidth(),getHeight()-mDivideLineStroke,paint);
        }
        if(hasVerticalDivideLine){
            //画竖线
            for(int i=0;i<=COLUMN_NUM;i++){
                canvas.drawLine(i*mColumnSize,0,i*mColumnSize,getHeight(),paint);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mColumnSize=(float) getWidth()/COLUMN_NUM;
        mWeekDayRowHeight =(float) getHeight()/mRowNum;
    }

    protected void drawBackGround(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mWeekDayBackground);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
    }

    /**
     * 获取一个月有多少天
     * @param year
     * @param month
     * @return 一个月的天数
     */
    public int getMonthDays(int year,int month){
        if((month==1)&&((year%4)==0)&&(year%100)!=0||(year%400==0)){
           return mDayNumsAMonth[month]+1;
        }else{
            return mDayNumsAMonth[month];
        }
    }

    /**
     * 获取某年某月某日是星期几
     * 星期日是0，星期一是1以此类推
     * @param year
     * @param month
     * @param day
     * @return
     */
    public int getDayOfWeek(int year,int month,int day){
        mCalendar.set(year,month,day);
        return mCalendar.get(Calendar.DAY_OF_WEEK)-1;
    }

    /**
     * 画号数
     * @param canvas
     * @param paint
     */
    protected void drawWeekDay(Canvas canvas, Paint paint) {
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
            String dayString = mDayText[day];
            int column = (day + mFirstDayWeekNumber) % COLUMN_NUM;
            int row = (day + mFirstDayWeekNumber) / COLUMN_NUM;
            //画选中的日期的背景圆
            if((mSelectDay==day+1)&&(mSelectMon==mNowMon)&&(mSelectYear==mNowYear)){
                float cx=mColumnSize*column+mColumnSize/2;
                float cy=mWeekDayRowHeight*row+mWeekDayRowHeight/2;
                paint.setColor(mCircleColor);
                canvas.drawCircle(cx,cy,mCircleRadius,paint);
                paint.setColor(mWeekdaySelectColor);
            }else {
                paint.setColor(mWeekdayTextColor);
            }
            //居中显示的关键代码
            int startX = (int) (mColumnSize * column + (mColumnSize - paint.measureText(dayString)) / 2);
            int startY = (int) (mWeekDayRowHeight * row + mWeekDayRowHeight / 2 - (paint.ascent() + paint.descent()) / 2);
            canvas.drawText(dayString, startX, startY, paint);

        }
    }




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
                        //计算几号，计算相对于mFirstDayWeekNumber 之后过了几个格子
                        mSelectDay=(mSelectRow*COLUMN_NUM+mSelectColumn)-mFirstDayWeekNumber+1;
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
    protected float getTextSizeSp(float size){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }
}
