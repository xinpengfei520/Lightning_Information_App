package com.atguigu.guoqingjie_app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchActivity extends Activity {

    private EditText et_input;
    private ImageView iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_input = (EditText) findViewById(R.id.et_input);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        /**
         * 给sv_search设置一个文本改变的监听
         * 注 ： 参数：需要一个TextWatcher的对象，而TextWatcher是一个接口，所以需要一个接口的实现类
         */
        et_input.addTextChangedListener(new inputLetterChangerListener());
    }

    public void delete(View v) {
        String text = et_input.getText().toString();

        if (!"".equals(text)) {
            et_input.setText("");
            iv_delete.setVisibility(View.GONE);
        }
    }

    /**
     * 点击取消的回调
     * @param v
     */
    public void cancel(View v) {
        finish();
    }

    private class inputLetterChangerListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!"".equals(s.toString())) {
                iv_delete.setVisibility(View.VISIBLE);
            } else {
                iv_delete.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
