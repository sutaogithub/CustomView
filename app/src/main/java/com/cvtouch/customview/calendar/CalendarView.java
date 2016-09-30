package com.cvtouch.customview.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
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
 * @brief 日历控件
 * @date 2016/9/13
 */
public class CalendarView extends LinearLayout{

    private static final int WEEK_HEIGHT_WEIGHT =1 ;
    private static final int CALENDAR_HEIGHT_WEIGHT =13 ;
    private static final int TITLE_HEIGHT_WEIGHT = 2;
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
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.Calendar);
        int  titleBackground= arr.getColor(R.styleable.Calendar_titleBackground,TITLE_BACKGROUND);
        float  titleTextSize= arr.getDimension(R.styleable.Calendar_titleTextSize,getTextSizeSp(TITLE_TEXT_SIZE));
        int  titleText_Color= arr.getColor(R.styleable.Calendar_titleText_Color,TITLE_TEXT_COLOR);
        boolean  horizontalDivideLine= arr.getBoolean(R.styleable.Calendar_horizontalDivideLine,true);
        boolean  verticalDivideLine=  arr.getBoolean(R.styleable.Calendar_verticalDivideLine,true);
        float  divideLineStroke= arr.getDimension(R.styleable.Calendar_divideLineStroke,DIVIDE_LINE_STROKE*mDisplayMetrics.density);
        int  divideLineColor= arr.getColor(R.styleable.Calendar_divideLineColor,DIVIDE_LINE_COLOR);
        int  weekdayBackground= arr.getColor(R.styleable.Calendar_weekdayBackground, WEEKDAY_BACKGROUND);
        int  weekdayTextColor= arr.getColor(R.styleable.Calendar_weekdayTextColor,WEEKDAY_TEXT_COLOR);
        int  weekdaySelectColor= arr.getColor(R.styleable.Calendar_weekdaySelectColor,WEEKDAY_SELECT_COLOR);
        int  circleColor= arr.getColor(R.styleable.Calendar_circleColor,CIRCLE__COLOR);
        float  circleRadius= arr.getDimension(R.styleable.Calendar_circleRadius,CIRCLE_RADIUS*mDisplayMetrics.density);
        float  weekdayTextSize= arr.getDimension(R.styleable.Calendar_weekdayTextSize,getTextSizeSp(WEEKDAY_TEXT_SIZE));
        int  lastMonthTextColor= arr.getColor(R.styleable.Calendar_lastMonthTextColor,LASTMONTH_TEXT_COLOR);
        int titleHeight=arr.getInt(R.styleable.Calendar_titleHeightWeight,TITLE_HEIGHT_RATIO);
        int weekHeight= arr.getInt(R.styleable.Calendar_weekHeightWeight,WEEK_HEIGHT_RATIO);
        int calendarHeight=arr.getInt(R.styleable.Calendar_calendarHeightWeight,CALENDAR_HEIGHT_WEIGHT);
        int weekBackground= arr.getColor(R.styleable.Calendar_weekBackground,WEEK_BACKGROUND);
        float weekTextSize= arr.getDimension(R.styleable.Calendar_weekTextSize,getTextSizeSp(WEEK_TEXT_SIZE));
        int weekTextColor=arr.getColor(R.styleable.Calendar_weekTextColor,WEEK_TEXT_COLOR);
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

        LayoutParams params2 = (LayoutParams) mCalendarPager.getLayoutParams();
        params2.weight=calendarHeight;
        mCalendarPager.setLayoutParams(params2);
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

    //设置日期选中监听
    public void setOnDateSelectListener(DateView.OnDateSelectListener listener){
        mListener=listener;
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }
    /**
     * 设置日期文字
     * @param mTitleText 月，日，周 的文字，要长度为3，并按月，日，周的顺序
     */
    public void setTitleDateText(String[] mTitleText) {
        mTitle.setTitleText(mTitleText);
        mTitle.invalidate();
    }

    /**
     * 设置周几的文字
     * @param mWeekText 长度必须为7，从周日开始,周六最后
     */
    public void setTitleWeekText(String[] mWeekText) {
        mTitle.setWeekText(mWeekText);
        mTitle.invalidate();
    }
    /**
     * 设置星期几的文字
     * @param text
     */
    public void setWeekText(String[] text){
        mWeekView.setWeekText(text);
        mWeekView.invalidate();
    }

    /**
     * 设置星期几的背景
     * @param color
     */
    public void setWeekBackground(int color){
        mWeekView.setBackground(color);
        mWeekView.invalidate();
    }

    /**
     * 设置星期几的文字颜色
     * @param color
     */
    public void setWeekTextColor(int color){
        mWeekView.setWeekTextColor(color);
        mWeekView.invalidate();
    }

    /**
     * 设置星期几的文字尺寸
     * @param size
     */
    public void setWeekTextSize(float size){
        mWeekView.setWeekTextSize(size);
        mWeekView.invalidate();
    }


    /**
     * 设置水平分割线
     * @param hasHorizontalDivideLine
     */
    public void setHorizontalDivideLine(boolean hasHorizontalDivideLine) {
        for(DateView view:mList){
            view.setHorizontalDivideLine (hasHorizontalDivideLine);
            view.invalidate();
        }
    }

    /**
     * 设置竖直分割线
     * @param hasVerticalDivideLine
     */
    public void setVerticalDivideLine(boolean hasVerticalDivideLine) {
        for(DateView view:mList){
            view.setVerticalDivideLine (hasVerticalDivideLine);
            view.invalidate();
        }
    }

    /**
     * 设置分割线的宽度
     * @param mDivideLineStroke
     */
    public void setDivideLineStroke(float mDivideLineStroke) {
        for(DateView view:mList){
            view.setDivideLineStroke (mDivideLineStroke);
            view.invalidate();
        }
    }

    /**
     * 设置分割线的颜色
     * @param mDivideLineColor
     */
    public void setDivideLineColor(int mDivideLineColor) {
        for(DateView view:mList){
            view.setDivideLineColor (mDivideLineColor);
            view.invalidate();
        }
    }

    /**
     * 设置日期的背景颜色
     * @param mWeekDayBackground
     */
    public void setWeekdayBackground(int mWeekDayBackground) {
        for(DateView view:mList){
            view.setWeekdayBackground (mWeekDayBackground);
            view.invalidate();
        }
    }

    /**
     * 设置日期的文字颜色
     * @param mWeekDayColor
     */
    public void setWeekdayTextColor(int mWeekDayColor) {
        for(DateView view:mList){
            view.setWeekdayTextColor (mWeekDayColor);
            view.invalidate();
        }
    }

    /**
     * 设置日期被选中的颜色
     * @param mWeekDaySelectColor
     */
    public void setWeekdaySelectColor(int mWeekDaySelectColor) {
        for(DateView view:mList){
            view.setWeekdaySelectColor (mWeekDaySelectColor);
            view.invalidate();
        }
    }

    /**
     * 设置日期被选中时，圆的颜色
     * @param mCircleColor
     */
    public void setCircleColor(int mCircleColor) {
        for(DateView view:mList){
            view.setCircleColor (mCircleColor);
            view.invalidate();
        }
    }

    /**
     * 设置上个月的文字颜色
     * @param mLastMonthTextColor
     */
    public void setLastMonthTextColor(int mLastMonthTextColor) {
        for(DateView view:mList){
            view.setLastMonthTextColor (mLastMonthTextColor);
            view.invalidate();
        }
    }

    /**
     * 设置日期文字的大小
     * @param mWeekDayTextSize
     */
    public void setWeekdayTextSize(float mWeekDayTextSize) {
        for(DateView view:mList){
            view.setWeekdayTextSize (mWeekDayTextSize);
            view.invalidate();
        }
    }

    /**
     * 设置圆的尺寸
     * @param mCircleRadius
     */
    public void setCircleRadius(float mCircleRadius) {
        for(DateView view:mList){
            view.setCircleRadius (mCircleRadius);
            view.invalidate();
        }
    }

    /**
     * 设置日历翻页动画
     * @param anim
     */
    public void setScrollAnimation(ViewPager.PageTransformer anim){
        mCalendarPager.setPageTransformer(true,anim);
    }
}
