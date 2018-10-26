package com.example.administrator.playaudiotest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.administrator.playaudiotest.activity.CloudActivity;
import com.example.administrator.playaudiotest.other.MineMusicList;
import com.example.administrator.playaudiotest.bean.MyApplication;
import com.example.administrator.playaudiotest.bean.PlayContent;
import com.example.administrator.playaudiotest.R;

public class BottomPlayService extends Service {

    View mFloatView;
    private FrameLayout mContentContainer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    public BottomPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public   void initBottomEvents(){

//
        PlayContent.playOrpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CloudActivity.mmediaPlayer.isPlaying()) {
                    CloudActivity.mmediaPlayer.start();
                    PlayContent.playCont = true;
                    PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
                }else {
                    CloudActivity.mmediaPlayer.pause();
                    PlayContent.playCont = false;
                    PlayContent.playOrpause.setImageResource(R.drawable.play_btn);

                }

            }
        });
    }

    public void initBottomDatas(FragmentManager fm){
        PlayContent.bottomPagerAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return MineMusicList.bottomMusicFragment.get(position);
            }

            @Override
            public int getCount() {
                return MineMusicList.bottomMusicFragment.size();
            }
        };
//        PlayContent.bottomViewPager.setAdapter(PlayContent.bottomPagerAdapter);
//        PlayContent.bottomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(PlayContent.playCont == false){
//                    CloudActivity.mmediaPlayer.reset();
//                    MusicAdapter.initMediaPlayer(MineMusicList.mineMusicList.musicList.get(0).getMusicPath());
//
//                }else {
//                    CloudActivity.mmediaPlayer.reset();
//                    MusicAdapter.initMediaPlayer(MineMusicList.mineMusicList.musicList.get(position).getMusicPath());
//                    CloudActivity.mmediaPlayer.start();
//                    PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
//                }
//                PlayContent.playCont = true;
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                PlayContent.bottomViewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//
//            }
//        });

    }




    public void initBottomView(){
//        PlayContent.bottomViewPager = (ViewPager) mFloatView.findViewById(R.id.bottom_view_pager);
        PlayContent.playOrpause = (ImageView) mFloatView.findViewById(R.id.start_pause);

    }


    public void CreateFloatView() {
        if (MineMusicList.mineMusicList.musicList.size() > 0) {
            mFloatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.bottom_musicplay, null);
            initBottomView();
            initBottomEvents();
          //  initBottomDatas(MyApplication.nowActivity().);
            ViewGroup mDecorView = (ViewGroup) MyApplication.nowActivity().getWindow().getDecorView();
            mContentContainer = (FrameLayout) ((ViewGroup) mDecorView.getChildAt(0)).getChildAt(1);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;//设置对齐位置
            mContentContainer.addView(mFloatView, layoutParams);

        }
    }








}
