package com.cvtouch.customview;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/9
 */
public class SelectView extends View{

    private Bitmap mBtnImg,mClockImg;
    private int mHeight;
    private int mWidth;
    private  int topBtnHorOffset, bottomBtnHorOffset;
    private int btnAreaOffset;
    private  int btnVerOffset;
    private int mClockHorOffset;
    private int mClockVerOffset;
    private int btnSize;
    private  int rectStartY;
    private int rectEndY;
    private int mClockSize;
    private int mMinRectHeight;
    private int mMoveTrigger;
    private Paint mPaint;
    private Rect mTimeSectionArea;
    private Rect mTotalTimeArea;
    private Rect mTopBtnImg, mBottomBtnImg;
    private Rect mTopBtnArea,mBottomBtnArea;
    private Rect mClockArea;
    private onTouchMoveListener mListener;
    public interface onTouchMoveListener{
        void upMove();
        void downMove();

    }
    public void setMoveListener(onTouchMoveListener listener){
        mListener=listener;
    }
    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBtnImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cvt_circle);
        mClockImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.cvt_clock);
        mMinRectHeight=SystemUtils.dip2px(context,50);
        rectStartY=SystemUtils.dip2px(context,-100);
        rectEndY=SystemUtils.dip2px(context,75);
        topBtnHorOffset =SystemUtils.dip2px(context,60);
        bottomBtnHorOffset =SystemUtils.dip2px(context,30);
        btnVerOffset =SystemUtils.dip2px(context,17);
        btnSize =SystemUtils.dip2px(context,36);
        btnAreaOffset =SystemUtils.dip2px(context,15);
        mClockHorOffset =SystemUtils.dip2px(context,2);
        mClockVerOffset =SystemUtils.dip2px(context,2);
        mClockSize=SystemUtils.dip2px(context,25);
        mMoveTrigger=SystemUtils.dip2px(context,25);
        mPaint=new Paint();
    }
    public SelectView(Context context) {
        super(context,null);
    }

    private float mPreY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPreY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy=event.getY()-mPreY;
                if(Math.abs(dy)>mMoveTrigger){
                    if(mTopBtnArea.contains((int)event.getX(),(int)event.getY())){
                            if(dy>0){
                                if(rectStartY+mMinRectHeight<rectEndY){
                                    rectStartY+=mMinRectHeight;

                                }
                            }
                            else{
                                if(rectEndY-mMinRectHeight>0){
                                    rectStartY-=mMinRectHeight;

                                }
                            }
                            invalidate();
                    }else
                    if(mBottomBtnArea.contains((int)event.getX(),(int)event.getY())){
                            if(dy>0){
                                if(rectEndY+mMinRectHeight<mHeight){
                                    rectEndY+=mMinRectHeight;
                                    if(mListener!=null){
                                        mListener.downMove();
                                    }
                                }
                            }
                            else{
                                if(rectEndY-mMinRectHeight>rectStartY){
                                    rectEndY-=mMinRectHeight;
                                    if(mListener!=null){
                                        mListener.upMove();
                                    }
                                }
                            }

                        invalidate();
                    }
                    mPreY=event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:break;
        }

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight=getHeight();
        mWidth=getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(0x00,0x00,0X00,0X00);
        drawBlackRect(canvas);
        drawButton(canvas);

    }


    public void drawBlackRect(Canvas canvas){
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawRect(0,rectStartY,mWidth,rectEndY,mPaint);
    }
    public void drawButton(Canvas canvas){
        int topLeft=mWidth- topBtnHorOffset;
        int topTop=rectStartY- btnVerOffset;
        mTopBtnImg =new Rect(topLeft,topTop,topLeft+ btnSize,topTop+ btnSize);
        mTopBtnArea=new Rect(mTopBtnImg.left- btnAreaOffset,mTopBtnImg.top- btnAreaOffset,mTopBtnImg.right+ btnAreaOffset,mTopBtnImg.bottom+ btnAreaOffset);
        canvas.drawBitmap(mBtnImg,null, mTopBtnImg,mPaint);

        int bottomLeft= bottomBtnHorOffset;
        int bottomTop=rectEndY- btnVerOffset;
        mBottomBtnImg =new Rect(bottomLeft,bottomTop,bottomLeft+ btnSize,bottomTop+ btnSize);
        mBottomBtnArea=new Rect(mBottomBtnImg.left- btnAreaOffset,mBottomBtnImg.top- btnAreaOffset,mBottomBtnImg.right+ btnAreaOffset,mBottomBtnImg.bottom+ btnAreaOffset);
        canvas.drawBitmap(mBtnImg,null,mBottomBtnImg,mPaint);
        mClockArea=new Rect(mClockHorOffset,rectStartY+mClockVerOffset,mClockHorOffset+mClockSize,rectStartY+mClockVerOffset+mClockSize);
        canvas.drawBitmap(mClockImg,null,mClockArea,mPaint);
    }
}
