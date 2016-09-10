package com.cvtouch.customview;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2016/9/9
 */
public class TimeSectionSelector extends RelativeLayout{
    private  LinearLayout container;
    private ScrollView scroller;
    private SelectView mSelectView;
    private Context mContext;
    public TimeSectionSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        View view=LayoutInflater.from(context).inflate(R.layout.selectview_layout, this, true);
        container= (LinearLayout) view.findViewById(R.id.time_container);
        scroller= (ScrollView) view.findViewById(R.id.scroll_list);
        mSelectView= (SelectView) view.findViewById(R.id.select_view);
        mSelectView.setMoveListener(new SelectView.onTouchMoveListener() {
            @Override
            public void upMove() {
                scroller.scrollBy(0,-SystemUtils.dip2px(mContext,50));
            }

            @Override
            public void downMove() {
                scroller.scrollBy(0,SystemUtils.dip2px(mContext,50));
            }
        });
        addTimeItem(context);

    }

    private void addTimeItem(Context context) {
        for(int i=0;i<24;i++){
            TimeView time= (TimeView) LayoutInflater.from(context).inflate(R.layout.time_item, container, false);
            TimeView halfTime= (TimeView) LayoutInflater.from(context).inflate(R.layout.time_item, container, false);
            time.setText(i+"时");
            time.setIsFullLine(true);
            halfTime.setIsFullLine(false);
            container.addView(time);
            container.addView(halfTime);
        }
    }

    public TimeSectionSelector(Context context) {
        super(context,null);
    }
}
