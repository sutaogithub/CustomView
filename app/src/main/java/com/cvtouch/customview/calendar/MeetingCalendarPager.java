package com.cvtouch.customview.calendar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 会议翻页日历
 * @date 2016/9/19
 */
public class MeetingCalendarPager extends CalendarPager {

    public GetMeetingData mProvider;
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

    /**
     * 拉取会议数据的回调
     */
    public interface GetMeetingData{
        int[] onGetMeetingData(int year,int month);
    }
    public void setGetMeetingData(GetMeetingData provider){
        mProvider =provider;
        updateDate(getCurrentItem());
    }
    @Override
    protected void updateDate(int position) {
        if(mProvider!=null){
            if(position<1||position>3){
                return;
            }
            //绘制上一个月,当月,下一个月
            if(mNowMonth==0){
                mList.get(position-1).setDate(mNowYear-1, 11);
                ((MeetingDateView)mList.get(position-1)).setMeetingData(mProvider.onGetMeetingData(mNowYear-1,11));
            }else {
                mList.get(position-1).setDate(mNowYear, mNowMonth-1);
                ((MeetingDateView)mList.get(position-1)).setMeetingData(mProvider.onGetMeetingData(mNowYear, mNowMonth-1));
            }
            mList.get(position).setDate(mNowYear, mNowMonth);
            ((MeetingDateView)mList.get(position)).setMeetingData(mProvider.onGetMeetingData(mNowYear, mNowMonth));
            if(mNowMonth==11) {
                mList.get(position + 1).setDate(mNowYear+1, 0);
                ((MeetingDateView)mList.get(position + 1)).setMeetingData(mProvider.onGetMeetingData(mNowYear+1, 0));
            }else {
                mList.get(position + 1).setDate(mNowYear, mNowMonth+1);
                ((MeetingDateView)mList.get(position + 1)).setMeetingData(mProvider.onGetMeetingData(mNowYear, mNowMonth+1));
            }
            if(position==LAST_PAGE_INDEX){
                if(mNowMonth==11) {
                    mList.get(FIST_PAGE_INDEX).setDate(mNowYear+1, 0);
                    ((MeetingDateView)mList.get(FIST_PAGE_INDEX)).setMeetingData(mProvider.onGetMeetingData(mNowYear+1,0));
                }else {
                    mList.get(FIST_PAGE_INDEX).setDate(mNowYear, mNowMonth+1);
                    ((MeetingDateView)mList.get(FIST_PAGE_INDEX)).setMeetingData(mProvider.onGetMeetingData(mNowYear, mNowMonth+1));
                }
            }
            if(position==FIST_PAGE_INDEX){
                if(mNowMonth==0){
                    mList.get(LAST_PAGE_INDEX).setDate(mNowYear-1, 11);
                    ((MeetingDateView)mList.get(LAST_PAGE_INDEX)).setMeetingData(mProvider.onGetMeetingData(mNowYear-1, 11));
                }else {
                    mList.get(LAST_PAGE_INDEX).setDate(mNowYear, mNowMonth-1);
                    ((MeetingDateView)mList.get(LAST_PAGE_INDEX)).setMeetingData(mProvider.onGetMeetingData(mNowYear,mNowMonth-1));
                }
            }
        }else {
            super.updateDate(position);
        }

    }
}
