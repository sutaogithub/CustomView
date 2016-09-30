package com.cvtouch.customview.calendar;

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
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.Calendar;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 底部月份选择条
 * @date 2016/9/12
 */
public class MonthSelectView extends View{
    //wrap_content时的默认高度
    private final int DEFAULT_HEIGHT=40;
    //wrap_content时的默认宽度
    private final int DEFAULT_WIDTH=300;
    //最大速度
    private final int MAX_VELOCITY;
    //每个格子的宽度
    private float mColumnSize;
    //一个屏幕上可以看见的月份的数量，默认是7
    private int mVisibleNum;
    //分割线的颜色
    private int mDivideLineColor;
    //分割线的宽度
    private float mDivideLineStroke;
    //月份的文字
    private String[] mText;
    //绘制的偏移量，用来响应左右滑动的效果
    private float mOffset;
    //行的高度
    private int mRowHeight;
    //被选择的月份
    private int mSelectMonth;
    //当前的年份
    private int mNowYear;
    //被选择的年
    private int mSelectYear;
    //最大偏移量
    private int mMaxOffset;
    //最小偏移量
    private float mMinOffset;
    private OnSelectedListener mListener;
    private DisplayMetrics mDisplayMetrics;
    //一年12个月的宽度
    private int  mOneYearWidth;
    //文字的大小
    private float mTextSize;
    //背景
    private int mBackGround;
    //控制滑动惯性的
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    //用来辅助计算Scroller的滑动增量的变量
    private int mPreScrollX;
    //控制是否有分割线，默认没有
    private boolean mHasDivideLine;
    //动画时间
    private long mAnimDuration;


    public void setDivideLine(boolean flag) {
        this.mHasDivideLine = flag;
    }

    public MonthSelectView(Context context) {
        this(context,null);
    }

    public MonthSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mScroller=new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        MAX_VELOCITY =configuration.getScaledMaximumFlingVelocity();
        initParams();
    }
    private void initParams() {
        Calendar calendar=Calendar.getInstance();
        mNowYear =calendar.get(Calendar.YEAR);
        mSelectYear=mNowYear;
        mTextSize=getTextSizeSp(15);
        //一开始不选择月
        mSelectMonth=-1;
        mOffset=0;
        mText=new String[]{"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        mDivideLineStroke=1;
        mDivideLineColor= Color.GRAY;
        //默认可见月份为7个
        mVisibleNum=7;
        mHasDivideLine=false;
        mBackGround=Color.WHITE;
        mAnimDuration =300;
    }
    public void setAnimDuration(long time){
        mAnimDuration =time;
    }

    public void setBackGround(int backGround) {
        this.mBackGround = backGround;
    }



    public void setVisibleNum(int visibleNum) {
        this.mVisibleNum = visibleNum;
    }

    public void setDivideLineColor(int color) {
        this.mDivideLineColor = color;
    }

    public void setDivideLineStroke(float stroke) {
        this.mDivideLineStroke = stroke;
    }

    public void setText(String[] mText) {
        if(mText==null||mText.length!=12){
            throw new IllegalArgumentException("the text array's length must be 12");
        }
        this.mText = mText;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setOnSelectListener(OnSelectedListener listener){
        mListener=listener;
    }
    public interface OnSelectedListener{
        void onSelected(int year,int position);
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
        //计算最小偏移量，等于右边显示6-12个月时，中间的一年的offet为它本身的12月加上下一年之前的5个月
        mMinOffset=-(mText.length-mVisibleNum)*mColumnSize-mText.length*mColumnSize;
        //计算最大偏移量，等于左边显示1-6月时，offset就等于左边12个月乘宽度
        mMaxOffset= (int) (mText.length*mColumnSize);
        mOneYearWidth=(int) (mText.length*mColumnSize);
        mRowHeight=getHeight();
        mOffset=getOffset();
    }

    /**
     * 获取在一年内时，将选中的月份居中显示的偏移量，
     * @return
     */
    private float getOffset() {

        if(mSelectMonth-mVisibleNum/2>=0&&mSelectMonth+mVisibleNum/2<=11) {
            return -(mSelectMonth-mVisibleNum/2)*mColumnSize;
        }else if(mSelectMonth-mVisibleNum/2<0){
            return Math.abs(mSelectMonth-mVisibleNum/2)*mColumnSize;
        }else if(mSelectMonth+mVisibleNum/2>11){
            return getWidth()-(mSelectMonth+mVisibleNum/2-mText.length+1)*mColumnSize-mOneYearWidth;
        }else {
            return mOffset;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint =new Paint();
        canvas.drawColor(mBackGround);
        drawLastYear(canvas,paint);
        drawThisYear(canvas,paint);
        drawNextYear(canvas,paint);
        if(mHasDivideLine){
            drawDivideLine(canvas,paint);
        }

    }

    private void drawNextYear(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(mTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        for(int i = 0;i < mText.length;i++) {
            paint.setColor(Color.BLACK);
            if(i==mSelectMonth&&mSelectYear==mNowYear+1){
                canvas.drawRect(mOffset+mOneYearWidth+mColumnSize * i,0,mOffset+mOneYearWidth+mColumnSize * i+mColumnSize,getHeight(),paint);
                paint.setColor(Color.WHITE);
            }
            //居中显示的关键代码
            float startX = mOffset+mOneYearWidth+mColumnSize * i + (mColumnSize - paint.measureText(mText[i])) / 2;
            float startY = mRowHeight / 2 - (paint.ascent() + paint.descent()) / 2;
            canvas.drawText(mText[i], startX, startY, paint);
        }
    }

    private void drawLastYear(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(mTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        for(int i = 0;i < mText.length;i++) {
            paint.setColor(Color.BLACK);
            if(i==mSelectMonth&&mSelectYear==mNowYear-1){
                canvas.drawRect(mOffset-mOneYearWidth+mColumnSize * i,0,mOffset-mOneYearWidth+mColumnSize * i+mColumnSize,getHeight(),paint);
                paint.setColor(Color.WHITE);
            }
            //居中显示的关键代码
            float startX = mOffset-mOneYearWidth+mColumnSize * i + (mColumnSize - paint.measureText(mText[i])) / 2;
            float startY = mRowHeight / 2 - (paint.ascent() + paint.descent()) / 2;
            canvas.drawText(mText[i], startX, startY, paint);
        }
    }

    public void setSelectDate(int year,int month){
            mSelectYear=year;
            mSelectMonth=month;
            startAnimation();
            Log.d("select",mSelectYear+"  "+mSelectMonth);
    }
    private void drawThisYear(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(mTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        for(int i = 0;i < mText.length;i++) {
            paint.setColor(Color.BLACK);
            if(i==mSelectMonth&&mSelectYear==mNowYear){
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

    private float mXDown,mXFirstDown;
    private final int SELECT_TRIGGER=8;
    private boolean isSelect=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXDown=event.getX();
                mXFirstDown=event.getX();
                isSelect=true;

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float distance=event.getX()-mXDown;
                mVelocityTracker.computeCurrentVelocity(1000, MAX_VELOCITY);
                if(Math.abs(event.getX()-mXFirstDown)>SELECT_TRIGGER){
                    isSelect=false;
                }
                scroll(distance);
                mXDown=event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(isSelect){
                    mSelectYear=mNowYear;
                    //点击了今年或下一年
                    if(-mOffset+mXFirstDown>0){
                        mSelectMonth=(int) ((-mOffset+mXFirstDown)/mColumnSize);
                        //点击了下一年
                        if(mSelectMonth>=mText.length){
                            mSelectMonth%=mText.length;
                            mSelectYear++;
                        }
                    }else {
                        //点击了去年
                        mSelectMonth=(int) ((-mOffset+mXFirstDown)/mColumnSize);
                        mSelectYear--;
                        mSelectMonth+=mText.length-1;
                    }

                    Log.d("select",mSelectYear+"  "+mSelectMonth);
                    startAnimation();
                    if(mListener!=null){
                        mListener.onSelected(mSelectYear,mSelectMonth);
                    }
                    invalidate();
                }else{
                    final float velocityX = mVelocityTracker.getXVelocity();
                    releaseVelocityTracker();
                    Log.d("scroll1","velocityX   "+velocityX);
                    mScroller.fling(0,0,(int)velocityX,0, -mOneYearWidth,mOneYearWidth,0,0);
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scroll(mScroller.getCurrX()-mPreScrollX);
            mPreScrollX=mScroller.getCurrX();
            invalidate();
        }else {
            mPreScrollX=0;
        }
    }
    private void scroll(float distance) {
        if(distance>0){
            mOffset=mOffset+distance<=mMaxOffset? mOffset+=distance:mMaxOffset;
        }else {
            mOffset=mOffset+distance>=mMinOffset? mOffset+=distance:mMinOffset;
        }
        //滑到了下一年,今年已经完全不可见
        if(mOffset<=-mOneYearWidth){
            mOffset+=mOneYearWidth;
            mNowYear++;
            //滑到了上一年，今年已经完全不可见
        }else if(mOffset>=mOneYearWidth){
            mOffset-=mOneYearWidth;
            mNowYear--;
        }
    }

    private long mLastAnimTime;
    private void startAnimation() {
        //用户快速滑动时不用动画
        if(System.currentTimeMillis()-mLastAnimTime< 300){
            mOffset=getOffset();
            mNowYear=mSelectYear;
            invalidate();
            mLastAnimTime=System.currentTimeMillis();
            return;
        }
        if(mColumnSize!=0){
            float animOffset=getOffset();
            //计算动画偏移量，因为getOffset是获取一年内选中月份时的偏移量，
            // 当点击下一年的或上一年的月份时，getOffset时相对于本年内的偏移量，所以还要加上或减去一年的长度才是动画偏移量
            if(mSelectYear>mNowYear){
                animOffset-=mOneYearWidth;
            }else if(mSelectYear<mNowYear){
                animOffset+=mOneYearWidth;
            }
            if(animOffset==mOffset){
                return;
            }
            ValueAnimator animator = ValueAnimator.ofFloat(mOffset,animOffset);
            animator.setDuration(mAnimDuration);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mOffset= (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //动画做完后再调整偏移位置，相当于将中间的一年又扯回到了中间（因为动画的执行完时，中间的12个月份可能已经不可见了）
                    //这步主要目的是为了无限轮滑。
                    mOffset=getOffset();
                    mNowYear=mSelectYear;
                    invalidate();
                }
            });
            mLastAnimTime=System.currentTimeMillis();
            animator.start();
        }

    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }


    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    private void acquireVelocityTracker(final MotionEvent event) {
        if(null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
}
