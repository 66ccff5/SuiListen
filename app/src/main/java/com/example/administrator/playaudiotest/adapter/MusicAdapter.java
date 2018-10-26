package com.example.administrator.playaudiotest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.playaudiotest.activity.CloudActivity;
import com.example.administrator.playaudiotest.other.MineMusicList;
import com.example.administrator.playaudiotest.bean.Music;
import com.example.administrator.playaudiotest.activity.MusicActivity;
import com.example.administrator.playaudiotest.bean.MyApplication;
import com.example.administrator.playaudiotest.bean.PlayContent;
import com.example.administrator.playaudiotest.R;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    private Music musicTest = null;
    private Context mContext;
    private List<Music> mMusicList;
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
    private int playModel = 0;
    private int position = 0;

    private int t = 0;
    LayoutInflater inflater;
    public static MusicAdapter CloudMusicAdapter;
    private ViewGroup contentView;




    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicView;
        TextView musicName;
        TextView musicSinger;



       public ViewHolder(View v){
            super(v);
            musicView = v;
            musicName = (TextView) v.findViewById(R.id.music_name);
            musicSinger = (TextView) v.findViewById(R.id.music_singer);


        }
    }

    public void setPlayModel(int i){
        this.playModel = i;
    }

    public int getPlayModel(){
        return playModel;
    }

    public MusicAdapter(Context context, List<Music> musicList,ViewGroup viewGroup)
    {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.mMusicList = musicList;
        this.contentView = viewGroup;

        Music music = mMusicList.get(position);
        musicTest = music;
        initMediaPlayer(musicTest.getMusicPath());
        if (musicTest.getMusicImage() == null) {
            selectUri = Uri.parse(musicTest.getMusicPath());
            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            musicTest.setMusicTitle(musicTitle);
            musicTest.setMusicSinger(musicSinger);
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
                musicTest.setMusicImage(newBitmap);
            }
        }
        PlayContent.setTitle(musicTest.getMusicTitle());
        PlayContent.setImage(musicTest.getMusicImage());
        PlayContent.setSinger(musicTest.getMusicSinger());


    }

    public static void initMediaPlayer(String path){
        try{
            CloudActivity.mmediaPlayer.setDataSource(path);
            CloudActivity.mmediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.music_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);



        holder.musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = holder.getAdapterPosition();
                Music music = mMusicList.get(position);

                if(musicTest == null){
                    musicTest = music;
                    if (musicTest.getMusicImage() == null) {
                        selectUri = Uri.parse(musicTest.getMusicPath());
                        metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
                        musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        musicTest.setMusicTitle(musicTitle);
                        musicTest.setMusicSinger(musicSinger);
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
                            musicTest.setMusicImage(newBitmap);
                        }
                    }

                    initMediaPlayer(musicTest.getMusicPath());
                    CloudActivity.mmediaPlayer.start();
                    PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);


                }else {


                    if (music.getMusicName().equals(musicTest.getMusicName())) {
                        if (!CloudActivity.mmediaPlayer.isPlaying()) {
                            CloudActivity.mmediaPlayer.start();
                            PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
                        } else {
                            CloudActivity.mmediaPlayer.pause();
                            PlayContent.playOrpause.setImageResource(R.drawable.play_btn);
                        }

                    } else {

                        CloudActivity.mmediaPlayer.reset();
                            musicTest = music;
                        if (musicTest.getMusicImage() == null) {
                            selectUri = Uri.parse(musicTest.getMusicPath());
                            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
                            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                            musicTest.setMusicTitle(musicTitle);
                            musicTest.setMusicSinger(musicSinger);
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
                                musicTest.setMusicImage(newBitmap);
                            }
                        }
                        initMediaPlayer(musicTest.getMusicPath());
                        CloudActivity.mmediaPlayer.start();
                        PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);

                    }

                }
                PlayContent.setTitle(musicTest.getMusicTitle());
                PlayContent.setImage(musicTest.getMusicImage());
                PlayContent.setSinger(musicTest.getMusicSinger());
                if(MusicActivity.activity != null) {
                    MusicActivity.activity.changeMusic();
                }


            }
        });

        return holder;
    }

    public void playMusicLoop(){
        CloudActivity.mmediaPlayer.reset();
//        position = (position + 1) % mMusicList.size();
//        Music music = MineMusicList.mineMusicList.musicList.get(position);
//        musicTest = music;
        MusicAdapter.initMediaPlayer(musicTest.getMusicPath());
        CloudActivity.mmediaPlayer.start();
        PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
//        if (music.getMusicImage() == null) {
//            selectUri = Uri.parse(music.getMusicPath());
//            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
//            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//            musicTest.setMusicTitle(musicTitle);
//            musicTest.setMusicSinger(musicSinger);
//            artwork = metadataRetriever.getEmbeddedPicture();
//            if (artwork != null) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
//                rawHeight = bitmap.getHeight();
//                rawWidth = bitmap.getWidth();
//                newHeight = 120;
//                newWidth = 120;
//                heightScale = ((float) newHeight) / rawHeight;
//                widthScale = ((float) newWidth) / rawWidth;
//                matrix = new Matrix();
//                matrix.postScale(heightScale, widthScale);
//                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight, matrix, true);
//                musicTest.setMusicImage(newBitmap);
//            }
//        }
//        PlayContent.setTitle(musicTest.getMusicTitle());
//        PlayContent.setImage(musicTest.getMusicImage());
//        PlayContent.setSinger(musicTest.getMusicSinger());
    }

    public void playRandom(){
        CloudActivity.mmediaPlayer.reset();
        position = (int)(Math.random()*MineMusicList.mineMusicList.musicList.size());
        Music music = MineMusicList.mineMusicList.musicList.get(position);
        musicTest = music;
        MusicAdapter.initMediaPlayer(musicTest.getMusicPath());
        CloudActivity.mmediaPlayer.start();
        PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
        if (music.getMusicImage() == null) {
            selectUri = Uri.parse(music.getMusicPath());
            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            musicTest.setMusicTitle(musicTitle);
            musicTest.setMusicSinger(musicSinger);
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
                musicTest.setMusicImage(newBitmap);
            }
        }
        PlayContent.setTitle(musicTest.getMusicTitle());
        PlayContent.setImage(musicTest.getMusicImage());
        PlayContent.setSinger(musicTest.getMusicSinger());
    }



    public void playNextMusic() {
        CloudActivity.mmediaPlayer.reset();
        position = (position + 1) % mMusicList.size();
        Music music = MineMusicList.mineMusicList.musicList.get(position);
        musicTest = music;
        MusicAdapter.initMediaPlayer(musicTest.getMusicPath());
        CloudActivity.mmediaPlayer.start();
        PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
        if (music.getMusicImage() == null) {
            selectUri = Uri.parse(music.getMusicPath());
            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            musicTest.setMusicTitle(musicTitle);
            musicTest.setMusicSinger(musicSinger);
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
                musicTest.setMusicImage(newBitmap);
            }
        }
        PlayContent.setTitle(musicTest.getMusicTitle());
        PlayContent.setImage(musicTest.getMusicImage());
        PlayContent.setSinger(musicTest.getMusicSinger());
    }


    public Music nowPlay(){
        return MineMusicList.mineMusicList.musicList.get(position);
    }


    public void playPreMusic(){
        CloudActivity.mmediaPlayer.reset();
        position = (mMusicList.size() + position - 1) % mMusicList.size();
        Music music = MineMusicList.mineMusicList.musicList.get(position);
        musicTest = music;
        MusicAdapter.initMediaPlayer(musicTest.getMusicPath());
        CloudActivity.mmediaPlayer.start();
        PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
        if (musicTest.getMusicImage() == null) {
            selectUri = Uri.parse(musicTest.getMusicPath());
            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            musicTest.setMusicTitle(musicTitle);
            musicTest.setMusicSinger(musicSinger);
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
                musicTest.setMusicImage(newBitmap);
            }
        }
        PlayContent.setTitle(musicTest.getMusicTitle());
        PlayContent.setImage(musicTest.getMusicImage());
        PlayContent.setSinger(musicTest.getMusicSinger());
    }

    public void playAll(){
        CloudActivity.mmediaPlayer.reset();
        position = 0;
        musicTest = MineMusicList.mineMusicList.musicList.get(position);
        initMediaPlayer(musicTest.getMusicPath());
        CloudActivity.mmediaPlayer.start();
        PlayContent.playOrpause.setImageResource(R.drawable.pause_btn);
        if (musicTest.getMusicImage() == null) {
            selectUri = Uri.parse(musicTest.getMusicPath());
            metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
            musicTitle = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            musicSinger = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            musicTest.setMusicTitle(musicTitle);
            musicTest.setMusicSinger(musicSinger);
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
                musicTest.setMusicImage(newBitmap);
            }
        }
        PlayContent.setTitle(musicTest.getMusicTitle());
        PlayContent.setImage(musicTest.getMusicImage());
        PlayContent.setSinger(musicTest.getMusicSinger());
        for(int i=0;i<PlayContent.bottomPlayOrPauseList.size();i++){
            PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.pause_btn);
        }

    }





    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = mMusicList.get(position);
        holder.musicName.setText(music.getMusicTitle());
        holder.musicSinger.setText(music.getMusicSinger());
    }




    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

}
