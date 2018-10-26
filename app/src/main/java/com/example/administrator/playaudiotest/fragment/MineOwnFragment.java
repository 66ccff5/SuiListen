package com.example.administrator.playaudiotest.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.playaudiotest.activity.LocalMusicActivity;
import com.example.administrator.playaudiotest.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MineOwnFragment extends android.support.v4.app.Fragment {
    ExpandableListView expandableListView;
    List<String> group;
    List<List<String>> child;
    LinearLayout localMusic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_own_fragment, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.mine_own_expandableListView);
        localMusic = (LinearLayout) view.findViewById(R.id.own_local_music);
        group = new ArrayList<>();
        group.add("创建的歌单");
        group.add("收藏的歌单");
        child = new ArrayList<>();
        List<String> citem1 = new ArrayList<>();
        List<String> citem2 = new ArrayList<>();
        citem1.add("歌单1");
        citem1.add("歌单2");
        citem2.add("歌单3");
        citem2.add("歌单4");
        citem2.add("歌单5");
        child.add(citem1);
        child.add(citem2);
        SongListExpandableAdapter songListExpandableAdapter = new SongListExpandableAdapter(getActivity());
        expandableListView.setAdapter(songListExpandableAdapter);

        localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), LocalMusicActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public class SongListExpandableAdapter extends BaseExpandableListAdapter {

        Activity activity;

        public  SongListExpandableAdapter(Activity a)
        {
            activity = a;
        }
        public  Object getChild(int  groupPosition, int  childPosition)
        {
            return  child.get(groupPosition).get(childPosition);
        }
        public  long  getChildId(int  groupPosition, int  childPosition)
        {
            return  childPosition;
        }
        public  int  getChildrenCount(int  groupPosition)
        {
            return  child.get(groupPosition).size();
        }
        public  View getChildView(int  groupPosition, int  childPosition,
                                  boolean  isLastChild, View convertView, ViewGroup parent)
        {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.song_list_created_item,null);
            }
            convertView.setTag(R.layout.song_list_created,groupPosition);
            convertView.setTag(R.layout.song_list_created_item,childPosition);
            TextView textView = (TextView) convertView.findViewById(R.id.song_list_item);
            textView.setText(child.get(groupPosition).get(childPosition));
            return convertView;
        }
        // group method stub
        public  Object getGroup(int  groupPosition)
        {
            return  group.get(groupPosition);
        }
        public  int  getGroupCount()
        {
            return  group.size();
        }
        public  long  getGroupId(int  groupPosition)
        {
            return  groupPosition;
        }
        public  View getGroupView(int  groupPosition, boolean  isExpanded,
                                  View convertView, ViewGroup parent)
        {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.song_list_created,null);
            }
            convertView.setTag(R.layout.song_list_created,groupPosition);
            convertView.setTag(R.layout.song_list_created_item,-1);
            TextView textView = (TextView) convertView.findViewById(R.id.song_list_name);
            textView.setText(group.get(groupPosition));
            return convertView;

        }

        public  boolean  hasStableIds()
        {
            return  false ;
        }
        public  boolean  isChildSelectable(int  groupPosition, int  childPosition)
        {
            return  true ;
        }
    }




}
