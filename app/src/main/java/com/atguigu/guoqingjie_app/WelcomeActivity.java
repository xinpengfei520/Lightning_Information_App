package com.atguigu.guoqingjie_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {

    private RelativeLayout rl_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        rl_welcome = (RelativeLayout)findViewById(R.id.rl_welcome);

        playAnimation();
    }

    private void playAnimation() {

        //1.缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(3000);
        //2.透明度动画(从透明到不透明)
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        //3.旋转动画：参数说明：0~360度旋转，旋转轴的坐标都是相对于自己大小的0.5
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3000);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);

        /**
         * 设置动画的监听，因为此处时动画集(同时进行)，可选其中一个动画进行监听，注意此处不能给
         * animationSet设置监听
         */
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
                alphaAnimation2.setDuration(3000);
                alphaAnimation2.setFillAfter(true);
                alphaAnimation2.setAnimationListener(new Animation.AnimationListener(){

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();//在开启一个新的Activity的同时，销毁当前的Activity
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                rl_welcome.startAnimation(alphaAnimation2);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rl_welcome.startAnimation(animationSet);

    }
}
