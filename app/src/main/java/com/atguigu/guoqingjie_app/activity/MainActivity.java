package com.atguigu.guoqingjie_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.base.BaseFragment;
import com.atguigu.guoqingjie_app.fragment.AboutFragment;
import com.atguigu.guoqingjie_app.fragment.BeautyFragment;
import com.atguigu.guoqingjie_app.fragment.ContentFragment;
import com.atguigu.guoqingjie_app.fragment.LeftMenuFragment;
import com.atguigu.guoqingjie_app.fragment.NewsFragment;
import com.atguigu.guoqingjie_app.fragment.VideoFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;

public class MainActivity extends SlidingFragmentActivity {

    public static final String CONTENT_TAG = "content_tag";
    public static final String LEFTMEMU_TAG = "leftmemu_tag";
    private TextView tv_title;
    private RadioGroup rg_main;
    /**
     * 判断按键是否被按下
     */
    private boolean flag = true;
    /**
     * 各个Fragemnt页面的集合
     */
    private ArrayList<BaseFragment> fragments;

    /**
     * 列表中对应的Fragment的位置
     */
    private int position;

    /**
     * 当前显示的Fragment
     */
    private Fragment content;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    flag = true;
                    Log.e("TAG", "handleMessage()");
                    break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);

        //3.获取slidingMenu对象
        SlidingMenu slidingMenu = getSlidingMenu();

        //4.设置支持滑动的模式：左右可滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //5.设置页面模式：左侧菜单+主页面
        slidingMenu.setMode(SlidingMenu.LEFT);

        //6.设置占主页面的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(200));

        tv_title = (TextView) findViewById(R.id.tv_title);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        initFragment();

        //设置RadioGroup状态改变的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        rg_main.check(R.id.rb_news);//打开软件默认在news页面

    }

    /**
     * 点击左上角菜单按钮的回调
     *
     * @param v
     */
    public void iv_menu(View v) {

    }

    /**
     * 点击搜索按钮的回调
     *
     * @param v
     */
    public void iv_search(View v) {

        startActivity(new Intent(this, SearchActivity.class));
    }

    /**
     * 获取对应位置的Fragment
     *
     * @param position
     * @return
     */
    private Fragment getFragment(int position) {
        return fragments.get(position);
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {

//        //1.开启事务
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        //2.添加左侧菜单页面的Fragment
//        transaction.replace(R.id.fl_leftmemu,new LeftMenuFragment(), LEFTMEMU_TAG);
//        //3.添加主页面的Fragment
//        transaction.replace(R.id.fl_main_content,new ContentFragment(), CONTENT_TAG);
//
//        //提交事务，不要忘了！
//        transaction.commit();

        fragments = new ArrayList<>();
        fragments.add(new NewsFragment());//新闻
        fragments.add(new BeautyFragment());//美图
        fragments.add(new VideoFragment());//视频
        fragments.add(new AboutFragment());//关于
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.rb_news:
                    position = 0;
                    tv_title.setText("新 闻");
                    break;
                case R.id.rb_beauty:
                    position = 1;
                    tv_title.setText("美 图");
                    break;
                case R.id.rb_video:
                    position = 2;
                    tv_title.setText("视 频");
                    break;
                case R.id.rb_about:
                    position = 3;
                    tv_title.setText("关 于");
                    break;
            }
            Fragment toFragment = getFragment(position);
            switchFragment(content, toFragment);
        }
    }

    /**
     * @param fromFragment : 在点击时正在显示的Fragment
     * @param toFragment   : 在点击后马上要显示的Fragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {

        if (toFragment != null && toFragment != content) {
            /**
             * 得到Fragment事务对象(必须要让当前Activity继承FragmentActivity)
             */
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (!toFragment.isAdded()) {//如果要显示的Fragment没有添加就添加
                //隐藏之前显示的fromFragment
                if (fromFragment != null) {//隐藏之前先判空
                    transaction.hide(fromFragment);
                }
                //添加toFragment到容器，并提交
                transaction.add(R.id.fl_main_container, toFragment).commit();
            } else {//如果添加过了，就隐藏当前的，显示下一个
                //隐藏之前显示的fromFragment
                if (fromFragment != null) {
                    transaction.hide(fromFragment);
                }
                //显示toFragment
                transaction.show(toFragment).commit();
            }
            content = toFragment;//当前显示的Fragment
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (flag) {
            flag = false;
            Toast.makeText(MainActivity.this, "再点击一次返回键退出", Toast.LENGTH_SHORT).show();
            //发送一个延迟消息

            handler.sendEmptyMessageDelayed(1, 2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        Log.e("TAG", "onDestroy()");
        //方式一：
        handler.removeMessages(1);
        //方式二：移除所有消息和回调
//      handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }

    /**
     * 得到左侧菜单
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMEMU_TAG);
    }

    /**
     * 得到ContentFragment
     * @return
     */
    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONTENT_TAG);
    }

}
