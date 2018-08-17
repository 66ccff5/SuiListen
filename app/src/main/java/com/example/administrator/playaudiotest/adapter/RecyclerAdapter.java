package com.example.administrator.playaudiotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.playaudiotest.R;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder>{

    private Context context;
    private LayoutInflater inflater;
    private String[] str;

    public RecyclerAdapter(String[] str, Context context) {
        this.str = str;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.music_item,parent,false);
        MyHolder holder=new MyHolder(view);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.musicName.setText(str[position]);
        holder.musicName.setTag(position);
//        //给瀑布流这是100到400的随机高度
//        int height= (int) (100+Math.random()*300);
//        ViewGroup.LayoutParams params=holder.tv_item.getLayoutParams();
//        holder.tv_item.setLayoutParams(params);
//        holder.tv_item.setText(str[position]);
    }

    @Override
    public int getItemCount() {
        return str.length;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        View musicView;
        TextView musicName;

        public MyHolder(View itemView) {
            super(itemView);
            musicView = itemView;
            musicName = (TextView) itemView.findViewById(R.id.music_name);

        }
    }
}

