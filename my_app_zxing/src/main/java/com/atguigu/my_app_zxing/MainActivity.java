package com.atguigu.my_app_zxing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private EditText et_main;
    private ImageView iv_main;
    private File pngFile;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_main = (EditText) findViewById(R.id.et_main);
        iv_main = (ImageView) findViewById(R.id.iv_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "关于");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 生成的回调
     *
     * @param v
     */
    public void produce(View v) {

        if ("".equals(et_main.getText().toString())) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_main.startAnimation(animation);
            Toast.makeText(MainActivity.this, "傻叉，您还没有输入内容", Toast.LENGTH_SHORT).show();

        } else {
//            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.moon);
//            Bitmap bitmap = EncodingUtils.createQRCode(et_main.getText().toString().trim(), 500, 500, logoBitmap);
            Bitmap bitmap = EncodingUtils.createQRCode(et_main.getText().toString().trim(), 500, 500, null);
            iv_main.setImageBitmap(bitmap);
            et_main.setText("");
            Toast.makeText(MainActivity.this, "生成成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 扫码二维码的回调
     *
     * @param v
     */
    public void scan(View v) {
        int requestCode = 1;
        startActivityForResult(new Intent(this, CaptureActivity.class), requestCode);
    }

    /**
     * 处理返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            et_main.setText(scanResult);
        }
    }

    /**
     * 随机生成二维码的回调
     *
     * @param v
     */
    public void random(View v) {

        // 给二维码添加logo图片
//        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.moon);
        bitmap = EncodingUtils.createQRCode("中秋节快乐", 500, 500, null);
        iv_main.setImageBitmap(bitmap);
        Toast.makeText(MainActivity.this, "傻叉，中秋节快乐", Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存二维码的回调
     *
     * @param v
     */
    public void save(View v) {
        File filesDir = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filesDir = new File("/sdcard/QRCode");
        } else {
            filesDir = new File("/storage/sdcard0/QRCode");
        }
        if (!filesDir.exists()) {
            filesDir.mkdir();
        }

        //获取sp对象
        SharedPreferences sp = this.getSharedPreferences("count", Context.MODE_PRIVATE);
        if ("".equals(sp.getString("count", ""))) {

            //获取Editor的对象
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("count", 0 + "");

            editor.commit();
        }
        int count = Integer.parseInt(sp.getString("count", ""));
        pngFile = new File(filesDir, "image" + (count++) + ".png");
        //获取Editor的对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("count", count + "");

        //提交数据(如果没有此方法的执行，是不会将数据写入文件的）
        editor.commit();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pngFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
