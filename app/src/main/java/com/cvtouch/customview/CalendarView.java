package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

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
    private int mColumnSize,mRowSize;
    private int mSelectColumn,mSelectRow;
    private int mTitltColor=0xfff9f9f9;


    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
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
        for(int i=0;i<5;i++){
            canvas.drawLine(0,mWeekDayStart+i*mWeekDayRowHeight,getWidth(),mWeekDayStart+i*mWeekDayRowHeight,paint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mColumnSize=getWidth()/7;
        mTitleHeight=getHeight()/7;
        mWeekHeight=mTitleHeight/2;
        mWeekDayRowHeight =(getHeight()-mTitleHeight-mWeekHeight)/5;
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

    private void drawWeekDay(Canvas canvas) {
        int weekNumber=5;
        Paint paint =new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        for(int day = 0;day < 28;day++) {
            String dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            //画选中的日期的背景圆
            if(column==mSelectColumn&&row==mSelectRow){
                int cx=mColumnSize*column+mColumnSize/2;
                int cy=mWeekDayStart+mWeekDayRowHeight*row+mWeekDayRowHeight/2;
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
        paint.setTextSize(40);
        int fontWidth = (int) paint.measureText("6月1日 周三");
        int startX = (getWidth() - fontWidth)/2;
        int startY = (int) (mTitleHeight/2 - (paint.ascent() + paint.descent())/2);
        canvas.drawText("6月1日 周三", startX, startY, paint);
    }

    private void drawWeek(Canvas canvas) {
        Paint paint=new Paint();
        paint.setTextSize(35);
        int columnWidth = getWidth() / 7;
        for(int i=0;i < mWeekText.length;i++){
            String text = mWeekText[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth)/2;
            int startY = mWeekStart+(int) (mWeekHeight/2 - (paint.ascent() + paint.descent())/2);
            if(text.indexOf("日") > -1|| text.indexOf("六") > -1){
                paint.setColor(Color.BLACK);
            }else{
                paint.setColor(Color.BLACK);
            }
            canvas.drawText(text, startX, startY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x=event.getX();
                float y=event.getY();
                mSelectColumn= (int) (x/mColumnSize);
                mSelectRow= (int) ((y-mWeekDayStart)/mWeekDayRowHeight);
                invalidate();
                break;
        }

        return true;
    }
}
