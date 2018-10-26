package com.example.administrator.playaudiotest.activity;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.administrator.playaudiotest.bean.MyApplication;
import com.example.administrator.playaudiotest.R;
import com.example.administrator.playaudiotest.adapter.NetRecyclerAdapter;
import com.example.administrator.playaudiotest.service.NetMusic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerchActivity extends AppCompatActivity {

    public static final String CLOUD_MUSIC_API_MUSIC = "http://music.163.com/song/media/outer/url?id=";
    public static final String CLOUD_MUSIC_API_PREFIX = "http://s.music.163.com/search/get/?";
    //根据关键字搜索到的音乐列表
    private RecyclerView netMusicListView;
    private NetRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    //存放搜索到的音乐信息的list
    public static List<NetMusic> netMusicList = new ArrayList<>();
    private LayoutInflater inflater;
    private final String TAG = "SerchActivity";
    //获取的json数据格式的网络响应赋值给searchResponse
    private SearchView searchView;
    private String searchResponse = null;
    //缓存专辑图片的下载链接，当下载完成并播放歌曲时，点击播放图标加载专辑图标时避免重新联网搜索，直接从缓存中获取
    private Map<String, SoftReference<String>> picUrlMap = new HashMap<String, SoftReference<String>>();

    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }
        initView();
        initEvents();
    }

    private void initView(){
        adapter = new NetRecyclerAdapter(netMusicList);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        layoutManager = new LinearLayoutManager(this);
        searchView = (SearchView) findViewById(R.id.music_search_view);
        inflater = LayoutInflater.from(this);
        netMusicListView = (RecyclerView) findViewById(R.id.netmusiclist);
        netMusicListView.setLayoutManager(layoutManager);
        netMusicListView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.mine_own_item_devide));
        netMusicListView.addItemDecoration(dividerItemDecoration);
    }

    private void initEvents(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MyApplication.getContext(), "搜索内容为：" + query, Toast.LENGTH_SHORT).show();
                String musicUrl = getRealUrl(query);
                new SearchMusicTask(netMusicListView).execute(musicUrl);
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            public boolean onClose() {
                netMusicListView.setAdapter(null);
                // netMusicList.clear();adapter.notifyDataSetChanged();
                return false;
            }
        });
    }


//
//    // 网络音乐列表适配器
//    private BaseAdapter netMusicListAdapter = new BaseAdapter() {
//
//        @Override
//        public int getCount() {
//            return netMusicList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView,
//                            ViewGroup parent) {
//            View view = convertView;
//            Map<String, Object> item = netMusicList.get(position);
//            if (convertView == null) {
//                view = inflater.inflate(R.layout.music_item, null);
//            }
//            TextView musicTitle = (TextView) view.findViewById(R.id.music_name);
//            musicTitle.setTag("title");
//            TextView musicArtist = (TextView) view.findViewById(R.id.music_singer);
//            musicTitle.setText((String) item.get("title"));
//            musicArtist.setText((String) item.get("artist"));
//            return view;
//        }
//http://music.163.com/weapi/cloudsearch/get/web?csrf_token=0&s=%E5%A4%9C&limit=20&total:=true&offset=0&type=1
//    };

    //返回可以访问的网络资源
    private String getRealUrl(String query) {
        String key = null;
        try {
            key = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return CLOUD_MUSIC_API_PREFIX + "type=1&s='" + key
                + "'&limit=20&offset=0";
    }

    public void onDestroy() {
        super.onDestroy();
    }

    // 负责搜索音乐的异步任务，搜索完成后显示网络音乐列表
    private class SearchMusicTask extends AsyncTask<String, Void, Void> {
        private RecyclerView musicList;

        public SearchMusicTask(RecyclerView musicList) {
            this.musicList = musicList;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
                conn.setConnectTimeout(5000);
                //使用缓存提高处理效率
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                //网络响应赋值给成员变量searchResponse
                searchResponse = sb.toString();
                parseResponse();
                Log.d(TAG, "searchResponse = " + searchResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //adapter数据更新后通知列表更新
            adapter.notifyDataSetChanged();
            //musicList.setAdapter(netMusicListAdapter);
        }

        //json解析网络响应
        private void parseResponse() {
            try {
                JSONObject response = new JSONObject(searchResponse);
                JSONObject result = response.getJSONObject("result");
                JSONArray songs = result.getJSONArray("songs");
                if (netMusicList.size() > 0) netMusicList.clear();
                for (int i = 0; i < songs.length(); i++) {
                    JSONObject song = songs.getJSONObject(i);
                    //获取歌曲名字
                    String id = song.getString("id");
                    String name = song.getString("name");
                    String title = song.getJSONObject("album").getString(
                            "name");
                    //获取歌词演唱者
                    String artist = song.getJSONArray("artists")
                            .getJSONObject(0).getString("name");
                    //获取歌曲专辑图片的url
                    String albumPicUrl = song.getJSONObject("album").getString(
                            "picUrl");
                    //获取歌曲音频的url
                    String audioUrl = CLOUD_MUSIC_API_MUSIC + id + ".mp3";
                    Log.d(TAG, "doenloadUrl = " + audioUrl);
                    //保存音乐信息的Map
                    NetMusic music = new NetMusic(id,name,audioUrl,artist,albumPicUrl,title);
//                    Map<String, Object> item = new HashMap<>();
//                    item.put("title", title);
//                    item.put("artist", artist);
//                    item.put("picUrl", albumPicUrl);
//                    picUrlMap.put(title + artist, new SoftReference<String>(
//                            albumPicUrl));
//                    item.put("audio", audioUrl);
                    //将一条歌曲信息存入list中
                    netMusicList.add(music);
                }
                Log.d(TAG, "搜到" + netMusicList.size() + "首歌");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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


}








