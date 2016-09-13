package com.cvtouch.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/13
 */
public class CalendarView extends LinearLayout{

    private DateTitleView mTitle;
    private CalendarPager mCalendar;
    private DateView.OnDateSelectListener mListener;
    public CalendarView(Context context) {
        this(context,null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View root=LayoutInflater.from(context).inflate(R.layout.calendar_view_layout, this, true);
        mTitle= (DateTitleView) findViewById(R.id.view_title);
        mCalendar= (CalendarPager) findViewById(R.id.viewpager_calendar);
        mTitle.setTitleDate(mCalendar.getSelectYear(),mCalendar.getSelectMonth(),mCalendar.getSelectDay());
        mCalendar.setOnDateSelectListener(new DateView.OnDateSelectListener() {
            @Override
            public void onSelected(int year, int month, int day) {
                mTitle.setTitleDate(year,month,day);
                if(mListener!=null){
                    mListener.onSelected(year,month,day);
                }
            }
        });
    }
    public void setOnDateSelectListener(DateView.OnDateSelectListener listener){
        mListener=listener;
    }
}
