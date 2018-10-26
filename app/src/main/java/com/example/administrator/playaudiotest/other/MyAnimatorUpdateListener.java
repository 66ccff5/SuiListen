package com.example.administrator.playaudiotest.other;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.CountDownTimer;

/**
 * Created by Administrator on 2018/5/21 0021.
 */

public class MyAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {


    private ObjectAnimator animator;

    public MyAnimatorUpdateListener(ObjectAnimator animator){
        this.animator = animator;
    }


    private boolean isPause = false;

    private boolean isPaused = false;

    private boolean isPlay = true;

    private float fraction = 0.0f;

    private long mCurrentPlayTime = 0l;

    public boolean isPause(){
        return isPause;
    }

    public boolean isPlay(){
        return isPlay;
    }

    public void pause(){
        isPause = true;
        isPlay = false;
    }

    public void play(){
        isPause = false;
        isPaused = false;
        isPlay = true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if(isPause){
            if(!isPaused){
                mCurrentPlayTime = valueAnimator.getCurrentPlayTime();
                fraction = valueAnimator.getAnimatedFraction();
                valueAnimator.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float v) {
                        return fraction;
                    }
                });
                isPaused = true;
            }
            new CountDownTimer(ValueAnimator.getFrameDelay(),ValueAnimator.getFrameDelay()){
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    animator.setCurrentPlayTime(mCurrentPlayTime);
                }
            }.start();
        }else {
            valueAnimator.setInterpolator(null);
        }
    }
}
