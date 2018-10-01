package com.atguigu.guoqingjie_app.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinpengfei on 2016/10/24.
 * 微信:18091383534
 * Function :左侧带单的Fragment
 */

public class LeftMenuFragment extends BaseFragment {

    private ListView listview;

    private List<String> list;

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.left_menu, null);
        listview = (ListView) view.findViewById(R.id.listview);
        listview.setAdapter(new MyAdapter());
        notifyAll();

        return view;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_menu, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(list.get(position));

            return convertView;
        }
    }

    @Override
    public void initData() {
        super.initData();

        list = new ArrayList<>();
        list.add("首页");
        list.add("资讯");
        list.add("美女");
        list.add("更多");
        list.add("反馈");
    }
}
