package com.atguigu.guoqingjie_app.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.atguigu.guoqingjie_app.base.BaseFragment;

/**
 * Created by xinpengfei on 2016/10/24.
 * 微信:18091383534
 * Function :左侧带单的Fragment
 */

public class LeftMenuFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View initView() {

        textView = new TextView(context);
        textView.setText("左侧菜单被初始化了");
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
