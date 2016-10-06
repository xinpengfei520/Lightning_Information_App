package fragment;

import android.util.Log;
import android.view.View;

import com.atguigu.guoqingjie_app.R;

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
