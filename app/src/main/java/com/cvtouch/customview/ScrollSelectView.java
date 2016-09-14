package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/14
 */
public class ScrollSelectView extends View{
    private final int DEFAULT_HEIGHT=40;
    private final int DEFAULT_WIDTH=300;
    private float mColumnSize;
    private int mVisibleNum=7;
    private int mDivideLineColor= Color.GRAY;
    private float mDivideLineStroke=1;
    private String[] mText=new String[]{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    private int mRowHeight;
    private int mSelectMonth=-1;
    private int mNowYear;
    private int mSelectYear;
    private int mMaxOffset;
    private float mMinOffset;
    private OnSelectedListener mListener;
    private DisplayMetrics mDisplayMetrics;
    private int first;
    private int last;

    public void setOnSelectListener(OnSelectedListener listener){
        mListener=listener;
    }
    public interface OnSelectedListener{
        void onSelected(int position);
    }

    public ScrollSelectView(Context context) {
        this(context,null);
    }
    public ScrollSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar=Calendar.getInstance();
        mNowYear =calendar.get(Calendar.YEAR);
        mSelectYear=mNowYear;
        first=0;
        last=7;
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
        mColumnSize=(float) getWidth()/mVisibleNum;
        mMinOffset=-(mText.length-mVisibleNum)*mColumnSize-mText.length*mColumnSize;
        mMaxOffset= (int) (mText.length*mColumnSize);
        mRowHeight=getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint =new Paint();
        drawText(canvas,paint);
    }

    private void drawText(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        for(int i = first;i <=last;i++) {
            paint.setColor(Color.BLACK);
            if(i==mSelectMonth&&mSelectYear==mNowYear+1){
                canvas.drawRect(+mColumnSize * i,0,mColumnSize * i+mColumnSize,getHeight(),paint);
                paint.setColor(Color.WHITE);
            }
            //居中显示的关键代码
            float startX = mColumnSize * i + (mColumnSize - paint.measureText(mText[i])) / 2;
            float startY = mRowHeight / 2 - (paint.ascent() + paint.descent()) / 2;
            canvas.drawText(mText[i], startX, startY, paint);
        }
    }

    private float mXDown,mXChange,mXFirstDown;
    private final int SELECT_TRIGGER=8;
    private boolean isSelect=false;
//    private void scroll(float distance) {
//        if(distance>0){
//            mOffset=mOffset+mXChange<=mMaxOffset? mOffset+=mXChange:mMaxOffset;
//        }else {
//            mOffset=mOffset+mXChange>=mMinOffset? mOffset+=mXChange:mMinOffset;
//        }
//        //滑到了下一年
//        if(mOffset<=-mOneYearWidth){
//            mOffset+=mOneYearWidth;
//            mNowYear++;
//            //滑到了上一年
//        }else if(mOffset>=mOneYearWidth){
//            mOffset-=mOneYearWidth;
//            mNowYear--;
//        }
//    }
}
