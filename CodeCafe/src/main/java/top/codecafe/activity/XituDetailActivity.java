package top.codecafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.kymjs.base.backactivity.BaseBackActivity;
import com.kymjs.model.XituBlog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.rx.Result;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.codecafe.R;
import top.codecafe.delegate.BrowserDelegate;
import top.codecafe.inter.IRequestVo;
import top.codecafe.utils.LinkDispatcher;
import top.codecafe.utils.Tools;
import top.codecafe.widget.EmptyLayout;

/**
 * 来自稀土掘金数据的博客详情界面
 *
 * @author kymjs (http://www.kymjs.com/) on 12/4/15.
 */
public class XituDetailActivity extends BaseBackActivity<BrowserDelegate> implements IRequestVo {
    public static final String KEY_XITU_DATA = "xitu_data_key";
    private XituBlog data;

    private EmptyLayout emptyLayout;
    private String contentUrl;

    private Subscription cacheSubscript;
    private Subscription requestSubscript;

    @Override
    protected Class<BrowserDelegate> getDelegateClass() {
        return BrowserDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        emptyLayout = viewDelegate.get(R.id.emptylayout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        data = intent.getParcelableExtra(KEY_XITU_DATA);
        if (data != null) {
            cacheSubscript = Observable.just(RxVolley.getCache(data.getLink()))
                    .map(new Func1<byte[], String>() {
                        @Override
                        public String call(byte[] bytes) {
                            return new String(bytes);
                        }
                    })
                    .filter(new Func1<String, Boolean>() {
                        @Override
                        public Boolean call(String s) {
                            return !TextUtils.isEmpty(s);
                        }
                    })
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            return contentUrl = parserHtml(s);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            viewDelegate.setContentUrl(contentUrl);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            contentUrl = "";
                        }
                    });
            doRequest();
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_share && !TextUtils.isEmpty(contentUrl)) {
            Tools.shareUrl(this, contentUrl);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doRequest() {
        requestSubscript = new RxVolley.Builder().url(data.getLink()).getResult()
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        return contentUrl = parserHtml(new String(result.data));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s != null && viewDelegate != null) {
                            viewDelegate.setContentUrl(s);
                            setTitle(LinkDispatcher.getActionTitle(s, data.getTitle()));
                        } else {
                            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewDelegate != null && viewDelegate.webView != null) {
            viewDelegate.webView.removeAllViews();
            viewDelegate.webView.destroy();
        }
        if (cacheSubscript != null && cacheSubscript.isUnsubscribed())
            cacheSubscript.unsubscribe();
        if (requestSubscript != null && requestSubscript.isUnsubscribed())
            requestSubscript.unsubscribe();
    }

    /**
     * 跳转到博客详情界面
     *
     * @param data 传递要显示的博客信息
     */
    public static void goinActivity(Context cxt, @NonNull XituBlog data) {
        Intent intent = new Intent(cxt, XituDetailActivity.class);
        intent.putExtra(KEY_XITU_DATA, data);
        cxt.startActivity(intent);
    }

    /**
     * 对博客数据加工,适应手机浏览
     */
    public String parserHtml(String html) {
        String title = data.getTitle();
        int sub = title.indexOf(']');
        if (sub < 0) return "";
        title = title.substring(sub + 1).trim();

        int index = html.indexOf(title + "</h2>");
        if (index < 0) return "";
        html = html.substring(0, index).trim();

        int star = html.lastIndexOf("<a href=\"");
        if (star < 0) return "";
        html = html.substring(star + 9);

        int end = html.indexOf("\"");
        if (end < 0) return "";
        html = html.substring(0, end);
        return html;
    }
}
