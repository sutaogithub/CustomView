package com.cvtouch.customview;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/14
 */
public class TimeSelectView extends View {

    private final DisplayMetrics mDisplayMetrics;
    private final Bitmap mBtnImg;
    private final Bitmap mClockImg;
    private float mLineYMargin;
    private int  btnVerOffset;
    private float  rectStartY;
    private float mLineInterval;
    private float  rectEndY;
    private int  bottomBtnHorOffset;
    private int  topBtnHorOffset;
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

    public TimeSelectView(Context context) {
        this(context,null);
    }

    public TimeSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mBtnImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cvt_circle);
        mClockImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cvt_clock);
        rectEndY= (int) (mDisplayMetrics.density*75);
        topBtnHorOffset = (int) (mDisplayMetrics.density*60);
        bottomBtnHorOffset = (int) (mDisplayMetrics.density*30);
        btnVerOffset = (int) (mDisplayMetrics.density*17);
        btnSize = (int) (mDisplayMetrics.density*36);
        btnAreaOffset =(int) mDisplayMetrics.density*6;
        mClockHorOffset =(int) mDisplayMetrics.density*2;
        mClockVerOffset =(int) mDisplayMetrics.density*2;
        mClockSize=(int) mDisplayMetrics.density*25;
        mLineYMargin=mDisplayMetrics.density*30;
        rectStartY= mLineYMargin;
        mMoveTrigger=mDisplayMetrics.density*3;
        mTopScrollTrigger=mDisplayMetrics.density*80;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLineInterval= ((float)getHeight()-mLineYMargin*2)/48;
        mMinRectHeight=mLineInterval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        for(int i=0;i<=48;i++){
            canvas.drawLine(0,mLineYMargin+mLineInterval*i,getWidth(),mLineYMargin+mLineInterval*i,paint);
        }
//        drawBlackRect(canvas,paint);
        drawButton(canvas,paint);
    }

    public void drawBlackRect(Canvas canvas,Paint paint){
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        canvas.drawRect(0,rectStartY,getWidth(),rectEndY,paint);
    }
    public void drawButton(Canvas canvas,Paint paint){
        int topLeft=getWidth()- topBtnHorOffset;
        int topTop= (int) (rectStartY- btnVerOffset);
        mTopBtnImg =new Rect(topLeft,topTop,topLeft+ btnSize,topTop+ btnSize);
        mTopBtnArea=new Rect(mTopBtnImg.left- btnAreaOffset,mTopBtnImg.top- btnAreaOffset,mTopBtnImg.right+ btnAreaOffset,mTopBtnImg.bottom+ btnAreaOffset);
        canvas.drawBitmap(mBtnImg,null, mTopBtnImg,paint);


        int bottomLeft= bottomBtnHorOffset;
        int bottomTop= (int) (rectEndY- btnVerOffset);
        mBottomBtnImg =new Rect(bottomLeft,bottomTop,bottomLeft+ btnSize,bottomTop+ btnSize);
        mBottomBtnArea=new Rect(mBottomBtnImg.left- btnAreaOffset,mBottomBtnImg.top- btnAreaOffset,mBottomBtnImg.right+ btnAreaOffset,mBottomBtnImg.bottom+ btnAreaOffset);
        canvas.drawBitmap(mBtnImg,null,mBottomBtnImg,paint);

//        mClockArea=new Rect(mClockHorOffset,rectStartY+mClockVerOffset,mClockHorOffset+mClockSize,rectStartY+mClockVerOffset+mClockSize);
//        canvas.drawBitmap(mClockImg,null,mClockArea,paint);
    }


    private float mPreY,mToly;
    private boolean isButton;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mPreY;
                mToly+=dy;
                if (Math.abs(mToly)>mMoveTrigger&&mTopBtnArea.contains((int) event.getX(), (int) event.getY())) {
                    isButton=true;
                    if (dy > 0) {
                        if (rectEndY-rectStartY >=mMinRectHeight*2 ) {
                            rectStartY += mMinRectHeight;

                        }
                    } else {
                        if (rectStartY-mMinRectHeight>=mLineYMargin&&rectEndY - rectStartY <= 14*mMinRectHeight) {
                            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) getLayoutParams();
                            Log.d("trigger",event.getY()+params.topMargin+"");
                            if(event.getY()+params.topMargin<mTopScrollTrigger){
//                                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) getLayoutParams();
                                if(params.topMargin+mMinRectHeight<=0&&params.topMargin+mMinRectHeight>=((View)getParent()).getHeight()-getHeight()){
                                    params.topMargin+=mMinRectHeight;
                                }
                                setLayoutParams(params);
                                getParent().requestLayout();
                            }
                            rectStartY -= mMinRectHeight;
                        }
                    }
                    invalidate();
                } else if (Math.abs(mToly)>mMoveTrigger&&mBottomBtnArea.contains((int) event.getX(), (int) event.getY())) {
                    isButton=true;
                    if (dy > 0) {
                        if (rectEndY-rectStartY <=mMinRectHeight*14) {
                            rectEndY += mMinRectHeight;
                        }
                    } else {
                        if (rectEndY-rectStartY >=mMinRectHeight*2) {
                            rectEndY -= mMinRectHeight;

                        }
                    }
                    invalidate();
                }else{
                    if(!isButton&&Math.abs(mToly)>mMoveTrigger){
                        mToly=0;
                        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) getLayoutParams();
                        if(params.topMargin+dy<=0&&params.topMargin+dy>=((View)getParent()).getHeight()-getHeight()){
                            params.topMargin+=dy;
                        }
                        setLayoutParams(params);
                        getParent().requestLayout();
//                      invalidate();
                    }
                }
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mToly=0;
                isButton=false;
                break;
        }


        return true;
    }
}
