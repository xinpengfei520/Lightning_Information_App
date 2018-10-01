package top.codecafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.kymjs.api.Api;
import com.kymjs.base.BaseFrameActivity;
import com.kymjs.base.MainFragment;
import com.kymjs.model.Event;
import com.kymjs.rxvolley.rx.RxBus;

import rx.Subscription;
import rx.functions.Action1;
import top.codecafe.R;
import top.codecafe.delegate.MainDelegate;
import top.codecafe.fragment.BlogListFragment;
import top.codecafe.fragment.TopListFragment;
import top.codecafe.fragment.XituFragment;

/**
 * 主界面
 */
public class MainActivity extends BaseFrameActivity<MainDelegate> {

    public static final String MENU_CLICK_EVEN = "slid_menu_click_event";

    private MainFragment currentFragment; //当前内容所显示的Fragment
    private MainFragment content1 = new BlogListFragment();
    private MainFragment content2 = new XituFragment();
    private MainFragment content3 = new TopListFragment();

    private Subscription rxBusSubscript;

    private boolean isOnKeyBacking;
    private Handler mMainLoopHandler = new Handler(Looper.getMainLooper());
    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
            viewDelegate.cancleExit();
        }
    };


    @Override
    protected Class<MainDelegate> getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewDelegate.menuIsOpen()) {
                viewDelegate.changeMenuState();
            } else if (isOnKeyBacking) {
                mMainLoopHandler.removeCallbacks(onBackTimeRunnable);
                isOnKeyBacking = false;
                finish();
            } else {
                isOnKeyBacking = true;
                viewDelegate.showExitTip();
                mMainLoopHandler.postDelayed(onBackTimeRunnable, 2000);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 用Fragment替换内容区
     *
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(MainFragment targetFragment) {
        if (targetFragment.equals(currentFragment)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_content, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            targetFragment.onChange();
        }
        if (currentFragment != null && currentFragment.isVisible()) {
            transaction.hide(currentFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, content1, content1.getClass().getName())
                .commit();

        rxBusSubscript = RxBus.getDefault().take(Event.class)
                .subscribe(new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        changeContent(event);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxBusSubscript != null && rxBusSubscript.isUnsubscribed())
            rxBusSubscript.unsubscribe();
    }

    /**
     * 根据MainSlidMenu点击选择不同的响应
     *
     * @param event
     */
    private void changeContent(Event event) {
        if (!MENU_CLICK_EVEN.equals(event.getAction())) return;
        View view = event.getObject();
        switch (view.getId()) {
            case R.id.menu_item_tag1:
                changeFragment(content1);
                break;
            case R.id.menu_item_tag2:
                changeFragment(content2);
                break;
            case R.id.menu_item_tag3:
                changeFragment(content3);
                break;
            case R.id.menu_item_tag4:
                BlogDetailActivity.goinActivity(this, Api.OSL, null);
                break;
            default:
                break;
        }
        viewDelegate.changeMenuState();
    }
}
