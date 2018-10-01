package com.atguigu.guoqingjie_app.listener;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.atguigu.guoqingjie_app.fragment.NewsFragment.imageDescriptions;

/**
 * Created by xpf on 2016/11/20 :)
 * Wechat:18091383534
 * Function:ViewPager页面变化的监听器
 */

public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private LinearLayout ll_group_point;
    private int lastIndex;
    private TextView tv_msg;
    private ArrayList<ImageView> imageViews;
    private Handler handler;

    public MyOnPageChangeListener(Handler handler, ArrayList<ImageView> imageViews, TextView tv_msg, int lastIndex, LinearLayout ll_group_point) {
        this.handler = handler;
        this.imageViews = imageViews;
        this.tv_msg = tv_msg;
        this.lastIndex = lastIndex;
        this.ll_group_point = ll_group_point;
    }

    /**
     * 当页面滚动了的时候回调这个方法(必须掌握)
     *
     * @param position             滚动页面的位置
     * @param positionOffset       当前滑动页面的百分比，例如滑动一半是0.5
     * @param positionOffsetPixels 当前页面滑动的像素
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.e("TAG", "position=" + position + "," + positionOffset + "," + positionOffsetPixels);
    }

    /**
     * 当页面改变了的时候回调这个方法
     *
     * @param position 当前被选中页面的位置
     */
    @Override
    public void onPageSelected(int position) {

        int realPosition = position % imageViews.size();
        String text = imageDescriptions[realPosition];
        tv_msg.setText(text);
        // 1.把之前的设置为默认
        ll_group_point.getChildAt(lastIndex).setEnabled(false);
        // 2.把当前的位置对应的页面设置为高亮
        ll_group_point.getChildAt(realPosition).setEnabled(true);
        // ll_group_point.getChildAt(position) --->ImageView
        lastIndex = realPosition;
    }

    /**
     * 当页面状态发生变化的时候回调这个方法
     * 静止到滑动
     * 滑动到静止
     */
    boolean isDragging = false;

    @Override
    public void onPageScrollStateChanged(int state) {
        // 正在拖动中...
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            Log.e("TAG", "正在拖动中...");
            // 移除消息
            handler.removeCallbacksAndMessages(null);
            isDragging = true;

            // 静止状态中...
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            Log.e("TAG", "静止状态中...");

            handler.removeCallbacksAndMessages(null);//这个一定要加上
            handler.sendEmptyMessageDelayed(0, 3000);

            // 正在滑动中...
        } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
            Log.e("TAG", "正在滑动中...");
            isDragging = false;
            handler.removeMessages(0); // 这个一定要加上
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }
}
