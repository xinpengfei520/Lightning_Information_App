package com.atguigu.guoqingjie_app.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.atguigu.guoqingjie_app.R;
import com.atguigu.guoqingjie_app.base.BaseFragment;

/**
 * Created by xinpengfei on 2016/10/4.
 * <p>
 * Function :
 */

public class BeautyFragment extends BaseFragment {

    private ListView lv_beauty;

    @Override
    public View initView() {

        Log.e("TAG", "本地美图UI创建了");
        View view = View.inflate(context, R.layout.activity_beauty, null);
        lv_beauty = (ListView) view.findViewById(R.id.lv_beauty);

        lv_beauty.setAdapter(new BeautyAdapter());
        initBeauty();
        return view;

    }

    private void initBeauty() {

        /*//2.分线程联网下载数据
        new Thread() {

            @Override
            public void run() {

                String path = "http://192.168.23.1:8080/Test/";

                HttpURLConnection conn = null;
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    if (conn.getResponseCode() == 200) {

                        is = conn.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "联网失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();*/

    }

    private class BeautyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
