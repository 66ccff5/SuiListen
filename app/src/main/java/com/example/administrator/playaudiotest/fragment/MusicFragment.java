package com.example.administrator.playaudiotest.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.playaudiotest.CloudActivity;
import com.example.administrator.playaudiotest.MainActivity;
import com.example.administrator.playaudiotest.MineMusicList;
import com.example.administrator.playaudiotest.R;
import com.example.administrator.playaudiotest.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/1 0001.
 */

public class MusicFragment extends android.support.v4.app.Fragment{

    private RecyclerView music_list;
    private SwipeRefreshLayout refreshLayout;
    LinearLayoutManager layoutManager;
    View view;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    MusicAdapter.CloudMusicAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }

        }
    };

    public MusicFragment(){

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_fragment, container, false);

        music_list = (RecyclerView) view.findViewById(R.id.music_view);
        layoutManager = new LinearLayoutManager(view.getContext());
        music_list.setLayoutManager(layoutManager);
        MusicAdapter.CloudMusicAdapter = new MusicAdapter(view.getContext(), MineMusicList.mineMusicList.musicList, MainActivity.bottomView);
        music_list.setAdapter(MusicAdapter.CloudMusicAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(view.getContext(),R.drawable.mine_own_item_devide));
        music_list.addItemDecoration(dividerItemDecoration);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_recycler);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MineMusicList.mineMusicList.refeshMusicList(getActivity());
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }).start();
            }

        });
        return view;
    }









}
