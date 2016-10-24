package com.atguigu.guoqingjie_app.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.guoqingjie_app.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewDetails extends Activity {

    private static final int MESSAGE_OK = 1;
    private static final int MESSAGE_ERROR = 2;
    private TextView tv_details;
    private ProgressBar pb_loading;
    //handler的使用：1.创建Handler的对象，并重写handleMessage()
    /*private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {//此处的方法，也是回调方法

            //handler的使用：4.在handleMessage()实现消息的处理---在主线程中执行的
            switch (msg.what) {
                case MESSAGE_OK:
                    String content = (String) msg.obj;
                    tv_details.setText(content);
                    break;
                case MESSAGE_ERROR:
                    Toast.makeText(NewDetails.this, "数据加载失败", Toast.LENGTH_SHORT).show();

                    break;
            }

            pb_loading.setVisibility(View.INVISIBLE);
        }

    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);

        tv_details = (TextView) findViewById(R.id.tv_details);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        //设置TextView可上下滑动(与xml中的属性对应)
        tv_details.setMovementMethod(ScrollingMovementMethod.getInstance());

        initDetails();
    }

    private void initDetails() {

        //1.主线程：显示提示视图
        pb_loading.setVisibility(View.VISIBLE);

        //2.分线程：联网下载数据
        new Thread() {
            @Override
            public void run() {
                final String content = getStringData();

                //3.主线程：更新提示视图
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (content == null) {
                            Toast.makeText(NewDetails.this, "加载数据失败", Toast.LENGTH_SHORT).show();

                        } else {
//                            tv_details.setText(content);
                            tv_details.setText(Html.fromHtml(content));

                        }
                        // //设置ProgressBar的消失
                        pb_loading.setVisibility(View.GONE);//快捷键：ctrl + alt + /
                    }
                });
            }
        }.start();
    }

    private String getStringData() {

        Intent intent = getIntent();
        String path = intent.getExtras().getString("url");
        HttpURLConnection conn = null;
        InputStream is = null;

        try {

            // 2.将此地址封装在URL对象中
            URL url = new URL(path);
            // 3.获取与服务器的连接的对象
            conn = (HttpURLConnection) url.openConnection();

            // 4.设置连接的请求参数
            // 4.1设置请求方式
            conn.setRequestMethod("GET");
            // 4.2设置连接的超时时间
            conn.setConnectTimeout(5000);
            // 4.3设置读取数据的超时时间
            conn.setReadTimeout(5000);
            //5.获取连接
            conn.connect();

            // 6.获取响应码，如果响应码是200，表示正确的获取到了服务器端返回的数据
            if (conn.getResponseCode() == 200) {
                //7.获取输入流
                is = conn.getInputStream();
                // 8.将输入流中的数据转换为字符串，并设置给EditText显示
                String content = fromInputStreamToString(is);

                return content;

            } else {

                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    /**
     * 从输入流中读取内容，返回一个内容的字符串
     *
     * @param fis
     * @return
     * @throws IOException
     */
    private String fromInputStreamToString(InputStream fis) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];
        int len;
        while ((len = fis.read(buffer)) != -1) {

            baos.write(buffer, 0, len);
        }
        return baos.toString();

    }

    /**
     * 返回按钮的回调
     * @param v
     */
    public void back(View v) {
        finish();
    }

    /**
     * 分享按钮的回调
     * @param v
     */
    public void share(View v) {
        Toast.makeText(NewDetails.this, "分享", Toast.LENGTH_SHORT).show();
    }
}
