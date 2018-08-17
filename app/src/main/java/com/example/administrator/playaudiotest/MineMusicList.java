package com.example.administrator.playaudiotest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.storage.StorageManager;

import com.example.administrator.playaudiotest.fragment.BottomMUsicFragment;
import com.example.administrator.playaudiotest.fragment.FriendsFragment;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

public  class MineMusicList {
    public static MineMusicList mineMusicList = new MineMusicList();

    public  List<Music> musicList = new LinkedList<>();

    public static Music music_one;
    public static List<BottomMUsicFragment> bottomMusicFragment = new ArrayList<>();
    public Uri selectUri;
    public MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
    public byte[] artwork;
    int rawHeight;
    int rawWidth;
    int newHeight;
    int newWidth;
    float heightScale;
    float widthScale;
    Matrix matrix;
    String musicTitle;
    String musicSinger;
    String musicTime;





    public MineMusicList(){

    }

    public  void refeshMusicList(Activity activity){
        musicList.clear();
        String[] result = null;
        StorageManager storageManager = (StorageManager) activity.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method method = StorageManager.class.getMethod("getVolumePaths");
            method.setAccessible(true);
            try {
                result =(String[])method.invoke(storageManager);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < result.length; i++) {
                File file = new File(result[i] + "/netease");

                getAllFiles(file);

                System.out.println("path----> " + result[i]+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void getAllFiles(File path) {
        Music music;
        File files[] = path.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFiles(f);

                } else {
                    if (f.getName().endsWith(".mp3")) {
                        music = new Music();
                        music.setMusicName(f.getName().replace(".mp3",""));
                        music.setMusicPath(f.getPath());
                            selectUri = Uri.parse(f.getPath());
                            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
                            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                            musicTime = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            music.setMusicTitle(musicTitle);
                            music.setMusicSinger(musicSinger);

                            music.setMusicTime(musicTime);
                            artwork = metadataRetriever.getEmbeddedPicture();
                            if (artwork != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
                                rawHeight = bitmap.getHeight();
                                rawWidth = bitmap.getWidth();
                                newHeight = 120;
                                newWidth = 120;
                                heightScale = ((float) newHeight) / rawHeight;
                                widthScale = ((float) newWidth) / rawWidth;
                                matrix = new Matrix();
                                matrix.postScale(heightScale, widthScale);
                                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight, matrix, true);
                                music.setMusicImage(newBitmap);
                            }
                        musicList.add(music);

                    }
                }
            }
        }
    }







    }



