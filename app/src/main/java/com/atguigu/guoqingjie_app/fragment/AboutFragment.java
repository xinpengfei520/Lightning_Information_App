package com.atguigu.guoqingjie_app.fragment;

import android.util.Log;
import android.view.View;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.base.BaseFragment;

/**
 * Created by xinpengfei on 2016/10/4.
 * <p>
 * Function :
 */

public class AboutFragment extends BaseFragment {

    @Override
    public View initView() {

        Log.e("TAG", "关于UI创建了");
        View view = View.inflate(context, R.layout.activity_about, null);

        return view;

    }
}
