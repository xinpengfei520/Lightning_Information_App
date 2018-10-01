package com.atguigu.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作用：自定义带下拉刷新的ListView
 */
public class RefreshListView extends ListView {
    /**
     * 整个头部：下拉刷新和顶部轮播图
     */
    private LinearLayout headView;
    private ImageView iv_red_arrow;
    private ProgressBar progressbar;

    private TextView tv_status;
    private TextView tv_time;
    private float startY;

    private static final int PULL_DOWN_REFRESH = 0;// 下拉刷新状态
    private static final int RELEASE_REFRESH = 1;  // 手松刷新状态
    private static final int REFRESHING = 2;       // 正在刷新状态
    private int currentStatus = PULL_DOWN_REFRESH; // 当前状态

    private int headerHeight;     // 下拉刷新控件的高
    private int footerViewHeight; // 加载更多控件的高
    private boolean isLoadMore = false; // 是否已经加载更多
    private View topnews;    // 顶部轮播图部分的视图
    private View footerView; // 加载更多的View
    private View pullDwonView;

    private Animation upAnimation;
    private Animation dwonAnimation;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.footview_refresh, null);

        // 默认是隐藏
//        View.setPaddingTop(0,-控件的高，0,0);//完成隐藏
//        View.setPaddingTop(0,0，0,0);//完成显示
//        View.setPaddingTop(0,控件的高，0,0);//控件两倍显示
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);

        addFooterView(footerView);

        // 设置监听滑动底部
        setOnScrollListener(new MyOnScrollListener());
    }

    /**
     * 添加顶部轮播图
     */
    public void addTopNewsView(View topnews) {
        this.topnews = topnews;
        headView.addView(topnews); // 下拉刷新控件和顶部轮播图部分合并一块，一并以一个头的方法添加到ListView中
    }

    class MyOnScrollListener implements OnScrollListener {

        /**
         * ListView当状态变化的时候回调
         * 静止-->手指滑动
         * 手指滑动-->惯性滚动
         * 惯性滚动-->静止
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                // 并且是滑动到最后一条的时候
                // 当滑动到最后一个可见并且等于集合中最后一条位置的时候
                if (getLastVisiblePosition() == getAdapter().getCount() - 1) {

                    // 1.状态修改加载更多
                    isLoadMore = true;
                    // 2.显示加载更多控件
                    footerView.setPadding(10, 10, 10, 10);
                    // 3.回调接口
                    if (refreshListener != null) {
                        refreshListener.onLoadeMore();
                    }
                }
            }
        }

        // ListView正在滚动的时候回调
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        dwonAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        dwonAnimation.setDuration(500);
        dwonAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        // 初始化HeaderView
        headView = (LinearLayout) View.inflate(context, R.layout.header_refresh, null);
        iv_red_arrow = (ImageView) headView.findViewById(R.id.iv_red_arrow);
        progressbar = (ProgressBar) headView.findViewById(R.id.progressbar);
        pullDwonView = headView.findViewById(R.id.ll_pull_down);
        tv_status = (TextView) headView.findViewById(R.id.tv_status);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);

//        View.setPaddingTop(0,-控件的高，0,0);//完成隐藏
//        View.setPaddingTop(0,0，0,0);//完成显示
//        View.setPaddingTop(0,控件的高，0,0);//控件两倍显示
        pullDwonView.measure(0, 0);//测量控件的高
        headerHeight = pullDwonView.getMeasuredHeight(); // 得到控件的高
        pullDwonView.setPadding(0, -headerHeight, 0, 0);

        addHeaderView(headView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 1.记录坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = ev.getY();
                float distanceY = endY - startY;

                // 判断顶部轮播图是否完全显示
                boolean isDisplayTopNews = isDisplayTopNews();
                if (!isDisplayTopNews) {
                    break;
                }

                if (currentStatus == REFRESHING) {
                    break;
                }

                if (distanceY > 0) {
                    // float paddingTop = -控件的高 + distanceY
                    float paddingTop = -headerHeight + distanceY;

                    if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        // 切换成手松刷新状态
                        currentStatus = RELEASE_REFRESH;
                        refreshView(currentStatus);
                    } else if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        currentStatus = PULL_DOWN_REFRESH;
                        refreshView(currentStatus);
                    }
                    // View.setPaddingTop(0,paddingTop，0,0); // 完成显示
                    pullDwonView.setPadding(0, (int) paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:

                if (currentStatus == PULL_DOWN_REFRESH) {
                    pullDwonView.setPadding(10, -headerHeight, 10, 10);//隐藏
                } else if (currentStatus == RELEASE_REFRESH) {
                    // 正在加载
                    currentStatus = REFRESHING;

                    // 下拉刷新控件全部显示
                    pullDwonView.setPadding(10, 10, 10, 10);//完全显示的状态
                    refreshView(currentStatus);
                    // 回调接口由接口的实现类去联网请求
                    if (refreshListener != null) {
                        refreshListener.onPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int listViewOnScreenY = -1;

    /**
     * 判断是否完全显示顶部轮播图部分
     * 当ListView在Y轴的坐标小于或等于顶部轮播图在Y轴坐标的时候，就完全显示
     */
    private boolean isDisplayTopNews() {
        if (topnews != null) {
            int[] location = new int[2];
            // 得到ListView在屏幕的Y轴坐标
            if (listViewOnScreenY == -1) {
                this.getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }
            // 得到顶部轮播部分在屏幕的Y轴坐标
            topnews.getLocationOnScreen(location);
            int topNewsOnScreenY = location[1];

//        if(listViewOnScreenY <=topNewsOnScreenY){
//            return  true;
//        }else{
//            return  false;
//        }

            return listViewOnScreenY <= topNewsOnScreenY;
        } else {
            return true;
        }
    }

    private void refreshView(int currentStatus) {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH: // 下拉刷新
                tv_status.setText("下拉刷新...");
                iv_red_arrow.setVisibility(VISIBLE);
                iv_red_arrow.startAnimation(dwonAnimation);
                break;
            case RELEASE_REFRESH: // 手松刷新
                tv_status.setText("手松刷新...");
                iv_red_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING: // 正在刷新
                tv_status.setText("正在刷新...");
                iv_red_arrow.setVisibility(GONE);
                iv_red_arrow.clearAnimation();
                progressbar.setVisibility(VISIBLE);
                break;
        }
    }

    public void onFinishRefrsh(boolean sucess) {
        if (isLoadMore) { // 加载更多
            isLoadMore = false;
            footerView.setPadding(0, -footerViewHeight, 0, 0); // 设置隐藏

        } else { // 下拉刷新
            currentStatus = PULL_DOWN_REFRESH;
            pullDwonView.setPadding(0, -headerHeight, 0, 0); // 把下拉刷新控件隐藏
            progressbar.setVisibility(GONE);
            iv_red_arrow.setVisibility(VISIBLE);
            tv_status.setText("下拉刷新...");
            if (sucess) {
                tv_time.setText("上次更新时间:" + getSystemTime());
            }
        }
    }

    /**
     * 得到当前的系统时间
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 刷新的监听器
     */
    public interface OnRefreshListener {
        void onPullDownRefresh();// 当下拉刷新的时候回调这个方法

        void onLoadeMore();      // 当加载更多额时候回调
    }

    private OnRefreshListener refreshListener;

    /**
     * 设置刷新的监听：下拉刷新和上拉刷新(加载更多)
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }
}
