package com.atguigu.guoqingjie_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.utils.CacheUtils;
import com.atguigu.guoqingjie_app.utils.DensityUtil;
import com.atguigu.guoqingjie_app.utils.LogUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    public static final String START_MAIN = "start_main";

    private ViewPager viewpager;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;
    private TextView tv_magic;
    private Button btn_start_main;

    private ArrayList<ImageView> imageViews;

    /**
     * 两点间的间距
     */
    private int leftMarg;
    private int widthDpi;

    /**
     * 设置适配器--> 准备数据
     */
    private int ids[] = new int[]{
            R.drawable.guide2, R.drawable.guide3, R.drawable.guide5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        tv_magic = (TextView) findViewById(R.id.tv_magic);

        initData();
    }

    private void initData() {

        widthDpi = DensityUtil.dip2px(this, 10);
        LogUtil.e(widthDpi + "-----------------");

        imageViews = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            //把图片添加到集合中
            imageViews.add(imageView);

            //添加灰色的点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.point_gray);

            //设置点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDpi, widthDpi);
            if (i != 0) {
                //设置间距
                params.leftMargin = widthDpi;
            }
            point.setLayoutParams(params);

            ll_point_group.addView(point);

        }

        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());

        /**
         * 求间距
         * 构造方法--> 测量(measure--onMeasure)--> layout-->onLayout-->draw-->onDraw
         */
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //监听viewPager页面滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置点击事件
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录进入引导页面
                CacheUtils.putBoolean(GuideActivity.this, START_MAIN, true);
                //跳转到主页面
                startActivity(new Intent(GuideActivity.this, MainActivity.class));

                finish();//关闭引导页面
            }
        });
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * @param position             当前滑动页面的下标位置
         * @param positionOffset       滑动了页面的百分比
         * @param positionOffsetPixels 滑动了页面多少像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //红点移动的距离 = ViewPager页面的百分比* 间距

//            float leftMargin = positionOffset * leftMarg;
            //坐标 = 起始位置 + 红点移动的距离;

            float leftMargin = (position + positionOffset) * leftMarg;
            RelativeLayout.LayoutParams paramgs = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            //距离左边的距离
            paramgs.leftMargin = (int) leftMargin;
            iv_red_point.setLayoutParams(paramgs);

            LogUtil.e("position==" + position + ",positionOffset==" + positionOffset + ",positionOffsetPixels==" + positionOffsetPixels);


        }

        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {//滑动到最后一个页面显示按钮
                //让按钮显示
                btn_start_main.setVisibility(View.VISIBLE);
                tv_magic.setVisibility(View.VISIBLE);
            } else {
                //按钮隐藏
                btn_start_main.setVisibility(View.GONE);
                tv_magic.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {

            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            //间距 = 第一个点距离左边距离 - 第0个点距离左边距离
            leftMarg = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * @param container viewPager容器
         * @param position  创建视图对应的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        /**
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
