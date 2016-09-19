package com.cvtouch.customview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cvtouch.customview.circleimageview.CircularImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        CircularImageView iv= (CircularImageView) findViewById(R.id.iv);
//        ArrayList<Bitmap> bitmaps=new ArrayList<>();
//        for(int i=0;i<3;i++){
//            bitmaps.add(BitmapFactory.decodeResource(getResources(),R.drawable.touxiang));
//        }
//        iv.setImageBitmaps(bitmaps);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
