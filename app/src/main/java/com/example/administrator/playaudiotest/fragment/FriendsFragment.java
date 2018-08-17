package com.example.administrator.playaudiotest.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.playaudiotest.R;

/**
 * Created by Administrator on 2018/4/1 0001.
 */

public class FriendsFragment extends android.support.v4.app.Fragment {

    public static FriendsFragment friendsFragment = new FriendsFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        return view;
    }
}

