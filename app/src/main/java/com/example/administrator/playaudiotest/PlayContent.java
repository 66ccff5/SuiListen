package com.example.administrator.playaudiotest;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.playaudiotest.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class PlayContent{


    public static FragmentPagerAdapter bottomPagerAdapter;
    public static boolean playCont = false;
    public static ImageView playOrpause;
    public static LinearLayout bottomPager;
    public static List<ImageView> bottomMusicImageList = new ArrayList<>();
    public static List<TextView> bottomMusicTitleList = new ArrayList<>();
    public static List<TextView> bottomMusicSingerList = new ArrayList<>();
    public static List<ImageView> bottomPlayOrPauseList = new ArrayList<>();

    public static void setPlayOrPause(){

    }


    public static void setTitle(String title){
        for (int i=0;i<bottomMusicTitleList.size();i++){
            bottomMusicTitleList.get(i).setText(title);
        }
    }



    public static void setSinger(String singer){
        for (int i=0;i<bottomMusicSingerList.size();i++){
            bottomMusicSingerList.get(i).setText(singer);
        }
    }
    public static void setImage(Bitmap bitmap){
        for (int i=0;i<bottomMusicImageList.size();i++){
            bottomMusicImageList.get(i).setImageBitmap(bitmap);
        }
    }




}
