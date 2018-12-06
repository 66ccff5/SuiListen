package com.example.administrator.playaudiotest.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.playaudiotest.other.MineMusicList;
import com.example.administrator.playaudiotest.bean.Music;
import com.example.administrator.playaudiotest.bean.PlayContent;
import com.example.administrator.playaudiotest.R;
import com.example.administrator.playaudiotest.adapter.MusicAdapter;

public class LocalMusicActivity extends CloudActivity {

    private RecyclerView music_list;
    LinearLayoutManager layoutManager;
    TextView bottomMusicTitle;
    TextView bottomMusicSinger;
    ImageView bottomMusicImage;
    ImageView playOrPause;
    LinearLayout pagerView;
    TextView musicMount;
    LinearLayout playAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.localmusic_toolbar);
        setSupportActionBar(toolbar);
        ActionBar acionBar = getSupportActionBar();
        if (acionBar != null) {
            acionBar.setDisplayHomeAsUpEnabled(true);
        }
        initViews();
        layoutManager = new LinearLayoutManager(this);
        music_list.setLayoutManager(layoutManager);
        music_list.setAdapter(MusicAdapter.CloudMusicAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.mine_own_item_devide));
        music_list.addItemDecoration(dividerItemDecoration);
        Music music = MusicAdapter.CloudMusicAdapter.nowPlay();

        musicMount.setText("(共" + MineMusicList.mineMusicList.musicList.size() + "首)");


        PlayContent.bottomMusicSingerList.add(bottomMusicSinger);
        PlayContent.bottomMusicTitleList.add(bottomMusicTitle);
        PlayContent.bottomMusicImageList.add(bottomMusicImage);
        PlayContent.bottomPlayOrPauseList.add(playOrPause);
        PlayContent.setTitle(music.getMusicTitle());
        PlayContent.setSinger(music.getMusicSinger());
        PlayContent.setImage(music.getMusicImage());
        if (CloudActivity.mmediaPlayer.isPlaying()) {
            for(int i=0;i<PlayContent.bottomPlayOrPauseList.size();i++){
                PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.pause_btn);
            }
        } else {
            for(int i=0;i<PlayContent.bottomPlayOrPauseList.size();i++){
                PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.play_btn);
            }
        }
        initBottomEvents();
    }

    public void initViews(){
        music_list = (RecyclerView) findViewById(R.id.music_view);
        musicMount = (TextView) findViewById(R.id.msuic_mount);
        pagerView = (LinearLayout) findViewById(R.id.play_bottom_linera_1);
        bottomMusicTitle = (TextView) findViewById(R.id.bottom_music_text_1);
        bottomMusicSinger = (TextView) findViewById(R.id.bottom_music_text_singer_1);
        bottomMusicImage = (ImageView) findViewById(R.id.bottom_music_image_1);
        playOrPause = (ImageView) findViewById(R.id.start_pause_1);
        playAll = (LinearLayout) findViewById(R.id.play_all_linear);
    }


    public void initBottomEvents() {

        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAdapter.CloudMusicAdapter.playAll();
            }
        });

        pagerView.setOnTouchListener(new View.OnTouchListener() {
            float mStart;
            float mEnd;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStart = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mEnd = motionEvent.getX();

                        if (mEnd < mStart) {
//                            if(PlayContent.playCont == false){
//                                CloudActivity.mmediaPlayer.reset();
//                                MusicAdapter.initMediaPlayer(MineMusicList.mineMusicList.musicList.get(0).getMusicPath());
//
//                            }else {
                            MusicAdapter.CloudMusicAdapter.playNextMusic();

//                            }
                            PlayContent.playCont = true;
//                            bottomMusicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTitle());
//                            bottomMusicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger());
//                            bottomMusicImage.setImageBitmap(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicImage());


                        } else if (mEnd > mStart) {
//                            if(PlayContent.playCont == false){
//                                CloudActivity.mmediaPlayer.reset();
//                                MusicAdapter.initMediaPlayer(MineMusicList.mineMusicList.musicList.get(0).getMusicPath());
//
//                            }else {
                            MusicAdapter.CloudMusicAdapter.playPreMusic();

//                            }
                            PlayContent.playCont = true;
//                            bottomMusicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTitle());
//                            bottomMusicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger());
//                            bottomMusicImage.setImageBitmap(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicImage());

                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        playOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CloudActivity.mmediaPlayer.isPlaying()) {
                    CloudActivity.mmediaPlayer.start();
                    PlayContent.playCont = true;
                    for (int i = 0; i < PlayContent.bottomPlayOrPauseList.size(); i++) {
                        PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.pause_btn);
                    }

                } else {
                    CloudActivity.mmediaPlayer.pause();
                    PlayContent.playCont = false;
                    for (int i = 0; i < PlayContent.bottomPlayOrPauseList.size(); i++) {
                        PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.play_btn);
                    }

                }

            }
        });


    }
}