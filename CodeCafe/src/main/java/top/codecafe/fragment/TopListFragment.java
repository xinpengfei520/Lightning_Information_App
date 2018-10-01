package top.codecafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.kymjs.api.Api;
import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.frame.adapter.RecyclerHolder;
import com.kymjs.model.osc.Favorite;
import com.kymjs.model.osc.FavoriteList;
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
 * 推荐阅读列表界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/10/15.
 */
public class TopListFragment extends MainListFragment<Favorite> {

    private Subscription cacheSubscript;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cacheSubscript = Observable.just(RxVolley.getCache(Api.OSC_COLLECT_BLOG))
                .filter(new Func1<byte[], Boolean>() {
                    @Override
                    public Boolean call(byte[] cache) {
                        return cache != null && cache.length != 0;
                    }
                })
                .map(new Func1<byte[], ArrayList<Favorite>>() {
                    @Override
                    public ArrayList<Favorite> call(byte[] bytes) {
                        return parserInAsync(bytes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Favorite>>() {
                    @Override
                    public void call(ArrayList<Favorite> blogs) {
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
    protected BasePullUpRecyclerAdapter<Favorite> getAdapter() {
        return new BasePullUpRecyclerAdapter<Favorite>(recyclerView, datas, R.layout.item_blog) {
            @Override
            public void convert(RecyclerHolder holder, Favorite item, int position) {
                holder.setText(R.id.item_blog_tv_title, item.getTitle());
                holder.setText(R.id.item_blog_tv_description, "推荐播客:" + item.getTitle());
                holder.setText(R.id.item_blog_tv_author, "推荐阅读");

                holder.getView(R.id.item_blog_tv_date).setVisibility(View.GONE);
                holder.getView(R.id.item_blog_tip_recommend).setVisibility(View.GONE);
                holder.getView(R.id.item_blog_img).setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected ArrayList<Favorite> parserInAsync(byte[] t) {
        return XmlUtil.toBean(FavoriteList.class, t).getFavoritelist();
    }

    @Override
    public void doRequest() {
        HttpParams params = new HttpParams();
        params.putHeaders("cookie", Api.OSC_COOKIE);
        new RxVolley.Builder().url(Api.OSC_COLLECT_BLOG)
                .params(params)
                .cacheTime(60)
                .httpMethod(RxVolley.Method.GET)
                .callback(callBack)
                .doTask();
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        Favorite blog = (Favorite) data;
        OSCBlogDetailActivity.goinActivity(getActivity(), blog.getId(), blog.getUrl(), blog
                .getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cacheSubscript != null && cacheSubscript.isUnsubscribed())
            cacheSubscript.unsubscribe();
    }
}
