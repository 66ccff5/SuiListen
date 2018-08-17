package com.example.administrator.playaudiotest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.administrator.playaudiotest.fragment.BottomMUsicFragment;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class Music implements Serializable{

    private String musicName;
    private String musicTitle;
    private String musicPath;
    private String musicTime;
    private String musicSinger;
    private Bitmap musicOldImage = null;
    private Bitmap musicImage = null;

    public void setMusicTime(String time){
        this.musicTime = time;
    }

    public String getMusicTime(){
        return musicTime;
    }

    public void setMusicSinger(String singer){
        this.musicSinger = singer;
    }

    public String getMusicSinger(){
        return musicSinger;
    }

    public void setMusicTitle(String title){
        this.musicTitle = title;
    }

    public String getMusicTitle(){
        return musicTitle;
    }

    public void setOldMusicImage(Bitmap image){
        this.musicOldImage = image;
    }

    public Bitmap getOldMusicImage(){
        return musicOldImage;
    }


    public void setMusicImage(Bitmap image){
        this.musicImage = image;
    }

    public Bitmap getMusicImage(){
        return musicImage;
    }


    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }


    public String getMusicName() {

        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }
}
