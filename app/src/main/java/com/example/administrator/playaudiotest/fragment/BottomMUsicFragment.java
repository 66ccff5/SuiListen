package com.example.administrator.playaudiotest.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.playaudiotest.LaunchActivity;
import com.example.administrator.playaudiotest.MainActivity;
import com.example.administrator.playaudiotest.MineMusicList;
import com.example.administrator.playaudiotest.Music;
import com.example.administrator.playaudiotest.MyApplication;
import com.example.administrator.playaudiotest.R;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

public class BottomMUsicFragment extends android.support.v4.app.Fragment {

    private Music music;
    TextView textView;
    TextView singerView;
    ImageView imageView;
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
    public BottomMUsicFragment(){

    }

    public void setMusic(Music m){
        this.music = m;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_music_fragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.bottom_music_image);
        if(music.getMusicImage() == null){
        selectUri = Uri.parse(music.getMusicPath());
        metadataRetriever.setDataSource(MyApplication.getContext(),selectUri);
        musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        music.setMusicTitle(musicTitle);
        music.setMusicSinger(musicSinger);
        artwork = metadataRetriever.getEmbeddedPicture();
        if(artwork != null) {
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
        }
        imageView.setImageBitmap(music.getMusicImage());
        textView = (TextView) view.findViewById(R.id.bottom_music_text);
        singerView = (TextView) view.findViewById(R.id.bottom_music_text_singer);
        textView.setText(music.getMusicTitle());
        singerView.setText(music.getMusicSinger());
        return view;
    }




    public Music getMusic(){
        return music;
    }


}
