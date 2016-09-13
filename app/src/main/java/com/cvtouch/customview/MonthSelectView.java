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
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/12
 */
public class MonthSelectView extends View{
    private final int DEFAULT_HEIGHT=40;
    private final int DEFAULT_WIDTH=300;
    private float mColumnSize;
    private int mVisibleNum=7;
    private int mDivideLineColor= Color.GRAY;
    private float mDivideLineStroke=1;
    private String[] mText=new String[]{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    private float mOffset=0;
    private int mRowHeight;
    private int mSelectMonth=-1;
    private int mMaxOffset;
    private float mMinOffset;
    private OnSelectedListener mListener;
    private DisplayMetrics mDisplayMetrics;
    public void setOnSelectListener(OnSelectedListener listener){
        mListener=listener;
    }
    public interface OnSelectedListener{
        void onSelected(int position);
    }

    public MonthSelectView(Context context) {
        this(context,null);
    }

    public MonthSelectView(Context context, AttributeSet attrs) {
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
        mColumnSize=(float) getWidth()/mVisibleNum;
        mMinOffset=-(mText.length-mVisibleNum)*mColumnSize;
        mMaxOffset=0;
        mRowHeight=getHeight();
        getOffset();
    }

    private void getOffset() {
        if(mSelectMonth-mVisibleNum/2>=0&&mSelectMonth+mVisibleNum/2<=11) {
            mOffset=-(mSelectMonth-mVisibleNum/2)*mColumnSize;
        }else {
            if(mSelectMonth<mVisibleNum){
                mOffset=mMaxOffset;
            }else {
                mOffset=mMinOffset;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint =new Paint();
        canvas.drawColor(Color.WHITE);
        drawText(canvas,paint);

    }
    public void setSelectIndex(int month){
        mSelectMonth=month;
        startAnimation(month);
        invalidate();
    }
    private void drawText(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        for(int i = 0;i < mText.length;i++) {
            paint.setColor(Color.BLACK);
            if(i==mSelectMonth){
                canvas.drawRect(mOffset+mColumnSize * i,0,mOffset+mColumnSize * i+mColumnSize,getHeight(),paint);
                paint.setColor(Color.WHITE);
            }
            //居中显示的关键代码
            float startX = mOffset+mColumnSize * i + (mColumnSize - paint.measureText(mText[i])) / 2;
            float startY = mRowHeight / 2 - (paint.ascent() + paint.descent()) / 2;
            canvas.drawText(mText[i], startX, startY, paint);
        }
    }

    private void drawDivideLine(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setColor(mDivideLineColor);
        paint.setStrokeWidth(mDivideLineStroke);
        int divideNum=mText.length+1;
        for(int i=0;i<divideNum;i++){
            canvas.drawLine(mOffset+mColumnSize*i,0,mOffset+mColumnSize*i,getHeight(),paint);
        }
    }

    private float mXDown,mXChange,mXFirstDown;
    private final int SELECT_TRIGGER=8;
    private boolean isSelect=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXDown=event.getX();
                mXFirstDown=event.getX();
                isSelect=true;
                break;
            case MotionEvent.ACTION_MOVE:
                mXChange=event.getX()-mXDown;
                if(Math.abs(event.getX()-mXFirstDown)>SELECT_TRIGGER){
                    isSelect=false;
                }
                if(mXChange>0){
                    mOffset=mOffset+mXChange<=mMaxOffset? mOffset+=mXChange:mMaxOffset;
                }else {
                    mOffset=mOffset+mXChange>=mMinOffset? mOffset+=mXChange:mMinOffset;
                }
                mXDown=event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(isSelect){
                    mSelectMonth= (int) ((-mOffset+mXFirstDown)/mColumnSize);
                    startAnimation(mSelectMonth);
                    if(mListener!=null){
                        mListener.onSelected(mSelectMonth);
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    private void startAnimation(int index) {
        if(mColumnSize!=0&&(index-mVisibleNum/2>=0&&index+mVisibleNum/2<=11)){
            ValueAnimator animator = ValueAnimator.ofFloat(mOffset,-(index-mVisibleNum/2)*mColumnSize);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mOffset= (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
        }else {
            getOffset();
        }
    }

}
