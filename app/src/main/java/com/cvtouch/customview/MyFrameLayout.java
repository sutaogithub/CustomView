package com.cvtouch.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/30
 */
public class MyFrameLayout extends FrameLayout{

    private int mChildBottom;
    private int mChildTop;
    public MyFrameLayout(Context context) {
        this(context,null);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TimeSelectView mContent=new TimeSelectView(context);
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(mContent,params);
        setBackgroundColor(Color.GRAY);
        setPadding(0,400,0,400);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        View child=getChildAt(0);
        mChildTop=child.getTop();
        mChildBottom=child.getBottom();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }
}
