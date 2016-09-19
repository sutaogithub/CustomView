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
public class CalendarView extends LinearLayout{

    private static final float WEEK_HEIGHT_WEIGHT =1 ;
    private static final float CALENDAR_HEIGHT_WEIGHT =13 ;
    private static final float TITLE_HEIGHT_WEIGHT = 2;
    private final int WEEK_BACKGROUND =0xfff9f9f9 ;
    private final float WEEK_TEXT_SIZE =15 ;
    private final int WEEK_TEXT_COLOR =Color.BLACK;
    private final DisplayMetrics mDisplayMetrics;
    private final int TITLE_BACKGROUND=0xfff9f9f9;
    private final float TITLE_TEXT_SIZE=20;
    private final int TITLE_TEXT_COLOR= Color.BLACK;
    private final float DIVIDE_LINE_STROKE=0.5f;
    private final int DIVIDE_LINE_COLOR=Color.GRAY;
    private final int WEEKDAY_BACKGROUND=Color.WHITE;
    private final int WEEKDAY_TEXT_COLOR=Color.BLACK;
    private final int WEEKDAY_SELECT_COLOR=Color.WHITE;
    private final int CIRCLE__COLOR=Color.BLACK;
    private final float CIRCLE_RADIUS=20;
    private final float WEEKDAY_TEXT_SIZE=15;
    private final int LASTMONTH_TEXT_COLOR=Color.GRAY;
    private final int TITLE_HEIGHT_RATIO=2;
    private final int WEEK_HEIGHT_RATIO=1;
    private DateTitleView mTitle;
    private CalendarPager mCalendarPager;
    private DateView.OnDateSelectListener mListener;
    private WeekView mWeekView;
    private List<DateView> mList;
    public CalendarView(Context context) {
        this(context,null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        initView(context);
        initArribute(context, attrs);
    }
    protected void initArribute(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        int  titleBackground= arr.getColor(R.styleable.CalendarView_titleBackground,TITLE_BACKGROUND);
        float  titleTextSize= arr.getDimension(R.styleable.CalendarView_titleTextSize,getTextSizeSp(TITLE_TEXT_SIZE));
        int  titleText_Color= arr.getColor(R.styleable.CalendarView_titleText_Color,TITLE_TEXT_COLOR);
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
        int titleHeight=arr.getInt(R.styleable.CalendarView_titleHeightWeight,TITLE_HEIGHT_RATIO);
        int weekHeight= arr.getColor(R.styleable.CalendarView_weekHeightWeight,WEEK_HEIGHT_RATIO);
        int weekBackground= arr.getColor(R.styleable.CalendarView_weekBackground,WEEK_BACKGROUND);
        float weekTextSize= arr.getDimension(R.styleable.CalendarView_weekTextSize,getTextSizeSp(WEEK_TEXT_SIZE));
        int weekTextColor=arr.getColor(R.styleable.CalendarView_weekTextColor,WEEK_TEXT_COLOR);
        mTitle.setTitltBackgroud(titleBackground);
        mTitle.setTitleSize(titleTextSize);
        mTitle.setTitleTextColor(titleText_Color);
        for(DateView view:mList){
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
            view.invalidate();
        }
        mWeekView.setWeekTextSize(weekTextSize);
        mWeekView.setBackground(weekBackground);
        mWeekView.setWeekTextColor(weekTextColor);
        LayoutParams params = (LayoutParams) mTitle.getLayoutParams();
        params.weight=titleHeight;
        mTitle.setLayoutParams(params);
        LayoutParams params1 = (LayoutParams) mWeekView.getLayoutParams();
        params1.weight=weekHeight;
        mWeekView.setLayoutParams(params1);
        requestLayout();
    }

    private void initView(Context context) {
        mTitle= new DateTitleView(context);
        mCalendarPager = new CalendarPager(context);
        mWeekView= new WeekView(context);
        mTitle.setTitleDate(mCalendarPager.getSelectYear(), mCalendarPager.getSelectMonth(), mCalendarPager.getSelectDay());
        mCalendarPager.setOnDateSelectListener(new DateView.OnDateSelectListener() {
            @Override
            public void onSelected(int year, int month, int day) {
                mTitle.setTitleDate(year,month,day);
                if(mListener!=null){
                    mListener.onSelected(year,month,day);
                }
            }
        });
        mList= mCalendarPager.getPager();
        LayoutParams weekParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, WEEK_HEIGHT_WEIGHT);
        LayoutParams titleParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, TITLE_HEIGHT_WEIGHT);
        LayoutParams calendarParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, CALENDAR_HEIGHT_WEIGHT);
        setOrientation(VERTICAL);
        addView(mTitle,titleParams);
        addView(mWeekView,weekParams);
        addView(mCalendarPager,calendarParams);
    }

    public void setOnDateSelectListener(DateView.OnDateSelectListener listener){
        mListener=listener;
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }
}
