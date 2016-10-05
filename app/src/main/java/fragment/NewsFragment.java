package fragment;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.guoqingjie_app.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bean.Data;
import bean.ImageLoader;
import bean.Result;
import bean.Root;
import utils.DensityUtil;

/**
 * Created by xinpengfei on 2016/10/4.
 * <p>
 * Function :NewsFragment新闻显示页面
 */

public class NewsFragment extends BaseFragment {

    private static final int MESSAGE_RESULT_OK = 1;
    private static final int MESSAGE_RESULT_ERROR = 2;
    private static final int MESSAGE_RESULT_NO = 3;
    private ViewPager viewpager;
    private ListView lv_fg_news;
    private TextView tv_msg;
    private LinearLayout ll_group_point;
    private LinearLayout ll_loading;
    private MyPagerAdapter adapter;
    private NewsAdapter news_adapter;
    private List<Data> list;
    /**
     * 上一次被高亮显示的位置
     */
    private int lastIndex;
    /**
     * 图片的集合 ：可以是ImageView和Fragment，View
     */
    private ArrayList<ImageView> imageViews;
    /**
     * 图片的ID资源
     */
    private int[] ids = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

    /**
     * 图片标题集合
     */
    private final String[] imageDescriptions = {
            "尚硅谷波河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };
    /**
     * 当前Activity是否销毁
     */
    private boolean isActivityIsDestroy = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            int item = viewpager.getCurrentItem() + 1;
            viewpager.setCurrentItem(item);

            if (!isActivityIsDestroy) {
                handler.sendEmptyMessageDelayed(0, 3000);
            }

        }
    };

    private Handler handler2 = new Handler() {
        //处理消息
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_RESULT_OK:
                    ll_loading.setVisibility(View.GONE);//移除加载视图

                    //显示列表
                    lv_fg_news.setAdapter(news_adapter);
                    break;

                case MESSAGE_RESULT_ERROR:
                    Toast.makeText(context, "资源找不到", Toast.LENGTH_SHORT).show();
                    ll_loading.setVisibility(View.GONE);//移除加载视图
                    break;

                case MESSAGE_RESULT_NO:
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                    ll_loading.setVisibility(View.GONE);//移除加载视图
                    break;
            }
        }
    };

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public View initView() {
        Log.e("TAG", "本地视频UI创建了");
        View view = View.inflate(context, R.layout.activity_newsfragment, null);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        lv_fg_news = (ListView) view.findViewById(R.id.lv_fg_news);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        ll_group_point = (LinearLayout) view.findViewById(R.id.ll_group_point);
        lv_fg_news = (ListView) view.findViewById(R.id.lv_fg_news);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);

        //联网操作的过程:第1步：主线程：显示提示视图
        ll_loading.setVisibility(View.VISIBLE);
        news_adapter = new NewsAdapter();

        initViewPager();
        initNewsData();
        lv_fg_news.setAdapter(news_adapter);
//        news_adapter.notifyDataSetChanged();

        return view;

    }

    /**
     * 初始化ListView中的新闻数据
     */
    private void initNewsData() {

//        list = new ArrayList<>();

        //联网操作的过程：第2步：分线程：联网下载数据
        new Thread() {
            public void run() {
                String path = "http://v.juhe.cn/toutiao/index?type=top&key=2a0f1ffbb5dba8795809d1da60a2baa6";
                HttpURLConnection conn = null;
                ByteArrayOutputStream baos = null;
                InputStream is = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");

                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    Log.e("TAG", responseCode + "");
                    if (responseCode == 200) {
                        is = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                        }
                        //从服务器端返回的json数组的字符串
                        String result = baos.toString();
                        Log.e("TAGA", result);
                        //使用GSON解析，还原为java对象构成的集合
                        Gson gson = new Gson();
                        //初始化list
//                        list = gson.fromJson(result, new TypeToken<List<Root>>() {
//                        }.getType());
                        Root root = gson.fromJson(result,Root.class);
                        Result result1 = root.getResult();
                        list = result1.getData();

                        //发送消息：
                        handler2.sendEmptyMessage(MESSAGE_RESULT_OK);
                    } else {
                        //发送消息：
                        handler2.sendEmptyMessage(MESSAGE_RESULT_ERROR);

                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    //发送消息
                    handler2.sendEmptyMessage(MESSAGE_RESULT_NO);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }


        }.start();
    }

    private void initViewPager() {

        //2.准备数据
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {

            ImageView imageView = new ImageView(context);
            imageView.setTag(i);//把位置设为tag
            imageView.setBackgroundResource(ids[i]);
            //给imageView设置点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (int) v.getTag();//把tag转换为位置
                    String text = imageDescriptions[position];//获取图片标题并显示
                    Toast.makeText(context, "text = " + text, Toast.LENGTH_SHORT).show();
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
                        handler.sendEmptyMessageDelayed(0, 3000);
                        Log.e("TAG", "ACTION_UP");
                    }

                    return false;
                }
            });

            //加入到集合中
            imageViews.add(imageView);
            initPoint(i);//初始化viewpager中的点

            adapter = new MyPagerAdapter();//设置适配器
            viewpager.setAdapter(adapter);

            //设置文本为第0个
            tv_msg.setText(imageDescriptions[0]);

            //设置viewPager页面改变的监听
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

            //保证Integer.MAX_VALUE/2是imageViews.size()的整数倍
            int item = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % imageViews.size();

            //设置中间值
            viewpager.setCurrentItem(item);

            //开始循环滑动
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    /**
     * 初始化并创建视图中的点
     *
     * @param i
     */
    private void initPoint(int i) {
        //有多少个页面就创建多少个点
        ImageView point = new ImageView(context);
        int widthDp = DensityUtil.dip2px(context, 8);
        Log.e("TAG", widthDp + "");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDp, widthDp);
        if (i != 0) {
            params.leftMargin = widthDp;
        }

        point.setLayoutParams(params);
        point.setBackgroundResource(R.drawable.point_selector);

        //默认第一个点告诉
        if (i == 0) {
            point.setEnabled(true);
        } else {
            point.setEnabled(false);
        }

        //添加指示点到线性布局
        ll_group_point.addView(point);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调这个方法(必须掌握)
         *
         * @param position             滚动页面的位置
         * @param positionOffset       当前滑动页面的百分比，例如滑动一半是0.5
         * @param positionOffsetPixels 当前页面滑动的像素
         */

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            Log.e("TAG", "position=" + position + "," + positionOffset + "," + positionOffsetPixels);
        }

        /**
         * 当页面改变了的时候回调这个方法
         *
         * @param position 当前被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {

            int realPosition = position % imageViews.size();
            String text = imageDescriptions[realPosition];
            tv_msg.setText(text);
            //1.把之前的设置为默认
            ll_group_point.getChildAt(lastIndex).setEnabled(false);
            //2.把当前的位置对应的页面设置为高亮
            ll_group_point.getChildAt(realPosition).setEnabled(true);
            //ll_group_point.getChildAt(position) --->ImageView
            lastIndex = realPosition;

        }

        /**
         * 当页面状态发生变化的时候回调这个方法
         * 静止到滑动
         * 滑动到静止
         *
         * @param state
         */
        boolean isDragging = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            //正在拖动中...
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                Log.e("TAG", "正在拖动中...");
                //移除消息
                handler.removeCallbacksAndMessages(null);
                isDragging = true;

                //静止状态中...
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                Log.e("TAG", "静止状态中...");

                handler.removeCallbacksAndMessages(null);//这个一定要加上
                handler.sendEmptyMessageDelayed(0, 3000);

                //正在滑动中...
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                Log.e("TAG", "正在滑动中...");
                isDragging = false;
                handler.removeMessages(0);//这个一定要加上
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    }

    /**
     * 创建一个继承于PagerAdapter的适配器，至少需要实现4方法
     */
    private class MyPagerAdapter extends PagerAdapter {

        //返回总条数
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;//imageViews.size()
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

            //1.添加到viewPager中
            ImageView imageView = imageViews.get(position % imageViews.size());
            container.addView(imageView);

            //2.并且返回当前页面的相关的特性
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

            return view == object;//此句和上面的相同
        }

        /**
         * 销毁container:ViewPager
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }

    private class NewsAdapter extends BaseAdapter {

        private ImageLoader imageLoader;

        public NewsAdapter() {
            imageLoader = new ImageLoader(context, R.drawable.loading, R.drawable.error);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news, null);
            }
            ImageView iv_item_pic = (ImageView) convertView.findViewById(R.id.iv_item_pic);
            TextView tv_item_title = (TextView) convertView.findViewById(R.id.tv_item_title);
            TextView tv_item_top = (TextView) convertView.findViewById(R.id.tv_item_top);
            TextView tv_item_date = (TextView) convertView.findViewById(R.id.tv_item_date);

            Data data = list.get(position);

            tv_item_title.setText(data.getTitle());
            tv_item_top.setText(data.getType());
            tv_item_date.setText(data.getDate());

            //加载图片
            imageLoader.loadImage(data.getThumbnail_pic_s(), iv_item_pic);

            return convertView;
        }
    }
}
