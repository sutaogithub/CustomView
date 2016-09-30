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
 * @brief 会议日历组合控件
 * @date 2016/9/13
 */
public class MeetingCalendarView extends LinearLayout {

    private static final float DEFAULT_DONT_SET = -1;
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
    private DateView.OnDateSelectListener mListener;
    private MeetingCalendarPager mCalendarPager;
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
        mCalendarPager = new MeetingCalendarPager(context);
        mMonthView= new MonthSelectView(context);
        mMonthView.setSelectDate(mCalendarPager.getSelectYear(), mCalendarPager.getSelectMonth());
        mMonthView.setOnSelectListener(new MonthSelectView.OnSelectedListener() {
            @Override
            public void onSelected(int year, int month) {
                mCalendarPager.setDate(year,month);
            }
        });
        mCalendarPager.setOnDateChangeListener(new CalendarPager.OnDateChangeListener() {

            @Override
            public void onDateChange(int year, int month) {
                mMonthView.setSelectDate(year,month);
            }
        });
        mCalendarPager.setOnDateSelectListener(new DateView.OnDateSelectListener() {
            @Override
            public void onSelected(int year, int month, int day) {
                if(mListener!=null){
                    mListener.onSelected(year,month,day);
                }
                mMonthView.setSelectDate(year,month);
            }
        });
        mWeekView= new WeekView(context);
        mList= mCalendarPager.getPager();
        LayoutParams weekParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, WEEK_HEIGHT_WEIGHT);
        LayoutParams monthParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, MONTH_HEIGHT_WEIGHT);
        LayoutParams calendarParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, CALENDAR_HEIGHT_WEIGHT);
        setOrientation(VERTICAL);
        addView(mWeekView,weekParams);
        addView(mCalendarPager,calendarParams);
        addView(mMonthView,monthParams);
    }

    public MeetingCalendarView(Context context) {
        this(context,null);

    }
    private void initAttribute(Context context,AttributeSet attrs){
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.Calendar);
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
        int weekHeight= arr.getInt(R.styleable.Calendar_weekHeightWeight, WEEK_HEIGHT_WEIGHT);
        int weekBackground= arr.getColor(R.styleable.Calendar_weekBackground,WEEK_BACKGROUND);
        float weekTextSize= arr.getDimension(R.styleable.Calendar_weekTextSize,getTextSizeSp(WEEK_TEXT_SIZE));
        int weekTextColor=arr.getColor(R.styleable.Calendar_weekTextColor,WEEK_TEXT_COLOR);
        String meetingString=arr.getString(R.styleable.Calendar_meetingString);
        float meetingTextSize=arr.getDimension(R.styleable.Calendar_meetingTextSize,getTextSizeSp(MEETING_TEXT_SIZE));
        int meetingTextColor=arr.getColor(R.styleable.Calendar_meetingTextColor,MEETING_TEXT_COLOR);
        int selectRectColor=arr.getColor(R.styleable.Calendar_selectRectColor,SELECT_RECT_COLOR);
        int notThisMonthColor=arr.getColor(R.styleable.Calendar_notThisMonthColor,NOT_THIS_MONTH_COLOR);
        int monthHeightWeight=arr.getInt(R.styleable.Calendar_monthHeightWeight,MONTH_HEIGHT_WEIGHT);
        int calendarHeight=arr.getInt(R.styleable.Calendar_calendarHeightWeight,CALENDAR_HEIGHT_WEIGHT);
        float meetingWeekdayTextYOffset=arr.getDimension(R.styleable.Calendar_meetingStringYOffset,DEFAULT_DONT_SET);
        float meetingStringYOffset=arr.getDimension(R.styleable.Calendar_meetingStringYOffset,DEFAULT_DONT_SET);
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
            if(meetingWeekdayTextYOffset>0){
                view.setWeekdayTextYOffset(meetingWeekdayTextYOffset);
            }
            if(meetingStringYOffset>0){
                view.setMeetingStringYOffset(meetingStringYOffset);
            }
            if(meetingString!=null&&meetingString.equals("")){
                view.setMeetingString(meetingString);
            }else {
                view.setMeetingString(MEETING_STRING);
            }
            view.invalidate();
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

        LayoutParams params2 = (LayoutParams) mCalendarPager.getLayoutParams();
        params2.weight=calendarHeight;
        mCalendarPager.setLayoutParams(params2);
        requestLayout();
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }

    public void setOnDateSelectListener(DateView.OnDateSelectListener listener){
        mListener=listener;
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
     * 设置月份选择条的动画时间
     * @param time
     */
    public void setMonthScrollAnimDuration(long time){
        mMonthView.setAnimDuration(time);
        mMonthView.invalidate();
    }

    /**
     * 设置月份选择条的背景颜色
     * @param backGround
     */
    public void setMonthBackGround(int backGround) {
        mMonthView.setBackGround(backGround);
        mMonthView.invalidate();
    }

    /**
     * 设置一个屏幕宽度下，月份可视的数目
     * @param visibleNum
     */
    public void setMonthVisibleNum(int visibleNum) {
        mMonthView.setVisibility(visibleNum);
    }

    /**
     * 设置是否有分割线
     * @param flag
     */
    public void setDivideLine(boolean flag) {
        mMonthView.setDivideLine(flag);
        mMonthView.invalidate();
    }

    /**
     * 设置月份分割线的颜色
     * @param color
     */
    public void setMonthDivideLineColor(int color) {
        mMonthView.setDivideLineColor(color);
        mMonthView.invalidate();
    }
    /**
     * 设置月份分割线的宽度
     * @param stroke
     */
    public void setMonthDivideLineStroke(float stroke) {
        mMonthView.setDivideLineStroke(stroke);
        mMonthView.invalidate();
    }

    /**
     * 设置月份的文字
     * @param text
     */
    public void setMonthText(String[] text) {
        mMonthView.setText(text);
        mMonthView.invalidate();
    }

    /**
     * 设置月份的文字大小
     * @param textSize
     */
    public void setMonthTextSize(float textSize) {
        mMonthView.setTextSize(textSize);
    }
    /**
     * 设置会议部分文字大小
     * @param meetingTextSize
     */
    public void setMeetingTextSize(float meetingTextSize) {
        for(MeetingDateView dateView:mList){
            dateView.setMeetingTextSize(meetingTextSize);
            invalidate();
        }
    }

    /**
     * 设置日期相对于Y轴的偏移
     * @param weekDayTextYOffset
     */
    public void setWeekDayTextYOffset(float weekDayTextYOffset) {
        for(MeetingDateView dateView:mList){
            dateView.setWeekdayTextYOffset( weekDayTextYOffset) ;
            invalidate();
        }
    }

    /**
     * 设置“场”字相对于中心的偏移
     * @param stringMeetingYOffset
     */
    public void setStringMeetingYOffset(float stringMeetingYOffset) {
        for(MeetingDateView dateView:mList){
            dateView.setMeetingStringYOffset( stringMeetingYOffset) ;
            invalidate();
        }
    }

    /**
     * 设置会议单位的文字，默认“场”字
     * @param stringMeeting
     */
    public void setMeetingString(String stringMeeting) {
        for(MeetingDateView dateView:mList){
            dateView.setMeetingString( stringMeeting) ;
            invalidate();
        }

    }

    /**
     * 设置备选中日期上方的矩形的颜色
     * @param selectRectColor
     */
    public void setSelectRectColor(int selectRectColor) {
        for(MeetingDateView dateView:mList){
            dateView.setSelectRectColor( selectRectColor) ;
            invalidate();
        }
    }

    /**
     * 设置被选中的日期上方黑色矩形的高度
     * @param selectRectHeight
     */
    public void setSelectRectHeight(float selectRectHeight) {
        for(MeetingDateView dateView:mList){
            dateView.setSelectRectHeight( selectRectHeight) ;
            invalidate();
        }
    }

    /**
     * 设置会议部分文字的颜色
     * @param meetingTextColor
     */
    public void setMeetingTextColor(int meetingTextColor) {
        for(MeetingDateView dateView:mList){
            dateView.setMeetingTextColor( meetingTextColor) ;
            invalidate();
        }
    }

    /**
     * 设置不是本月的空格的颜色
     * @param mNotThisMonthColor
     */
    public void setNotThisMonthColor(int mNotThisMonthColor) {
        for(MeetingDateView dateView:mList){
            dateView.setNotThisMonthColor( mNotThisMonthColor) ;
            invalidate();
        }
    }

    /**
     * 设置几号有多少场会议,注意日期从0开始
     * @param provider
     */
    public void setMeetingDataProvider(MeetingCalendarPager.GetMeetingData provider){
           mCalendarPager.setGetMeetingData(provider);
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
     * 设置日历翻页动画
     * @param anim
     */
    public void setScrollAnimation(ViewPager.PageTransformer anim){
        mCalendarPager.setPageTransformer(true,anim);
    }
}
