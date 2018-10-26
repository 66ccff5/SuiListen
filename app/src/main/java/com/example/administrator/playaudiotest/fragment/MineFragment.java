package com.example.administrator.playaudiotest.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.playaudiotest.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/1 0001.
 */

public class MineFragment extends android.support.v4.app.Fragment{

    private static int TAB_MARGIN_DIP = 11;

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private List<android.support.v4.app.Fragment> fragmentList;
    private MyAdapter adapter;
    private String[] titles = {"发现","我的","电台"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.mine_fragment_view_pager);
        tableLayout = (TabLayout) view.findViewById(R.id.mine_fragment_tab);
        fragmentList = new ArrayList<>();
        fragmentList.add(new MineFoundFragment());
        fragmentList.add(new MineOwnFragment());
        fragmentList.add(new MineRadioFragment());
        adapter = new MyAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.setTabTextColors(getResources().getColor(R.color.colorText),getResources().getColor(R.color.colorPrimary));
        setIndicator(getContext(),tableLayout,TAB_MARGIN_DIP,TAB_MARGIN_DIP);
        return view;
    }

    public static void setIndicator(Context context,TabLayout tableLayout,int leftDip,int rightDip){
        Class<?> tabLayout = tableLayout.getClass();
        Field tabStrip = null;
        try{
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try{
            ll_tab = (LinearLayout) tabStrip.get(tableLayout);
        }catch (Exception e){
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for(int i = 0;i < ll_tab.getChildCount();i++){
            View child = ll_tab.getChildAt(i);
            child.setPadding(0,0,0,0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }



    class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
