package com.atguigu.guoqingjie_app.fragment;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.guoqingjie_app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.atguigu.guoqingjie_app.bean.ImageLoader;

import com.atguigu.guoqingjie_app.base.BaseFragment;
import com.atguigu.guoqingjie_app.domain.Trailers;

/**
 * Created by xinpengfei on 2016/10/4.
 * <p>
 * Function :
 */

public class VideoFragment extends BaseFragment {

    private static final int MESSAGE_RESULT_OK = 1;
    private static final int MESSAGE_RESULT_ERROR = 2;
    private static final int MESSAGE_RESULT_NO = 3;
    private ListView lv_video;
    private VideoAdapter videoAdapter;
    private List<Trailers> videolist;
    private LinearLayout ll_loading;
    private Handler handler2 = new Handler() {
        //处理消息
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MESSAGE_RESULT_OK:
                    ll_loading.setVisibility(View.GONE);//移除加载视图

                    //显示列表
                    lv_video.setAdapter(videoAdapter);
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

    @Override
    public View initView() {

        Log.e("TAG", "本地新闻UI创建了");
        View view = View.inflate(context, R.layout.activity_video, null);
        lv_video = (ListView) view.findViewById(R.id.lv_video);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        videoAdapter = new VideoAdapter();
//        lv_video.setAdapter(videoAdapter);
        //联网操作的过程:第1步：主线程：显示提示视图
        ll_loading.setVisibility(View.VISIBLE);
        initVideoData();

        return view;

    }

    private void initVideoData() {
        videolist = new ArrayList<>();

        //联网操作的过程：第2步：分线程：联网下载数据
        new Thread() {
            public void run() {
                String path = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
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
                        //手动解析json数据并添加到集合中
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.optJSONArray("trailers");
                        if (jsonArray != null && jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject item = (JSONObject) jsonArray.get(i);
                                if (item != null) {

                                    //创建类
                                    Trailers trailers = new Trailers();
                                    videolist.add(trailers);//添加到集合中

                                    String name = item.optString("movieName");
                                    trailers.setMovieName(name);
                                    String desc = item.optString("videoTitle");
                                    trailers.setVideoTitle(desc);
                                    String imageUrl = item.optString("coverImg");
                                    trailers.setCoverImg(imageUrl);

                                }

                            }
                        }

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

    class VideoAdapter extends BaseAdapter {

        private ImageLoader imageLoader;

        public VideoAdapter() {
            imageLoader = new ImageLoader(context, R.drawable.loading, R.drawable.error);
        }

        @Override
        public int getCount() {
            return videolist.size();
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

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_video, null);
            }
            ImageView coverImg = (ImageView) convertView.findViewById(R.id.coverImg);
            TextView videoTitle = (TextView) convertView.findViewById(R.id.videoTitle);
            TextView movieName = (TextView) convertView.findViewById(R.id.movieName);

            Trailers trailer = videolist.get(position);

            videoTitle.setText(trailer.getVideoTitle());
            movieName.setText(trailer.getMovieName());

            //加载图片
            imageLoader.loadImage(trailer.getCoverImg(), coverImg);

            return convertView;
        }
    }
}
