package com.atguigu.guoqingjie_app.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinpengfei on 2016/10/5.
 * Function :图片加载的类，考虑图片的三级缓存
 */

public class ImageLoader {

    private Context context;
    private int loading;
    private int error;

    private Map<String, Bitmap> bitmapCache = new HashMap<String, Bitmap>();

    public ImageLoader(Context context, int loading, int error) {
        this.context = context;
        this.loading = loading;
        this.error = error;
    }

    //加载图片
    public void loadImage(String imagePath, ImageView iv_item_pic) {

        //给每一个ImageView设置唯一的tag
        iv_item_pic.setTag(imagePath);


        //考虑一级缓存
        Bitmap bitmap = fromFirstCache(imagePath);
        if (bitmap != null) {
            iv_item_pic.setImageBitmap(bitmap);
            return;
        }

        //二级缓存
        bitmap = fromSecondCache(imagePath);
        if (bitmap != null) {
            iv_item_pic.setImageBitmap(bitmap);
            //缓存到一级缓存
            bitmapCache.put(imagePath, bitmap);
            return;
        }
        //三级缓存
        fromThirdCache(imagePath, iv_item_pic);

    }

    // 三级缓存：使用异步任务(AsyncTask)实现
    public void fromThirdCache(final String imagePath, final ImageView iv_item_pic) {
        new AsyncTask<Void, Void, Bitmap>() {
            // 联网操作的第1步
            protected void onPreExecute() {
                iv_item_pic.setImageResource(loading);
            }

            //联网操作的第2步：分线程联网
            @Override
            protected Bitmap doInBackground(Void... params) {

                //判断当前的url与要联网的url是否一致，如果不一致。表明是旧的url地址，则不需要联网下载数据
                String currentUrl = (String) iv_item_pic.getTag();
                if (currentUrl != imagePath) {
                    return null;
                }

                HttpURLConnection conn = null;
                InputStream is = null;
                try {
                    URL url = new URL(imagePath);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        is = conn.getInputStream();//图片资源对应的输入流
                        Bitmap bitmap = BitmapFactory.decodeStream(is);


                        //缓存到一级缓存
                        bitmapCache.put(imagePath, bitmap);

                        //缓存到二级缓存
                        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                        //filePath:即为保存到的本地文件的绝对路径
                        String filePath = context.getExternalFilesDir(null) + "/" + fileName;
                        //如何将内存中的图片对象保存为硬盘中的一个图片文件
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));

                        return bitmap;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
                }

                return null;
            }

            //联网操作的第3步：主线程更新界面
            protected void onPostExecute(Bitmap result) {

                //判断要显示的图片（路径imagePath）是否是当前imageView要加载的图片(路径currentUrl)
                String currentUrl = (String) iv_item_pic.getTag();
                if (currentUrl != imagePath) {
                    return;
                }


                if (result != null) {//正确的获取到了服务器端发送的数据
                    //显示图片
                    iv_item_pic.setImageBitmap(result);

                } else {//①联网失败②联网成功，资源不存在等
                    iv_item_pic.setImageResource(error);
                }
            }
        }.execute();
    }

    /*
     * 二级缓存
     * imagePath: http://192.168.56.1:8989//Web_server/images/f17.jpg
     *
     * 本地图片文件的路径：storage/sdcard/Android/data/应用包名/files/f17.jpg
     */
    public Bitmap fromSecondCache(String imagePath) {

        String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);

        String filePath = context.getExternalFilesDir(null) + "/" + fileName;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

    //一级缓存
    public Bitmap fromFirstCache(String imagePath) {
        return bitmapCache.get(imagePath);

    }
}
