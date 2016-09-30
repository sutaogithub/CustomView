package com.cvtouch.customview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 星期显示
 * @date 2016/9/13
 */
public class WeekView extends View{
    //wrap_content时的默认高度
    private final int DEFAULT_HEIGHT=20;
    //wrap_content时的默认宽度
    private final int DEFAULT_WIDTH=300;
    private final DisplayMetrics mDisplayMetrics;
    //周几的文字大小
    private float mWeekTextSize;
    //周几的文字颜色
    private int mWeekTextColor;
    //周几的文字，避免反复创建
    private String[] mWeekText;
    //背景
    private int mBackground;

    public WeekView(Context context) {
        this(context,null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mWeekTextSize=getTextSizeSp(15);
        mWeekTextColor=Color.BLACK;
        mBackground=0xfff9f9f9;
        mWeekText=new String[]{"日","一","二","三","四","五","六"};
    }

    public void setWeekText(String[] text){
        if(text==null||text.length!=7){
            throw new IllegalArgumentException("the text array's length must be 7");
        }
        mWeekText=text;

    }
    public void setBackground(int color){
        mBackground=color;

    }
    public void setWeekTextColor(int color){
        mWeekTextColor=color;

    }
    public void setWeekTextSize(float size){
        mWeekTextSize=size;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        drawBackGround(canvas,paint);
        drawWeek(canvas,paint);
    }

    /**
     * 确保在wrap_content时能有最小的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

    private void drawBackGround(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mBackground);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
    }
    private void drawWeek(Canvas canvas, Paint paint) {
        paint.reset();
        paint.setTextSize(mWeekTextSize);
        paint.setColor(mWeekTextColor);
        int columnWidth = getWidth() / 7;
        for(int i=0;i < mWeekText.length;i++){
            String text = mWeekText[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth)/2;
            int startY =(int) (getHeight()/2 - (paint.ascent() + paint.descent())/2);
            canvas.drawText(text, startX, startY, paint);
        }
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }
}
