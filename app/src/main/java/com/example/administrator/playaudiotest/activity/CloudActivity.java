package com.example.administrator.playaudiotest.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.playaudiotest.adapter.MusicAdapter;

public class CloudActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    public static MediaPlayer mmediaPlayer = new MediaPlayer();

    public static FragmentManager fragmentManager;

    private WindowManager windowManager;
    private FrameLayout mContentContainer;
    private View mFloatView;


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch (MusicAdapter.CloudMusicAdapter.getPlayModel()){
            case 0:
                MusicAdapter.CloudMusicAdapter.playNextMusic();
                break;
            case 1:
                MusicAdapter.CloudMusicAdapter.playMusicLoop();
                break;
            case 2:
                MusicAdapter.CloudMusicAdapter.playRandom();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
