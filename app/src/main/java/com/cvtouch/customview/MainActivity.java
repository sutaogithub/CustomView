package com.cvtouch.customview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cvtouch.customview.calendar.CalendarView;
import com.cvtouch.customview.calendar.DateView;
import com.cvtouch.customview.calendar.MeetingCalendarPager;
import com.cvtouch.customview.calendar.MeetingCalendarView;
import com.cvtouch.customview.circleimageview.CircularImageView;
import com.cvtouch.customview.hanzitopinyin.HanziToPinyin;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MeetingCalendarView calendarView= (MeetingCalendarView) findViewById(R.id.calendar);
//        calendarView.setOnDateSelectListener(new DateView.OnDateSelectListener() {
//            @Override
//            public void onSelected(int year, int month, int day) {
//                Log.d("calendar,",year+"  "+month+"  "+day);
//            }
//        });
    }
}
