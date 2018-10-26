package com.example.administrator.playaudiotest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.playaudiotest.activity.CloudActivity;
import com.example.administrator.playaudiotest.R;
import com.example.administrator.playaudiotest.activity.SerchActivity;
import com.example.administrator.playaudiotest.service.NetMusic;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/6/11 0011.
 */

public class NetRecyclerAdapter extends RecyclerView.Adapter<NetRecyclerAdapter.ViewHolder> {

    private List<NetMusic> netList;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView musicName;
        TextView musicSinger;
        View v;

        public ViewHolder(View view){
            super(view);
            v = view;
            musicName = (TextView) view.findViewById(R.id.music_name);
            musicSinger = (TextView) view.findViewById(R.id.music_singer);
        }

    }

    public NetRecyclerAdapter(List<NetMusic> List){
        this.netList = List;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                NetMusic music = SerchActivity.netMusicList.get(position);
                try {
                    CloudActivity.mmediaPlayer.reset();
                    CloudActivity.mmediaPlayer.setDataSource(music.getMusicPath());
                    CloudActivity.mmediaPlayer.prepare();//prepare之后自动播放
                    //mediaPlayer.start();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("vip --->" + music.getMusicPath());
                CloudActivity.mmediaPlayer.start();

            }
        });
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NetMusic item = netList.get(position);
        holder.musicName.setText((String) item.getMusicName());
        holder.musicSinger.setText((String) item.getMusicSinger());
    }

    @Override
    public int getItemCount() {
        return netList.size();
    }
}
