package com.example.viewpagertext;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ViewPager实现画廊效果
 *
 * @author zhaoxinghai 2013/10/16
 */
public class MianActivity extends Activity {

    private static int TOTAL_COUNT = 5;
    private LinearLayout viewPagerContainer;
    private ViewPager viewPager;
    private TextView tvTitle;
    private int[] imageIDs;
    private ImageView[] mImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item1);

        imageIDs = new int[]{R.drawable.beautiful_001,
                R.drawable.beautiful_002, R.drawable.beautiful_003,
                R.drawable.beautiful_004, R.drawable.beautiful_005,
                R.drawable.beautiful_006, R.drawable.beautiful_007,
                R.drawable.beautiful_008/*, R.drawable.beautiful_009,
                R.drawable.beautiful_010, R.drawable.beautiful_011,
    			R.drawable.beautiful_012, R.drawable.beautiful_013*/};


        // 将图片装载到数组中
        mImageViews = new ImageView[imageIDs.length];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imageIDs[i]);
        }
        tvTitle = (TextView) findViewById(R.id.title);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerContainer = (LinearLayout) findViewById(R.id.pager_layout);

//        int height=ScreenUtil.getScreenHeight(this);
        int width = ScreenUtil.getScreenWidth(this);
        int left = viewPagerContainer.getLeft();
//        int right=viewPagerContainer.getRight();
        viewPager.setPageMargin(20);
        int pageWidth = (width - viewPager.getPageMargin() * 3 - left * 2) / 3;
        LinearLayout.LayoutParams viewPagerPara = new LinearLayout.LayoutParams(pageWidth, pageWidth * 2);
        viewPager.setLayoutParams(viewPagerPara);
        viewPager.setAdapter(new MyPagerAdapter());
        // to cache all page, or we will see the right item delayed
        viewPager.setOffscreenPageLimit(TOTAL_COUNT);


//        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        viewPagerContainer.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return viewPager.dispatchTouchEvent(event);
            }
        });
    }

    /**
     * this is a example fragment, just a imageview, u can replace it with your needs
     *
     * @author Trinea 2013-04-03
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//        	 return Integer.MAX_VALUE;  //循环滑动
            return imageIDs.length;
//            return TOTAL_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("pos:" + position);
            ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];

/*        	System.out.println("pos:" + position);
            ImageView imageView = new ImageView(MianActivity.this);
            imageView.setImageResource(R.drawable.tmp_image1 + position);

            ImageView imageView1 = new ImageView(MianActivity.this);
            imageView1.setImageResource(R.drawable.tmp_image2 + position);

            ImageView imageView2 = new ImageView(MianActivity.this);
            imageView2.setImageResource(R.drawable.tmp_image3 + position);

            ImageView imageView3 = new ImageView(MianActivity.this);
            imageView3.setImageResource(R.drawable.tmp_image4 + position);

            ImageView imageView4 = new ImageView(MianActivity.this);
            imageView4.setImageResource(R.drawable.tmp_image5 + position);

            ((ViewPager)container).addView(imageView, position);
            ((ViewPager)container).addView(imageView1, position);
            ((ViewPager)container).addView(imageView2, position);
            ((ViewPager)container).addView(imageView3, position);
            ((ViewPager)container).addView(imageView4, position);
            return imageView;*/

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            tvTitle.setText("position = " + position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 刷新布局
            if (viewPagerContainer != null) {
                viewPagerContainer.invalidate();
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}