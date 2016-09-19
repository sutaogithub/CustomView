package com.cvtouch.customview.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cvtouch.customview.R;

import java.util.List;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/13
 */
public class MeetingCalendarView extends LinearLayout {

    private  final int MONTH_HEIGHT_WEIGHT = 4;
    private final int WEEK_HEIGHT_WEIGHT =3;
    private final int CALENDAR_HEIGHT_WEIGHT =40;
    private  final int NOT_THIS_MONTH_COLOR =Color.GRAY ;
    private  final int SELECT_RECT_COLOR =Color.BLACK ;
    private  final int MEETING_TEXT_COLOR =Color.RED ;
    private  final float MEETING_TEXT_SIZE = 15;
    private  final String MEETING_STRING ="场" ;
    private final int WEEK_BACKGROUND =0xfff9f9f9 ;
    private final float WEEK_TEXT_SIZE =15 ;
    private final int WEEK_TEXT_COLOR = Color.BLACK;
    private final DisplayMetrics mDisplayMetrics;
    private final float DIVIDE_LINE_STROKE=0.5f;
    private final int DIVIDE_LINE_COLOR=Color.GRAY;
    private final int WEEKDAY_BACKGROUND=Color.WHITE;
    private final int WEEKDAY_TEXT_COLOR=Color.BLACK;
    private final int WEEKDAY_SELECT_COLOR=Color.WHITE;
    private final int CIRCLE__COLOR=Color.BLACK;
    private final float CIRCLE_RADIUS=20;
    private final float WEEKDAY_TEXT_SIZE=15;
    private final int LASTMONTH_TEXT_COLOR=Color.GRAY;
    private CalendarPager mCalendar;
    private MonthSelectView mMonthView;
    private WeekView mWeekView;
    private List<MeetingDateView> mList;
    public MeetingCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        initView(context);
        initAttribute(context,attrs);
    }

    private void initView(Context context) {
        mCalendar= new MeetingCalendarPager(context);
        mMonthView= new MonthSelectView(context);
        mMonthView.setSelectDate(mCalendar.getSelectYear(),mCalendar.getSelectMonth());
        mMonthView.setOnSelectListener(new MonthSelectView.OnSelectedListener() {
            @Override
            public void onSelected(int year, int month) {
                mCalendar.setDate(year,month);
            }
        });
        mCalendar.setOnDateChangeListener(new CalendarPager.OnDateChangeListener() {

            @Override
            public void onDateChange(int year, int month) {
                mMonthView.setSelectDate(year,month);
            }
        });
        mWeekView= new WeekView(context);
        mList=mCalendar.getPager();
        LayoutParams weekParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, WEEK_HEIGHT_WEIGHT);
        LayoutParams monthParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, MONTH_HEIGHT_WEIGHT);
        LayoutParams calendarParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, CALENDAR_HEIGHT_WEIGHT);
        setOrientation(VERTICAL);
        addView(mWeekView,weekParams);
        addView(mCalendar,calendarParams);
        addView(mMonthView,monthParams);
    }

    public MeetingCalendarView(Context context) {
        this(context,null);

    }
    private void initAttribute(Context context,AttributeSet attrs){
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        boolean  horizontalDivideLine= arr.getBoolean(R.styleable.CalendarView_horizontalDivideLine,true);
        boolean  verticalDivideLine=  arr.getBoolean(R.styleable.CalendarView_verticalDivideLine,true);
        float  divideLineStroke= arr.getDimension(R.styleable.CalendarView_divideLineStroke,DIVIDE_LINE_STROKE*mDisplayMetrics.density);
        int  divideLineColor= arr.getColor(R.styleable.CalendarView_divideLineColor,DIVIDE_LINE_COLOR);
        int  weekdayBackground= arr.getColor(R.styleable.CalendarView_weekdayBackground, WEEKDAY_BACKGROUND);
        int  weekdayTextColor= arr.getColor(R.styleable.CalendarView_weekdayTextColor,WEEKDAY_TEXT_COLOR);
        int  weekdaySelectColor= arr.getColor(R.styleable.CalendarView_weekdaySelectColor,WEEKDAY_SELECT_COLOR);
        int  circleColor= arr.getColor(R.styleable.CalendarView_circleColor,CIRCLE__COLOR);
        float  circleRadius= arr.getDimension(R.styleable.CalendarView_circleRadius,CIRCLE_RADIUS*mDisplayMetrics.density);
        float  weekdayTextSize= arr.getDimension(R.styleable.CalendarView_weekdayTextSize,getTextSizeSp(WEEKDAY_TEXT_SIZE));
        int  lastMonthTextColor= arr.getColor(R.styleable.CalendarView_lastMonthTextColor,LASTMONTH_TEXT_COLOR);
        int weekHeight= arr.getColor(R.styleable.CalendarView_weekHeightWeight, WEEK_HEIGHT_WEIGHT);
        int weekBackground= arr.getColor(R.styleable.CalendarView_weekBackground,WEEK_BACKGROUND);
        float weekTextSize= arr.getDimension(R.styleable.CalendarView_weekTextSize,getTextSizeSp(WEEK_TEXT_SIZE));
        int weekTextColor=arr.getColor(R.styleable.CalendarView_weekTextColor,WEEK_TEXT_COLOR);
        String meetingString=arr.getString(R.styleable.CalendarView_meetingString);
        float meetingTextSize=arr.getDimension(R.styleable.CalendarView_meetingTextSize,getTextSizeSp(MEETING_TEXT_SIZE));
        int meetingTextColor=arr.getColor(R.styleable.CalendarView_meetingTextColor,MEETING_TEXT_COLOR);
        int selectRectColor=arr.getColor(R.styleable.CalendarView_selectRectColor,SELECT_RECT_COLOR);
        int notThisMonthColor=arr.getColor(R.styleable.CalendarView_notThisMonthColor,NOT_THIS_MONTH_COLOR);
        int monthHeightWeight=arr.getInteger(R.styleable.CalendarView_monthHeightWeight,MONTH_HEIGHT_WEIGHT);
        for(MeetingDateView view:mList){
            view.setHorizontalDivideLine(horizontalDivideLine);
            view.setVerticalDivideLine(verticalDivideLine);
            view.setDivideLineStroke(divideLineStroke);
            view.setDivideLineColor(divideLineColor);
            view.setWeekdayBackground(weekdayBackground);
            view.setWeekdayTextColor(weekdayTextColor);
            view.setWeekdaySelectColor(weekdaySelectColor);
            view.setCircleColor(circleColor);
            view.setCircleRadius(circleRadius);
            view.setWeekdayTextSize(weekdayTextSize);
            view.setLastMonthTextColor(lastMonthTextColor);
            view.setMeetingTextSize(meetingTextSize);
            view.setMeetingTextColor(meetingTextColor);
            view.setSelectRectColor(selectRectColor);
            view.setNotThisMonthColor(notThisMonthColor);
            view.invalidate();
            if(meetingString!=null&&meetingString.equals("")){
                view.setMeetingString(meetingString);
            }else {
                view.setMeetingString(MEETING_STRING);
            }
        }
        mWeekView.setWeekTextSize(weekTextSize);
        mWeekView.setBackground(weekBackground);
        mWeekView.setWeekTextColor(weekTextColor);
        LayoutParams params = (LayoutParams) mMonthView.getLayoutParams();
        params.weight=monthHeightWeight;
        mMonthView.setLayoutParams(params);
        LayoutParams params1 = (LayoutParams) mWeekView.getLayoutParams();
        params1.weight=weekHeight;
        mWeekView.setLayoutParams(params1);
        requestLayout();
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }
}
