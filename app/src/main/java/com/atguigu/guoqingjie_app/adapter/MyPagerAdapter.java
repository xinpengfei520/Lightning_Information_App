package com.atguigu.guoqingjie_app.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by xpf on 2016/11/20 :)
 * Wechat:18091383534
 * Function:创建一个继承于PagerAdapter的适配器，至少需要实现4方法
 */

public class MyPagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> imageViews;

    public MyPagerAdapter(ArrayList<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    // 返回总条数
    @Override
    public int getCount() {
        return Integer.MAX_VALUE; // imageViews.size()
    }


    /**
     * 作用类似于getView();
     * 把页面添加到ViewPager中
     * 并且返回当前页面的相关的特性
     * container:容器就是ViewPager自身
     * position：实例化当前页面的位置
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // 1.添加到viewPager中
        ImageView imageView = imageViews.get(position % imageViews.size());
        container.addView(imageView);

        // 2.并且返回当前页面的相关的特性
        return imageView;
    }

    /**
     * view和object比较是否是同一个View
     * 如果相同返回true
     * 不相同返回false
     * object:其实就是instantiateItem()方法返回的值
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object; // 此句和上面的相同
    }

    /**
     * 销毁container:ViewPager
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
