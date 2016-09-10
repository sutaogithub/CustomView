package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/10
 */
public class TimeView extends View{
    private int mTimeTxtOffset;
    private int mLineTxtOffset;
    private boolean isFullLine;
    private String mText="";
    public TimeView(Context context) {
        super(context,null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLineTxtOffset=SystemUtils.dip2px(context,40);
        mTimeTxtOffset=SystemUtils.dip2px(context,20);
    }

    public void setIsFullLine(boolean isfull){
        isFullLine=isfull;
        invalidate();
    }
    public void setText(String text){
        mText=text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        Paint paint=new Paint();
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        if(isFullLine){
            canvas.drawLine(mLineTxtOffset,getHeight()/2,getWidth(),getHeight()/2,paint);
        }else{
            PathEffect effects = new DashPathEffect(new float[] { 1, 2}, 1);
            paint.setPathEffect(effects);
            canvas.drawLine(mLineTxtOffset,getHeight()/2,getWidth(),getHeight()/2,paint);
        }
    }

    private void drawText(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(40);
        canvas.drawText(mText,mTimeTxtOffset,getHeight()/2,paint);
    }
}
