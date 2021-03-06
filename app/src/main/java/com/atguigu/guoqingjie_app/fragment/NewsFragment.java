package com.atguigu.guoqingjie_app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.activity.NewsDetailActivity;
import com.atguigu.guoqingjie_app.adapter.MyPagerAdapter;
import com.atguigu.guoqingjie_app.adapter.NewsDetailsAdapter;
import com.atguigu.guoqingjie_app.base.BaseFragment;
import com.atguigu.guoqingjie_app.bean.Data;
import com.atguigu.guoqingjie_app.bean.Result;
import com.atguigu.guoqingjie_app.bean.Root;
import com.atguigu.guoqingjie_app.listener.MyOnPageChangeListener;
import com.atguigu.guoqingjie_app.utils.CacheUtils;
import com.atguigu.guoqingjie_app.utils.Constants;
import com.atguigu.guoqingjie_app.utils.DensityUtil;
import com.atguigu.guoqingjie_app.utils.LogUtil;
import com.atguigu.refreshlistview.RefreshListView;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinpengfei on 2016/10/4.
 * Function :NewsFragment新闻显示页面
 */

public class NewsFragment extends BaseFragment {

    private static final int MESSAGE_RESULT_OK = 1;
    private static final int MESSAGE_RESULT_ERROR = 2;
    private static final int MESSAGE_RESULT_NO = 3;
    private static final int IMAGVIEW = 4;
    private ViewPager viewpager;
    private RefreshListView listview;
    private TextView tv_msg;
    private LinearLayout ll_group_point;
    private LinearLayout ll_loading;
    private LinearLayout ll_fail;
    private MyPagerAdapter adapter;
    private NewsDetailsAdapter news_adapter; // 装新闻数据的适配器
    private List<Data> list;          // 用于存放新闻类对象的集合
    public int lastIndex;             // 上一次被高亮显示的位置
    private String topUrl;            // 头条新闻(top)的Url
    private String moreUrl;           // 加载更多的url
    private boolean isLoadMore = false; // 是否加载更多
    private boolean isActivityIsDestroy = false; // 当前Activity是否销毁
    private ArrayList<ImageView> imageViews; // 图片的集合
    // 图片的ID资源
    private int[] ids = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
    // 图片标题集合
    public static final String[] imageDescriptions = {
            "游客挤爆西安大雁塔音乐喷泉",
            "北京天坛公园迎来游园高峰",
            "南京市民抠出葵花表情宝典",
            "贵州乌公侗寨欢庆烧鱼节",
            "男子为疯狂健身注射燃油"};

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case MESSAGE_RESULT_OK:
                    // 显示列表
                    news_adapter = new NewsDetailsAdapter(context, list);
                    listview.setAdapter(news_adapter);
                    break;

                case MESSAGE_RESULT_ERROR:
                    Toast.makeText(context, "资源找不到", Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_RESULT_NO:
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                    ll_fail.setVisibility(View.VISIBLE);
                    break;
            }
            int item = viewpager.getCurrentItem() + 1;
            viewpager.setCurrentItem(item);
            if (!isActivityIsDestroy) {
                handler.sendEmptyMessageDelayed(IMAGVIEW, 3000);
            }
            ll_loading.setVisibility(View.GONE); // 移除加载视图
        }
    };


    /**
     * 初始化视图
     */
    @Override
    public View initView() {
        Log.e("TAG", "本地新闻UI创建了");
        View view = View.inflate(context, R.layout.activity_newsfragment, null);

        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.viewpager, null);

        viewpager = (ViewPager) view2.findViewById(R.id.viewpager);
        listview = (RefreshListView) view.findViewById(R.id.listview);
        tv_msg = (TextView) view2.findViewById(R.id.tv_msg);
        ll_group_point = (LinearLayout) view2.findViewById(R.id.ll_group_point);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        ll_fail = (LinearLayout) view.findViewById(R.id.ll_fail);

        // 联网操作的过程:第1步：主线程：显示提示视图
        ll_loading.setVisibility(View.VISIBLE);
        listview.addHeaderView(view2);

        initViewPager();
        initNewsData();

        // 设置点击某条新闻的的点击事件
        listview.setOnItemClickListener(new MyOnItemClickListener());

        // 设置刷新的监听
        listview.setOnRefreshListener(new MyOnRefreshListener());

        return view;
    }

    /**
     * 刷新页面的监听器
     */
    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            initNewsData();
        }

        @Override
        public void onLoadeMore() {

            if (TextUtils.isEmpty(moreUrl)) {
                // 没有更多
                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                listview.onFinishRefrsh(false);
            } else {
                // 加载更多
//                SystemClock.sleep(2000); // 增强加载效果
                getMoreDataFromNet();
            }
        }
    }

    /**
     * 加载更多的方法
     */
    private void getMoreDataFromNet() {

        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("TabDetailPager加载更多联网请求成功==" + result);
                isLoadMore = true;
                processData(result);
                listview.onFinishRefrsh(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("TabDetailPager加载更多联网请求失败==" + ex.getMessage());
                listview.onFinishRefrsh(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    /**
     * 点击事件的监听器
     */
    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int realPosition = position - 2;

//            先把保存的取出来，如果没有保存过就保存
//            String read_array_id = CacheUtils.getString(context, READ_ARRAY_ID);//"" //111

            String url = list.get(realPosition).getUrl();
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }

    /**
     * 初始化ListView中的新闻数据
     */
    private void initNewsData() {

        topUrl = Constants.BASE_URL + "top" + Constants.KEY;

        RequestParams params = new RequestParams(topUrl);
        params.setConnectTimeout(5000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("onSuccess==" + result);
                // 把数据缓存到本地
                CacheUtils.putString(context, topUrl, result);
                processData(result);
                listview.onFinishRefrsh(false);
                // 发送消息：
                handler.sendEmptyMessage(MESSAGE_RESULT_OK);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
                // 联网失败--> 发送消息
                handler.sendEmptyMessage(MESSAGE_RESULT_NO);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });

    }

    /**
     * 联网请求数据
     */
    private void processData(String result) {

        moreUrl = Constants.BASE_URL + Constants.GUONEI + Constants.KEY;

        if (!isLoadMore) {
            list = new ArrayList<>();
            // 使用GSON解析，还原为java对象构成的集合
            Root root = new Gson().fromJson(result, Root.class);
            Result result1 = root.getResult();
            list = result1.getData();

        } else {
            topUrl = moreUrl;
            initData();
            // 加载更多
            isLoadMore = false;
            // 使用GSON解析，还原为java对象构成的集合
            Root root = new Gson().fromJson(result, Root.class);
            Result result1 = root.getResult();
            // 把得到的更多数据加载到原来的集合中
            list.addAll(result1.getData());
            adapter.notifyDataSetChanged();
        }
    }

    private void initViewPager() {

        // 2.准备数据
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {

            ImageView imageView = new ImageView(context);
            imageView.setTag(i); // 把位置设为tag
            imageView.setBackgroundResource(ids[i]);
            // 给imageView设置点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (int) v.getTag(); // 把tag转换为位置
                    String text = imageDescriptions[position]; // 获取图片标题并显示
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            });

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        handler.removeCallbacksAndMessages(null);
                        Log.e("TAG", "ACTION_DOWN");

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessageDelayed(IMAGVIEW, 3000);
                        Log.e("TAG", "ACTION_UP");
                    }
                    return false;
                }
            });

            // 加入到集合中
            imageViews.add(imageView);
            initPoint(i); // 初始化viewpager中的点

            adapter = new MyPagerAdapter(imageViews); // 设置适配器
            viewpager.setAdapter(adapter);

            // 设置文本为第0个
            tv_msg.setText(imageDescriptions[0]);

            // 设置viewPager页面改变的监听
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener(handler, imageViews, tv_msg, lastIndex, ll_group_point));

            // 保证Integer.MAX_VALUE/2是imageViews.size()的整数倍
            int item = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % imageViews.size();

            // 设置中间值
            viewpager.setCurrentItem(item);

            // 开始循环滑动
            handler.sendEmptyMessageDelayed(IMAGVIEW, 3000);
        }
    }

    /**
     * 初始化并创建viewPager视图中的点
     */
    private void initPoint(int i) {
        // 有多少个页面就创建多少个点
        ImageView point = new ImageView(context);
        int widthDp = DensityUtil.dip2px(context, 8);
        Log.e("TAG", widthDp + "");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDp, widthDp);
        if (i != 0) {
            params.leftMargin = widthDp;
        }

        point.setLayoutParams(params);
        point.setBackgroundResource(R.drawable.point_selector);

        // 默认第一个点告诉
        if (i == 0) {
            point.setEnabled(true);
        } else {
            point.setEnabled(false);
        }

        // 添加指示点到线性布局
        ll_group_point.addView(point);
    }

}
