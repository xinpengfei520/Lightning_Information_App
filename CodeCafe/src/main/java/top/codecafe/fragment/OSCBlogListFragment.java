package top.codecafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kymjs.api.Api;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.model.osc.OSCBlog;
import com.kymjs.model.osc.OSCBlogList;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.activity.OSCBlogDetailActivity;
import top.codecafe.utils.XmlUtil;

/**
 * OSC博客列表
 *
 * @author kymjs (http://www.kymjs.com/) on 12/10/15.
 */
public class OSCBlogListFragment extends MainListFragment<OSCBlog> {

    private Subscription cacheSubscript;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cacheSubscript = Observable.just(RxVolley.getCache(Api.OSC_BLOG + getHttpParams(0)
                .getUrlParams()))
                .filter(new Func1<byte[], Boolean>() {
                    @Override
                    public Boolean call(byte[] cache) {
                        return cache != null && cache.length != 0;
                    }
                })
                .map(new Func1<byte[], ArrayList<OSCBlog>>() {
                    @Override
                    public ArrayList<OSCBlog> call(byte[] bytes) {
                        return parserInAsync(bytes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<OSCBlog>>() {
                    @Override
                    public void call(ArrayList<OSCBlog> blogs) {
                        datas = blogs;
                        adapter.refresh(datas);
                        viewDelegate.mEmptyLayout.dismiss();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    @Override
    protected ArrayList<OSCBlog> parserInAsync(byte[] t) {
        OSCBlogList list = XmlUtil.toBean(OSCBlogList.class, t);
        ArrayList<OSCBlog> datas = null;
        if (list != null) {
            datas = list.getBloglist();
            for (OSCBlog data : datas) data.format();
        }
        return datas;
    }

    @Override
    protected BasePullUpRecyclerAdapter<OSCBlog> getAdapter() {
        return new BasePullUpRecyclerAdapter<OSCBlog>(recyclerView, datas, R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, final OSCBlog item, int position) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, item.getBody().trim());
                holder.setText(R.id.item_blog_tv_author, "推荐阅读");
                holder.setText(R.id.item_blog_tv_date, item.getPubDate());

                holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.GONE);
                holder.getView(R.id.item_blog_img).setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void doRequest() {
        doRequest(0);
    }

    private void doRequest(int index) {
        new RxVolley.Builder().url(Api.OSC_BLOG)
                .params(getHttpParams(index))
                .contentType(RxVolley.Method.GET)
                .cacheTime(100)
                .callback(callBack)
                .doTask();
    }

    private HttpParams getHttpParams(int index) {
        HttpParams params = new HttpParams();
        params.put("authoruid", 2426111);
        params.put("pageIndex", index);
        params.put("pageSize", 20);
        return params;
    }

    @Override
    public void onBottom() {
        doRequest(datas.size() / 20);
        adapter.setState(BasePullUpRecyclerAdapter.STATE_LOADING);
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        OSCBlog blog = ((OSCBlog) data);
        OSCBlogDetailActivity.goinActivity(getActivity(), blog.getId(), blog.getUrl(),
                blog.getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cacheSubscript != null && cacheSubscript.isUnsubscribed())
            cacheSubscript.unsubscribe();
    }
}
