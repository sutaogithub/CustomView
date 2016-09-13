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
public class CalendarViewT extends LinearLayout {


    private CalendarPager mCalendar;
    private MonthSelectView mMonthView;
    public CalendarViewT(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.calendar_view2_layout, this, true);
                mCalendar= (CalendarPager) findViewById(R.id.viewpager_calendar);
        mMonthView= (MonthSelectView) findViewById(R.id.view_month);
        mMonthView.setSelectIndex(mCalendar.getSelectMonth());
        mMonthView.setOnSelectListener(new MonthSelectView.OnSelectedListener() {
            @Override
            public void onSelected(int position) {
                mCalendar.setMonth( position);
            }
        });
        mCalendar.setOnDateChangeListener(new CalendarPager.OnDateChangeListener() {

            @Override
            public void onDateChange(int year, int month) {
                mMonthView.setSelectIndex(month);
            }
        });
    }
    public CalendarViewT(Context context) {
        this(context,null);

    }
}
