package com.example.administrator.playaudiotest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.playaudiotest.R;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MineRadioFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_radio_fragment, container, false);
        return view;
    }

}
