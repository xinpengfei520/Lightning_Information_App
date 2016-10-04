package com.atguigu.guoqingjie_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fragment.AboutFragment;
import fragment.BaseFragment;
import fragment.BeautyFragment;
import fragment.NewsFragment;
import fragment.VideoFragment;

public class MainActivity extends FragmentActivity {

    private TextView tv_title;
    private RadioGroup rg_main;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = (TextView) findViewById(R.id.tv_title);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        initFragment();

        //设置RadioGroup状态改变的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        rg_main.check(R.id.rb_news);//打开软件默认在news页面

    }

    /**
     * 点击搜索按钮的回调
     * @param v
     */
    public void iv_search(View v) {

        startActivity(new Intent(this,SearchActivity.class));
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

}
