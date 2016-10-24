package com.atguigu.guoqingjie_app.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.activity.MainActivity;
import com.atguigu.guoqingjie_app.base.BaseFragment;
import com.atguigu.guoqingjie_app.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by xinpengfei on 2016/10/24.
 * 微信:18091383534
 * Function :主页面的Fragment
 */

public class ContentFragment extends BaseFragment {


    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @Override
    public View initView() {
        LogUtil.e("主页面的视图被初始化了...");
        View view = View.inflate(context, R.layout.fragment_content, null);

        //把View注入到xUtils中
        x.view().inject(ContentFragment.this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("主页面的数据被初始化了...");

        rg_main.check(R.id.rb_news);

        //设置viewPager的适配器
//        viewPager.setAdapter(new MyPagerAdapter());

        //监听RadioGroup状态的变化
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //设置监听页面被选中
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //默认侧滑菜单不可以滑动
        setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    private void setTouchModeAbove(int touchmodeNone) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeNone);
    }

//    class MyPagerAdapter extends PagerAdapter{
//
//        @Override
//        public int getCount() {
//            return
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//
////            BasePager basePager = basePagers.get(position);
////            View rootView = basePager.rootView;//各个页面
//
////            container.addView(rootView);
////
////            return rootView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //设置默认不可以滑动
            setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            switch (checkedId) {
                case R.id.rb_news ://新闻中心
                    //可以侧滑
                    setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    viewPager.setCurrentItem(0, false);
                    break;
                case R.id.rb_beauty ://美图中心
                    viewPager.setCurrentItem(1, false);
                    break;
                case R.id.rb_video ://视频页面
                    viewPager.setCurrentItem(2, false);
                    break;
                case R.id.rb_about ://关于
                    viewPager.setCurrentItem(3, false);
                    break;
            }
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //当选中某个页面的时候，才调用initData
//            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
