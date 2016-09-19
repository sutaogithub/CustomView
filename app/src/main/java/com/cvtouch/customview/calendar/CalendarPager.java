package com.cvtouch.customview.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/13
 */
public class CalendarPager extends ViewPager{

    private final int FIST_PAGE_INDEX=1;
    private final int LAST_PAGE_INDEX=3;
    private List<DateView> mList;
    private int mCurrentPagePosition;
    private boolean mIsChanged;
    private Calendar mCalendar;
    private int mNowMonth;
    private int mNowYear;
    private int mSelectYear;
    private int mSelectMonth;
    private int mSelectDay;
    private DateView.OnDateSelectListener mSelectListener;
    private  OnDateChangeListener mDateChangeListener;

    protected DateView getDateView(Context context) {
        return new DateView(context);
    }

    public interface OnDateChangeListener {
       void onDateChange(int year,int month);
    }

    public void setOnDateChangeListener(OnDateChangeListener listener){
        mDateChangeListener=listener;
    }

    public void setOnDateSelectListener(DateView.OnDateSelectListener listener){
        mSelectListener =listener;
    }
    public CalendarPager(Context context) {
        this(context,null);
    }

    public int getSelectYear() {
        return mSelectYear;
    }

    public int getSelectMonth() {
        return mSelectMonth;
    }

    public int getSelectDay() {
        return mSelectDay;
    }

    public CalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mList=new ArrayList<>();
        mCalendar=Calendar.getInstance();
        mNowYear=mCalendar.get(Calendar.YEAR);
        mNowMonth=mCalendar.get(Calendar.MONTH);
        mSelectYear=mNowYear;
        mSelectMonth=mNowMonth;
        mSelectDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        DateView.OnDateSelectListener listener=new DateSelectListener();
        //为了无限轮滑用5张view
        for(int i=0;i<5;i++){
            DateView view=getDateView(context);
            view.setDateSelectListener(listener);
            view.setSelectDate(mSelectYear,mSelectMonth,mSelectDay);
            mList.add(view);
        }
        setAdapter(new MyPagerAdapter());
        addOnPageChangeListener(new PageChageListener());
        mCurrentPagePosition=FIST_PAGE_INDEX;
        updateDate(mCurrentPagePosition);
        setCurrentItem(mCurrentPagePosition, false);
    }

    private void updateDate(int position) {
        if(position<1||position>3){
            return;
        }
        //绘制上一个月,当月,下一个月
        if(mNowMonth==0){
            mList.get(position-1).setDate(mNowYear-1, 11);
        }else {
            mList.get(position-1).setDate(mNowYear, mNowMonth-1);
        }
        mList.get(position).setDate(mNowYear, mNowMonth);
        if(mNowMonth==11) {
            mList.get(position + 1).setDate(mNowYear+1, 0);
        }else {
            mList.get(position + 1).setDate(mNowYear, mNowMonth+1);
        }
        if(position==LAST_PAGE_INDEX){
            if(mNowMonth==11) {
                mList.get(FIST_PAGE_INDEX).setDate(mNowYear+1, 0);
            }else {
                mList.get(FIST_PAGE_INDEX).setDate(mNowYear, mNowMonth+1);
            }
        }
        if(position==FIST_PAGE_INDEX){
            if(mNowMonth==0){
                mList.get(LAST_PAGE_INDEX).setDate(mNowYear-1, 11);
            }else {
                mList.get(LAST_PAGE_INDEX).setDate(mNowYear, mNowMonth-1);
            }
        }
    }

    public int getNowYear() {
        return mNowYear;
    }

    public int getNowMonth() {
        return mNowMonth;
    }
    public void setMonth(int month){
        mNowMonth=month;
        updateDate(getCurrentItem());
    }

    public void setDate(int year, int month){
        mNowMonth=month;
        mNowYear=year;
        updateDate(getCurrentItem());

    }

    public class  MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if(mList==null){
                return 0;
            }
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(mList.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mList.get(position));
            return mList.get(position);
        }
    }
    public class PageChageListener implements OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("scroll",position+"   "+positionOffset+"      "+positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            mIsChanged = true;
            mNowYear=mList.get(position).getYear();
            mNowMonth=mList.get(position).getMonth();
            if(mDateChangeListener!=null&&position>=FIST_PAGE_INDEX&&position<=LAST_PAGE_INDEX){
                mDateChangeListener.onDateChange(mNowYear,mNowMonth);
            }
            updateDate(position);
            if (position > LAST_PAGE_INDEX) {
                mCurrentPagePosition = FIST_PAGE_INDEX;
            } else if (position < FIST_PAGE_INDEX) {
                mCurrentPagePosition = LAST_PAGE_INDEX;
            } else {
                mCurrentPagePosition = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (ViewPager.SCROLL_STATE_IDLE == state) {
                if (mIsChanged) {
                    mIsChanged = false;
                    CalendarPager.this.setCurrentItem(mCurrentPagePosition, false);
                }
            }
        }
    }

    private class DateSelectListener implements DateView.OnDateSelectListener{

        @Override
        public void onSelected(int year, int month, int day) {
            mSelectYear=year;
            mSelectMonth=month;
            mSelectDay=day;
            for(DateView view:mList){
                view.setSelectDate(mSelectYear,mSelectMonth,mSelectDay);
            }
            if(mSelectListener !=null){
                mSelectListener.onSelected(mSelectYear,mSelectMonth,mSelectDay);
            }
        }
    }
    public List getPager(){
        return mList;
    }
}
