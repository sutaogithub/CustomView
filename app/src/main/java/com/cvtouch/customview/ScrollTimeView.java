package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/9/16.
 */
public class ScrollTimeView extends View {
    private Bitmap mBtnImg,mClockImg;
    private DisplayMetrics mDisplayMetrics;
    private float mLineYMargin;
    private float mRealHeight;
    private float DEFAULT_HEIGHT=1000;
    private float DEFAULT_WIDTH=300;
    private float  btnVerOffset;
    private float  rectStartY;
    private float mLineInterval;
    private float  rectEndY;
    private float  bottomBtnHorOffset;
    private float  topBtnHorOffset;
    private int btnSize;
    private int btnAreaOffset;
    private int mClockHorOffset;
    private int mClockVerOffset;
    private int mClockSize;
    private Rect mBottomBtnImg;
    private Rect mTopBtnImg;
    private Rect mTopBtnArea;
    private Rect mBottomBtnArea;
    private Rect mClockArea;
    private float mMinRectHeight;
    private float mMoveTrigger;
    private float mTopScrollTrigger;
    private final float mBtnMoveTrigger;
    private VelocityTracker mVelocityTracker;
    private int mNestedYOffset;
    private Scroller mScroller;
    private final int mTouchMoveTrigger;
    private float mMaxVelocity;
    private float mLineXMargin;
    private float mTextSize;
    private int mLineNum;
    private int mMaxRectNum;
    private float mTimeTextXOffset;
    private int mStartMinute;
    private int mEndMinute;
    private float mTimeTextWidth;
    private float mTimeTextSize;
    public ScrollTimeView(Context context) {
        this(context,null);
    }


    public ScrollTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mBtnImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cvt_circle);
        mClockImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cvt_clock);
        mRealHeight =mDisplayMetrics.density*2000;
        mLineYMargin=mDisplayMetrics.density*30;
        rectStartY= mLineYMargin;

        btnVerOffset = mDisplayMetrics.density*17;
        btnSize = (int) (mDisplayMetrics.density*36);
        btnAreaOffset =(int) mDisplayMetrics.density*6;


        mClockSize=(int) mDisplayMetrics.density*25;
        mMoveTrigger=mDisplayMetrics.density*3;
        mTopScrollTrigger=mDisplayMetrics.density*50;
        mBtnMoveTrigger=mDisplayMetrics.density*4;
        mScroller=new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mMaxVelocity=configuration.getScaledMaximumFlingVelocity();
        mTouchMoveTrigger = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mLineXMargin=mDisplayMetrics.density*50;
        bottomBtnHorOffset =mDisplayMetrics.density*30+mLineXMargin;
        mClockHorOffset = (int) (mLineXMargin+ mDisplayMetrics.density*10);
        mClockVerOffset =(int) mDisplayMetrics.density*5;
        topBtnHorOffset = (int) (mDisplayMetrics.density*60);
        mTextSize=mDisplayMetrics.density*15;
        mTimeTextXOffset =mDisplayMetrics.density*50;
        mTimeTextWidth =mDisplayMetrics.density*15;
        mTimeTextSize =mDisplayMetrics.density*15;
        //从0点开始画，画一个整点和半小时，24点不再画半小时
        mLineNum=2*24+1;
        mMaxRectNum=mLineNum-1;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLineInterval= ( mRealHeight -mLineYMargin*2)/(mLineNum-1);
        mMinRectHeight=mLineInterval;
        rectEndY= rectStartY+mMinRectHeight;
        setTimeRange(3,false,6,true);
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
        drawLine(canvas, paint);
        drawBlackRect(canvas,paint);
        drawButton(canvas,paint);
        drawClock(canvas,paint);
        drawTimeText(canvas,paint);

    }

    /**
     *
     * @param startHour 开始时间范围0-23
     * @param startHasHalf 是否有多半小时
     * @param endHour 结束时间 0-23
     * @param endHasHalf 是否有多半小时
     */
    public void setTimeRange(int startHour,boolean startHasHalf,int endHour,boolean endHasHalf){
        int start=startHour*2+(startHasHalf?1:0);
        int end=endHour*2+(endHasHalf?1:0);
        if(start>=end||start<0||end>mMaxRectNum){
            return;
        }
        mStartMinute=start*30;
        mEndMinute=end*30;
        rectStartY=mLineYMargin+start*mMinRectHeight;
        rectEndY=mLineYMargin+end*mMinRectHeight;
        invalidate();
    }
    private void drawTimeText(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setTextSize(mTimeTextSize);
        StringBuilder builder=new StringBuilder();
        if(mStartMinute/60<10){
            builder.append(0);
        }
        builder.append(mStartMinute/60);
        builder.append(":");
        if(mStartMinute%60<10){
            builder.append(0);
        }
        builder.append(mStartMinute%60);
        builder.append(" > ");
        if(mEndMinute/60<10){
            builder.append(0);
        }
        builder.append(mEndMinute/60);
        builder.append(":");
        if(mEndMinute%60<10){
            builder.append(0);
        }
        builder.append(mEndMinute%60);
        int startX = (int) (mClockArea.right+mTimeTextXOffset+((mTimeTextWidth - paint.measureText(builder.toString())) / 2));
        int startY = (int) (mClockArea.top+((mClockArea.bottom-mClockArea.top)/2 - (paint.ascent() + paint.descent()) / 2));
        canvas.drawText(builder.toString(),startX, startY, paint);
    }

    private void drawClock(Canvas canvas, Paint paint) {
        paint.reset();
        mClockArea=new Rect((int)mClockHorOffset,(int)(rectStartY+mClockVerOffset),(int)(mClockHorOffset+mClockSize),(int)(rectStartY+mClockVerOffset+mClockSize));
        canvas.drawBitmap(mClockImg,null,mClockArea,paint);
    }

    private void drawBlackRect(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mLineXMargin,rectStartY,getWidth(),rectEndY,paint);
    }

    private void drawLine(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setTextSize(mTextSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        PathEffect effects = new DashPathEffect(new float[]{10,10},0);
        paint.setPathEffect(effects);
        boolean isFullLine=true;
        for(int i=0;i<mLineNum;i++){
            if(isFullLine){
                canvas.drawLine(mLineXMargin,mLineYMargin+mLineInterval*i,getWidth(),mLineYMargin+mLineInterval*i,paint);
                //居中显示的关键代码
                String str=i/2+"时";
                int startX = (int) ((mLineXMargin - paint.measureText(str)) / 2);
                int startY = (int) (mLineInterval*i-mMinRectHeight+(int) (mLineYMargin+mMinRectHeight - (paint.ascent() + paint.descent()) / 2));
                canvas.drawText(str,startX, startY, paint);
            }else{
                path.reset();
                path.moveTo(mLineXMargin, mLineYMargin+mLineInterval*i);
                path.lineTo(getWidth(),mLineYMargin+mLineInterval*i);
                canvas.drawPath(path, paint);
            }
            isFullLine=!isFullLine;
        }
    }

    public void drawButton(Canvas canvas,Paint paint){
        int topLeft= (int) (getWidth()- topBtnHorOffset);
        int topTop= (int) (rectStartY- btnVerOffset);
        mTopBtnImg =new Rect(topLeft,topTop,topLeft+ btnSize,topTop+ btnSize);
        mTopBtnArea=new Rect(mTopBtnImg.left- btnAreaOffset,mTopBtnImg.top- btnAreaOffset,mTopBtnImg.right+ btnAreaOffset,mTopBtnImg.bottom+ btnAreaOffset);
        canvas.drawBitmap(mBtnImg,null, mTopBtnImg,paint);
        int bottomLeft= (int) bottomBtnHorOffset;
        int bottomTop= (int) (rectEndY- btnVerOffset);
        mBottomBtnImg =new Rect(bottomLeft,bottomTop,bottomLeft+ btnSize,bottomTop+ btnSize);
        mBottomBtnArea=new Rect(mBottomBtnImg.left- btnAreaOffset,mBottomBtnImg.top- btnAreaOffset,mBottomBtnImg.right+ btnAreaOffset,mBottomBtnImg.bottom+ btnAreaOffset);
        canvas.drawBitmap(mBtnImg,null,mBottomBtnImg,paint);
    }

    private float mPreY;
    private int mMoveDistance,mPointerId;
    private boolean isButton;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPreY=event.getY();
                mMoveDistance=0;
                isButton=false;
                mPointerId = event.getPointerId(0);
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dy= (int) (event.getY()-mPreY);
                mMoveDistance+=dy;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                if (mTopBtnArea.contains((int) event.getX(), (int) event.getY()+getScrollY())) {
                    isButton=true;
                    if(Math.abs(mMoveDistance)>mBtnMoveTrigger){
                        mMoveDistance =0;
                        if (dy > 0) {
                            if (rectEndY-rectStartY >=mMinRectHeight*2 ) {
                                if(event.getY()>getHeight()-mTopScrollTrigger&&getScrollY()+mMinRectHeight< mRealHeight -getHeight()){
                                    scrollBy(0, (int)mMinRectHeight);
                                }
                                rectStartY += mMinRectHeight;
                                mStartMinute+=30;
                            }
                        } else {
                            if (rectStartY-mMinRectHeight>=mLineYMargin&&rectEndY-rectStartY <=mMinRectHeight*mMaxRectNum) {
                                if(event.getY()<mTopScrollTrigger&&getScrollY()-mMinRectHeight>0){
                                    scrollBy(0, (int) -mMinRectHeight);
                                }
                                rectStartY -= mMinRectHeight;
                                mStartMinute-=30;
                            }
                        }
                        invalidate();
                    }
                } else if (mBottomBtnArea.contains((int) event.getX(), (int) event.getY()+getScrollY())) {
                    isButton=true;
                    if(Math.abs(mMoveDistance)>mBtnMoveTrigger){
                        mMoveDistance =0;
                        if (dy > 0) {

                            if (rectEndY +mMinRectHeight<=mRealHeight-mLineYMargin&&rectEndY-rectStartY <=mMinRectHeight*mMaxRectNum) {
                                if(event.getY()>getHeight()-mTopScrollTrigger&&getScrollY()+mMinRectHeight< mRealHeight -getHeight()){
                                    scrollBy(0, (int)mMinRectHeight);
                                }
                                rectEndY += mMinRectHeight;
                                mEndMinute+=30;
                            }
                        } else {
                            if (rectEndY-rectStartY >=mMinRectHeight*2) {
                                if(event.getY()<mTopScrollTrigger&&getScrollY()-mMinRectHeight>0){
                                    scrollBy(0, (int) -mMinRectHeight);
                                }
                                rectEndY -= mMinRectHeight;
                                mEndMinute-=30;
                            }
                        }
                        invalidate();
                    }
                }else if(!isButton&&getScrollY()-dy>0&&getScrollY()-dy< mRealHeight -getHeight()){
                    scrollBy(0, -dy);
                    Log.d("scroll",mRealHeight -getHeight()+"     "+getScrollY());
                }
                mPreY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                releaseVelocityTracker();
//                Log.d("scroll",velocityY+"");
                mScroller.fling(getScrollX(),getScrollY(),0,-(int)velocityY,0,0,0,(int)(mRealHeight -getHeight()));
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
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
