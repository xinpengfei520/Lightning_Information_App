package com.ldoublem.loadingView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.ldoublem.loadingviewlib.LVBattery;
import com.ldoublem.loadingviewlib.LVBlock;
import com.ldoublem.loadingviewlib.LVChromeLogo;
import com.ldoublem.loadingviewlib.LVCircular;
import com.ldoublem.loadingviewlib.LVCircularCD;
import com.ldoublem.loadingviewlib.LVCircularJump;
import com.ldoublem.loadingviewlib.LVCircularRing;
import com.ldoublem.loadingviewlib.LVCircularSmile;
import com.ldoublem.loadingviewlib.LVCircularZoom;
import com.ldoublem.loadingviewlib.LVEatBeans;
import com.ldoublem.loadingviewlib.LVFinePoiStar;
import com.ldoublem.loadingviewlib.LVFunnyBar;
import com.ldoublem.loadingviewlib.LVGears;
import com.ldoublem.loadingviewlib.LVGearsTwo;
import com.ldoublem.loadingviewlib.LVGhost;
import com.ldoublem.loadingviewlib.LVLineWithText;
import com.ldoublem.loadingviewlib.LVNews;
import com.ldoublem.loadingviewlib.LVPlayBall;
import com.ldoublem.loadingviewlib.LVRingProgress;
import com.ldoublem.loadingviewlib.LVWifi;

import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity {


    LVPlayBall mLVPlayBall;
    LVCircularRing mLVCircularRing;
    LVCircular mLVCircular;
    LVCircularJump mLVCircularJump;
    LVCircularZoom mLVCircularZoom;
    LVLineWithText mLVLineWithText;
    LVEatBeans mLVEatBeans;
    LVCircularCD mLVCircularCD;
    LVCircularSmile mLVCircularSmile;

    LVGears mLVGears;
    LVGearsTwo mLVGearsTwo;
    LVFinePoiStar mLVFinePoiStar;
    LVChromeLogo mLVChromeLogo;
    LVBattery mLVBattery;
    LVWifi mLVWifi;

    LVNews mLVNews;
    LVBlock mLVBlock;
    LVGhost mLVGhost;
    LVFunnyBar mLVFunnyBar;
    LVRingProgress mLVRingProgress;
    int mValueLVLineWithText = 0;
    int mValueLVNews = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_main);
        mLVPlayBall = (LVPlayBall) findViewById(R.id.lv_playball);
        mLVCircularRing = (LVCircularRing) findViewById(R.id.lv_circularring);
        mLVCircular = (LVCircular) findViewById(R.id.lv_circular);
        mLVCircularJump = (LVCircularJump) findViewById(R.id.lv_circularJump);
        mLVCircularZoom = (LVCircularZoom) findViewById(R.id.lv_circularZoom);
        mLVLineWithText = (LVLineWithText) findViewById(R.id.lv_linetext);
        mLVEatBeans = (LVEatBeans) findViewById(R.id.lv_eatBeans);
        mLVCircularCD = (LVCircularCD) findViewById(R.id.lv_circularCD);
        mLVCircularSmile = (LVCircularSmile) findViewById(R.id.lv_circularSmile);
        mLVGears = (LVGears) findViewById(R.id.lv_gears);
        mLVGearsTwo = (LVGearsTwo) findViewById(R.id.lv_gears_two);
        mLVFinePoiStar = (LVFinePoiStar) findViewById(R.id.lv_finePoiStar);
        mLVChromeLogo = (LVChromeLogo) findViewById(R.id.lv_chromeLogo);
        mLVBattery = (LVBattery) findViewById(R.id.lv_battery);
        mLVBattery.setBatteryOrientation(LVBattery.BatteryOrientation.HORIZONTAL);
//        mLVBattery.setBatteryOrientation(LVBattery.BatteryOrientation.HORIZONTAL);
        mLVBattery.setShowNum(false);
        mLVWifi = (LVWifi) findViewById(R.id.lv_wifi);
        mLVNews = (LVNews) findViewById(R.id.lv_news);
        mLVBlock = (LVBlock) findViewById(R.id.lv_block);
//        mLVBlock.isShadow(false);
        mLVGhost = (LVGhost) findViewById(R.id.lv_ghost);
        mLVFunnyBar=(LVFunnyBar)findViewById(R.id.lv_funnybar);
        mLVRingProgress=(LVRingProgress)findViewById(R.id.lv_ringp);
        mLVLineWithText.setValue(50);


    }

    public void startAnim(View v) {

        stopAll();
        if (v instanceof LVCircular) {
            ((LVCircular) v).startAnim();
        } else if (v instanceof LVCircularCD) {
            ((LVCircularCD) v).startAnim();
        } else if (v instanceof LVCircularSmile) {
            ((LVCircularSmile) v).startAnim();
        } else if (v instanceof LVCircularRing) {
            ((LVCircularRing) v).startAnim();
        } else if (v instanceof LVCircularZoom) {
            ((LVCircularZoom) v).startAnim();
        } else if (v instanceof LVCircularJump) {
            ((LVCircularJump) v).startAnim();
        } else if (v instanceof LVEatBeans) {
            ((LVEatBeans) v).startAnim();
        } else if (v instanceof LVPlayBall) {
            ((LVPlayBall) v).startAnim();
        } else if (v instanceof LVLineWithText) {
            startLVLineWithTextAnim();
        } else if (v instanceof LVGears) {
            ((LVGears) v).startAnim();
        } else if (v instanceof LVGearsTwo) {
            ((LVGearsTwo) v).startAnim();
        } else if (v instanceof LVFinePoiStar) {
            ((LVFinePoiStar) v).setDrawPath(false);
            ((LVFinePoiStar) v).startAnim();
        } else if (v instanceof LVChromeLogo) {
            ((LVChromeLogo) v).startAnim();
        } else if (v instanceof LVBattery) {
            ((LVBattery) v).startAnim();
        } else if (v instanceof LVWifi) {
            ((LVWifi) v).startAnim();
        } else if (v instanceof LVNews) {
//            ((LVNews) v).startAnim();
            startLVNewsAnim();
        } else if (v instanceof LVBlock) {
            ((LVBlock) v).startAnim();

        }else if(v instanceof LVGhost)
        {
            ((LVGhost) v).startAnim();
        }else if(v instanceof LVFunnyBar)
        {
            ((LVFunnyBar) v).startAnim();
        }
        else if(v instanceof LVRingProgress)
        {
            ((LVRingProgress)v).startAnim();
        }


    }

    public void startAnimAll(View v) {
        mLVCircular.startAnim();
        mLVCircularRing.startAnim();
        mLVPlayBall.startAnim();
        mLVCircularJump.startAnim();
        mLVCircularZoom.startAnim();
        startLVLineWithTextAnim();
        mLVEatBeans.startAnim();
        mLVCircularCD.startAnim();
        mLVCircularSmile.startAnim();
        mLVGears.startAnim();
        mLVGearsTwo.startAnim();
        mLVFinePoiStar.setDrawPath(true);
        mLVFinePoiStar.startAnim();
        mLVChromeLogo.startAnim();
        mLVBattery.startAnim();
        mLVWifi.startAnim();
//        mLVNews.startAnim();
        startLVNewsAnim();
        mLVBlock.startAnim();
        mLVGhost.startAnim();
        mLVFunnyBar.startAnim();
        mLVRingProgress.startAnim();
    }


    public void stopAnim(View v) {

        stopAll();

    }

    private void stopAll() {
        mLVCircular.stopAnim();
        mLVPlayBall.stopAnim();
        mLVCircularJump.stopAnim();
        mLVCircularZoom.stopAnim();
        mLVCircularRing.stopAnim();
        mLVEatBeans.stopAnim();
        stopLVLineWithTextAnim();
        mLVCircularCD.stopAnim();
        mLVCircularSmile.stopAnim();
        mLVGears.stopAnim();
        mLVGearsTwo.stopAnim();
        mLVFinePoiStar.stopAnim();
        mLVChromeLogo.stopAnim();
        mLVBattery.stopAnim();
        mLVWifi.stopAnim();
        stopLVNewsAnim();
        mLVBlock.stopAnim();
        mLVGhost.stopAnim();
        mLVFunnyBar.stopAnim();
        mLVRingProgress.stopAnim();

    }


    public Timer mTimerLVLineWithText = new Timer();// 定时器
    public Timer mTimerLVNews = new Timer();// 定时器


    private void startLVLineWithTextAnim() {
        mValueLVLineWithText = 0;
        if (mTimerLVLineWithText != null) {
            mTimerLVLineWithText.cancel();// 退出之前的mTimer
        }
        mTimerLVLineWithText = new Timer();
        timerTaskLVLineWithText();
    }

    private void stopLVLineWithTextAnim() {
        if (mTimerLVLineWithText != null) {
            mTimerLVLineWithText.cancel();// 退出之前的mTimer
            mLVNews.setValue(mValueLVNews);
        }
    }


    private void startLVNewsAnim() {
        mValueLVNews = 0;
        if (mTimerLVNews != null) {

            mTimerLVNews.cancel();
        }
        mTimerLVNews = new Timer();
        timerTaskLVNews();
    }

    private void stopLVNewsAnim() {
        mLVNews.stopAnim();
        if (mTimerLVNews != null) {
            mTimerLVNews.cancel();
            mLVLineWithText.setValue(mValueLVLineWithText);
        }
    }


    public void timerTaskLVNews() {
        mTimerLVNews.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mValueLVNews < 100) {
                    mValueLVNews++;
                    Message msg = mHandle.obtainMessage(1);
                    msg.arg1 = mValueLVNews;
                    mHandle.sendMessage(msg);
                } else {
                    mTimerLVNews.cancel();
                }
            }
        }, 0, 10);
    }


    public void timerTaskLVLineWithText() {
        mTimerLVLineWithText.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mValueLVLineWithText < 100) {

                    mValueLVLineWithText++;
                    Message msg = mHandle.obtainMessage(2);
                    msg.arg1 = mValueLVLineWithText;
                    mHandle.sendMessage(msg);

                } else {
                    mTimerLVLineWithText.cancel();
                }
            }
        }, 0, 50);
    }


    private Handler mHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2)
                mLVLineWithText.setValue(msg.arg1);
            else if (msg.what == 1) {
                mLVNews.setValue(msg.arg1);
            }
        }
    };


}
