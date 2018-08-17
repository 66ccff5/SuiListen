package com.example.administrator.playaudiotest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.storage.StorageManager;
import android.service.notification.NotificationListenerService;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.playaudiotest.adapter.CloudPagerAdapter;
import com.example.administrator.playaudiotest.adapter.MusicAdapter;
import com.example.administrator.playaudiotest.fragment.BottomMUsicFragment;
import com.example.administrator.playaudiotest.fragment.FriendsFragment;
import com.example.administrator.playaudiotest.fragment.MineFragment;
import com.example.administrator.playaudiotest.fragment.MusicFragment;
import com.example.administrator.playaudiotest.service.BottomPlayService;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends CloudActivity implements View.OnClickListener   {

    private DrawerLayout mDrawerLayout;

    private List<Music> musicList = new LinkedList<>();
    private MusicAdapter adapter;

    private ViewPager cloudViewPager;
  //  public static ViewPager bottomViewPager;
    private List<Fragment> cloudFragments = new ArrayList<Fragment>();
    private FragmentPagerAdapter cloudPagerAdapterr;
   // public  FragmentPagerAdapter bottomPagerAdapter;
    private LinearLayout toolMusicLayout,toolFriendsLayout,toolDiscoverLayout;
    private ImageView toolMusic,toolFriends,toolDiscover;
    TextView bottomMusicTitle;
    TextView bottomMusicSinger;
    ImageView bottomMusicImage;
    ImageView searchView;
    public static ViewGroup bottomView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomMusicTitle= (TextView) findViewById(R.id.bottom_music_text);
        bottomMusicSinger = (TextView) findViewById(R.id.bottom_music_text_singer);
        bottomMusicImage = (ImageView) findViewById(R.id.bottom_music_image);
        PlayContent.bottomMusicImageList.add(bottomMusicImage);
        PlayContent.bottomMusicTitleList.add(bottomMusicTitle);
        PlayContent.bottomMusicSingerList.add(bottomMusicSinger);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar acionBar = getSupportActionBar();
        if (acionBar != null){
            acionBar.setDisplayHomeAsUpEnabled(true);
            acionBar.setHomeAsUpIndicator(R.drawable.actionbar_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        CloudActivity.mmediaPlayer.setOnCompletionListener(this);
        initView();
        initEvents();
        initDatas();

        initBottomView();
        initBottomEvents();


    }


    // 5.0版本以上
    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    // 4.4 - 5.0版本
    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }

    private int getStatusBarHeight(){
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId>0){
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }





    private void initDatas(){
        cloudFragments = new ArrayList<>();
        cloudFragments.add(new MusicFragment());
        cloudFragments.add(new MineFragment());
        cloudFragments.add(new FriendsFragment());

        cloudPagerAdapterr = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }

            @Override
            public Fragment getItem(int position) {

//                if(cloudFragments.get(position) == null)

                return cloudFragments.get(position);
            }

            @Override
            public int getCount() {
                return cloudFragments.size();
            }
        };

        cloudViewPager.setAdapter(cloudPagerAdapterr);
        cloudViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                cloudViewPager.setCurrentItem(position);
                resetImageResource();
                selectTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initEvents(){

        toolMusicLayout.setOnClickListener(this);
        toolDiscoverLayout.setOnClickListener(this);
        toolFriendsLayout.setOnClickListener(this);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SerchActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    private void initView(){
        searchView = (ImageView) findViewById(R.id.tool_search);
        cloudViewPager = (ViewPager) findViewById(R.id.view_pager);
        toolMusicLayout = (LinearLayout) findViewById(R.id.toolbar_music_layout);
        toolDiscoverLayout = (LinearLayout) findViewById(R.id.toolbar_discover_layout);
        toolFriendsLayout = (LinearLayout) findViewById(R.id.toolbar_friends_layout);

        toolMusic = (ImageView) findViewById(R.id.toolbar_music);
        toolDiscover = (ImageView) findViewById(R.id.toolbar_discover);
        toolFriends = (ImageView) findViewById(R.id.toolbar_friends);

    }







    private void getAllFiles(File path) {
        Music music;
        File files[] = path.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        getAllFiles(f);

                    } else {
                        if (f.getName().endsWith(".mp3")) {
                            music = new Music();
                            music.setMusicName(f.getName());
                            music.setMusicPath(f.getPath());
                            musicList.add(music);
                        }
                    }
                }
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
     //   getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                break;
           case R.id.tool_search:
                Intent intent = new Intent(MainActivity.this, SerchActivity.class);
                startActivity(intent);
                break;
            default:

        }
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }


    @Override
    public void onClick(View view) {
        resetImageResource();
        switch (view.getId()){
            case R.id.toolbar_music_layout:
                selectTab(0);
                break;
            case R.id.toolbar_discover_layout:
               selectTab(1);
                break;
            case R.id.toolbar_friends_layout:
               selectTab(2);
                break;
            default:
                break;

        }
    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                toolMusic.setImageResource(R.drawable.actionbar_music_selected);
                break;
            case 1:
                toolDiscover.setImageResource(R.drawable.actionbar_discover_selected);
                break;
            case 2:
                toolFriends.setImageResource(R.drawable.actionbar_friends_selected);
                break;
            default:
                break;

        }
        //设置当前点击的Tab所对应的页面
        cloudViewPager.setCurrentItem(i);
    }





    private void resetImageResource(){
        toolMusic.setImageResource(R.drawable.actionbar_music_normal);
        toolDiscover.setImageResource(R.drawable.actionbar_discover_normal);
        toolFriends.setImageResource(R.drawable.actionbar_friends_normal);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch (MusicAdapter.CloudMusicAdapter.getPlayModel()){
            case 0:
                MusicAdapter.CloudMusicAdapter.playNextMusic();
                break;
            case 1:
                MusicAdapter.CloudMusicAdapter.playMusicLoop();
                break;
            case 2:
                MusicAdapter.CloudMusicAdapter.playRandom();
                break;
            default:
                break;
        }
    }

    public void initBottomEvents(){

        PlayContent.bottomPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MusicActivity.class);
                startActivity(intent);
            }
        });


        PlayContent.bottomPager.setOnTouchListener(new View.OnTouchListener() {

            float mStart;
            float mEnd;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mStart =  motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mEnd = motionEvent.getX();
                            if((mEnd - mStart) > 10 || (mEnd - mStart) < -10) {
                                if (mEnd < mStart) {
                                    MusicAdapter.CloudMusicAdapter.playNextMusic();
                                    PlayContent.playCont = true;
                                } else if (mEnd > mStart) {
                                    MusicAdapter.CloudMusicAdapter.playPreMusic();
                                    PlayContent.playCont = true;
                                }
                            }
                        break;
                    default:
                        break;
                }
                if((mEnd - mStart) > 100 || (mEnd - mStart) < -100) {
                    return true;
                }else {
                    return false;
                }

            }
        });




        PlayContent.playOrpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CloudActivity.mmediaPlayer.isPlaying()) {
                    CloudActivity.mmediaPlayer.start();
                    PlayContent.playCont = true;
                    for(int i=0;i<PlayContent.bottomPlayOrPauseList.size();i++){
                        PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.pause_btn);
                    }
                }else {
                    CloudActivity.mmediaPlayer.pause();
                    PlayContent.playCont = false;
                    for(int i=0;i<PlayContent.bottomPlayOrPauseList.size();i++){
                        PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.play_btn);
                    }

                }

            }
        });
    }






    public void initBottomView(){
        PlayContent.playOrpause = (ImageView) findViewById(R.id.start_pause);
        PlayContent.bottomPlayOrPauseList.add(PlayContent.playOrpause);
        PlayContent.bottomPager = (LinearLayout) findViewById(R.id.pager_bottom);
        bottomView = (ViewGroup) findViewById(R.id.play_bottom_linera);

    }




}
