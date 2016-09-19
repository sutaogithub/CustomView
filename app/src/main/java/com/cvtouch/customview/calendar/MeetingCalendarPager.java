package com.cvtouch.customview.calendar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/19
 */
public class MeetingCalendarPager extends CalendarPager {
    public MeetingCalendarPager(Context context) {
        super(context);
    }

    public MeetingCalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected DateView getDateView(Context context) {
        return new MeetingDateView(context);
    }
}
