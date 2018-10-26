package com.example.administrator.playaudiotest.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.playaudiotest.view.AutoMarqueeTextView;
import com.example.administrator.playaudiotest.other.ListPop;
import com.example.administrator.playaudiotest.other.MyAnimatorUpdateListener;
import com.example.administrator.playaudiotest.bean.MyApplication;
import com.example.administrator.playaudiotest.bean.PlayContent;
import com.example.administrator.playaudiotest.R;
import com.example.administrator.playaudiotest.adapter.MusicAdapter;

import zhangyf.vir56k.androidimageblur.BlurUtil;

public class MusicActivity extends CloudActivity {

    LinearLayout backgroup;

    public Uri selectUri;
    public MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
    public byte[] artwork;
    int rawHeight;
    int rawWidth;
    int newHeight;
    int newWidth;
    float heightScale;
    float widthScale;
    Bitmap newBitmap = null;
    Bitmap roundBitmap = null;
    Bitmap bitmap = null;
    BitmapDrawable drawable = null;
    android.support.v7.widget.Toolbar toolbar;
    AutoMarqueeTextView musicTitle;
    TextView musicSinger;
    ImageView playDisc;
    MyAnimatorUpdateListener listener;
    ImageView playOrPause;
    ImageView playNext;
    ImageView playPre;
    SeekBar musicSeek;
    ObjectAnimator animation1;
    ImageView playNeedle;
    ImageView playNeedlePau;
    Bitmap playneedleImage;
    Bitmap playneedleH;
    TextView musicMaxTime;
    TextView musicMInTime;
    Handler handler;
    Runnable updateThread;
    ImageView playSrc;
    ImageView playModel;
    final int listLoop = 0;
    final int musicLoop = 1;
    final int playRandom = 2;
    float alpha = 1;
    Handler mhandler;
    private RecyclerView popList = null;
    private LinearLayoutManager layoutManager = null;
    private LinearLayout popLin;
    PopupWindow window;
    public static MusicActivity activity = null;




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }
        activity = this;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.activity_music_toolbar);
        setSupportActionBar(toolbar);
        initView();
        selectUri = Uri.parse(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicPath());
        metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
        artwork = metadataRetriever.getEmbeddedPicture();
        if (artwork != null) {
            bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            rawHeight = bitmap.getHeight();
            rawWidth = bitmap.getWidth();
            newHeight = rawHeight;
            newWidth = rawWidth / 2;
            newBitmap = Bitmap.createBitmap(bitmap, rawWidth/4, 0, newWidth, newHeight);
            roundBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight);
        }
        setPlayRoundDisc(roundBitmap);

        musicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicName());
        musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());


        CloudActivity.mmediaPlayer.setOnCompletionListener(this);

        musicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger() + ">");
        bitmap = BlurUtil.doBlur(newBitmap,12,20);
        drawable = new BitmapDrawable(getResources(),bitmap);
        backgroup.setBackground(drawable);
        LinearInterpolator lin = new LinearInterpolator();
        animation1 = ObjectAnimator.ofFloat(playDisc,"rotation",0f,360f);
        animation1.setDuration(15000);
        animation1.setInterpolator(lin);
        animation1.setRepeatMode(Animation.RESTART);
        animation1.setRepeatCount(-1);
        listener = new MyAnimatorUpdateListener(animation1);
        animation1.start();
        animation1.pause();
        if(CloudActivity.mmediaPlayer.isPlaying()){
            animation1.resume();
            handler.post(updateThread);
        }else {
            playNeedle.setVisibility(View.INVISIBLE);
            playNeedlePau.setVisibility(View.VISIBLE);
            handler.removeCallbacks(updateThread);
        }
        initEvents();


    }

    public void changeMusic(){
        playNeedle.setVisibility(View.VISIBLE);
        playNeedlePau.setVisibility(View.INVISIBLE);
        musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());
        musicMaxTime.setText(converLongTimeToStr(Long.parseLong(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTime())));
        musicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicName());
        musicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger() + ">");
        selectUri = Uri.parse(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicPath());
        metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
        artwork = metadataRetriever.getEmbeddedPicture();
        if (artwork != null) {
            bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            rawHeight = bitmap.getHeight();
            rawWidth = bitmap.getWidth();
            newHeight = rawHeight;
            newWidth = rawWidth / 2;
            heightScale = ((float) newHeight) / rawHeight;
            widthScale = ((float) newWidth) / rawWidth;
            newBitmap = Bitmap.createBitmap(bitmap, rawWidth/4, 0, newWidth, newHeight);
            roundBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight);
        }
        bitmap = BlurUtil.doBlur(newBitmap,12,20);
        drawable = new BitmapDrawable(getResources(),bitmap);
        backgroup.setBackground(drawable);
        setPlayRoundDisc(roundBitmap);
        animation1.end();
        animation1.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch (MusicAdapter.CloudMusicAdapter.getPlayModel()){
            case 0:
                MusicAdapter.CloudMusicAdapter.playNextMusic();
                musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());
                playNormal();
                break;
            case 1:
                MusicAdapter.CloudMusicAdapter.playMusicLoop();
                musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());
                break;
            case 2:
                MusicAdapter.CloudMusicAdapter.playRandom();
                musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());
                playNormal();
                break;
            default:
                break;
        }


    }

    private void playNormal(){
        playNeedle.setVisibility(View.VISIBLE);
        playNeedlePau.setVisibility(View.INVISIBLE);
        musicMaxTime.setText(converLongTimeToStr(Long.parseLong(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTime())));
        musicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicName());
        musicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger() + ">");
        selectUri = Uri.parse(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicPath());
        metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
        artwork = metadataRetriever.getEmbeddedPicture();
        if (artwork != null) {
            bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            rawHeight = bitmap.getHeight();
            rawWidth = bitmap.getWidth();
            newHeight = rawHeight;
            newWidth = rawWidth / 2;
            heightScale = ((float) newHeight) / rawHeight;
            widthScale = ((float) newWidth) / rawWidth;
            newBitmap = Bitmap.createBitmap(bitmap, rawWidth/4, 0, newWidth, newHeight);
            roundBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight);
        }
        bitmap = BlurUtil.doBlur(newBitmap,12,20);
        drawable = new BitmapDrawable(getResources(),bitmap);
        backgroup.setBackground(drawable);
        setPlayRoundDisc(roundBitmap);
        animation1.end();
        animation1.start();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        getWindow().setAttributes(lp); //act 是上下文context

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(View view) {

        window = new ListPop(this);
        window.showAtLocation(view, Gravity.BOTTOM,0,0);

//        View popupView = MusicActivity.this.getLayoutInflater().inflate(R.layout.pop_window, null);
//
//
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//
//        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
//        window = new PopupWindow(popupView,width, height / 5 * 3);
//        // TODO: 2016/5/17 设置动画
//        window.setAnimationStyle(R.style.popup_window_anim);
//        // TODO: 2016/5/17 设置背景颜色
//        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
//        // TODO: 2016/5/17 设置可以获取焦点
//        window.setFocusable(true);
//        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
//        window.setOutsideTouchable(true);
//        // TODO：更新popupwindow的状态
//        window.update();
//        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
////        window.showAsDropDown(view, 0, 0,Gravity.BOTTOM);
//
//        window.showAtLocation(view, Gravity.BOTTOM,0,0);
        new Thread(new Runnable() {
            @Override
            public void run() {

                    while(alpha>0.5f){
                        try {
                            //4是根据弹出动画时间和减少的透明度计算
                            Thread.sleep(8);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg =mhandler.obtainMessage();
                        msg.what = 1;
                        //每次减少0.01，精度越高，变暗的效果越流畅
                        alpha-=0.01f;
                        msg.obj =alpha ;
                        mhandler.sendMessage(msg);

                }
            }
        }).start();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            //此处while的条件alpha不能<= 否则会出现黑屏
                            while(alpha<1f){
                                try {
                                    Thread.sleep(4);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("headportrait","alpha:"+alpha);
                                Message msg =mhandler.obtainMessage();
                                msg.what = 1;
                                alpha+=0.01f;
                                msg.obj =alpha ;
                                mhandler.sendMessage(msg);
                        }
                    }
                }).start();
            }

        });
    }










    private void setPlayRoundDisc(Bitmap bm){
        RoundedBitmapDrawable drawable1 = RoundedBitmapDrawableFactory.create(getResources(),BitmapFactory.decodeResource(getResources(),R.drawable.play_disc));
        drawable1.setCircular(true);
        drawable1.setAntiAlias(true);
        RoundedBitmapDrawable drawable2 = RoundedBitmapDrawableFactory.create(getResources(),bm);
        drawable2.setCircular(true);
        drawable2.setAntiAlias(true);
        Drawable[] layerrs = new Drawable[2];
        layerrs[0] = drawable2;
        layerrs[1] = drawable1;
        LayerDrawable layerDrawable = new LayerDrawable(layerrs);
        layerDrawable.setLayerInset(0,drawable1.getIntrinsicWidth() / 6,drawable1.getIntrinsicWidth() / 6,drawable1.getIntrinsicWidth() / 6,drawable1.getIntrinsicWidth() / 6);
        playDisc.setImageDrawable(layerDrawable);
    }


    private void initView(){
        playOrPause = (ImageView) findViewById(R.id.music_activity_play_or_pause);
        musicSinger = (TextView) findViewById(R.id.music_activity_singer);
        playNext = (ImageView) findViewById(R.id.music_activity_play_next);
        musicTitle = (AutoMarqueeTextView) findViewById(R.id.music_title);
        playPre = (ImageView) findViewById(R.id.music_activity_play_pre);
        backgroup = (LinearLayout) findViewById(R.id.music_activity_back);
        playDisc = (ImageView) findViewById(R.id.play_disc);
        popLin = (LinearLayout) LayoutInflater.from(MusicActivity.this).inflate(R.layout.pop_window, null).findViewById(R.id.play_model_pop);
        popList =(RecyclerView) LayoutInflater.from(MusicActivity.this).inflate(R.layout.pop_window, null).findViewById(R.id.pop_music_list_view);
        playNeedle = (ImageView) findViewById(R.id.play_needle);
        playModel = (ImageView) findViewById(R.id.music_play_model);
        musicSeek = (SeekBar) findViewById(R.id.music_seekbar);
        playNeedlePau = (ImageView) findViewById(R.id.play_needle_pau);
        playSrc = (ImageView) findViewById(R.id.play_src);
        layoutManager = new LinearLayoutManager(MusicActivity.this);
        playneedleImage = BitmapFactory.decodeResource(getResources(),R.drawable.play_needle);
        playneedleH = adjustPhotoRotation(playneedleImage,-30);
        playNeedlePau.setImageBitmap(playneedleH);
        layoutManager = new LinearLayoutManager(this);
        popList.setLayoutManager(layoutManager);
        popList.setAdapter(MusicAdapter.CloudMusicAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.mine_own_item_devide));
        popList.addItemDecoration(dividerItemDecoration);
        musicMaxTime = (TextView) findViewById(R.id.music_time_max);
        musicMaxTime.setText(converLongTimeToStr(Long.parseLong(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTime())));
        musicMInTime = (TextView) findViewById(R.id.music_time_min);
        handler = new Handler();
        updateThread = new Runnable(){
            public void run() {
                //获得歌曲现在播放位置并设置成播放进度条的值
                musicSeek.setProgress(CloudActivity.mmediaPlayer.getCurrentPosition());
                musicMInTime.setText(converLongTimeToStr(CloudActivity.mmediaPlayer.getCurrentPosition()));
                //每次延迟100毫秒再启动线程
                handler.postDelayed(updateThread, 500);

            }
        };
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        backgroundAlpha((float)msg.obj);
                        break;
                }
            }
        };

    }


    public static String converLongTimeToStr(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;

        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = (time - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return strMinute + ":" + strSecond;
        }
    }

    private Bitmap adjustPhotoRotation(Bitmap bm,int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, 0, 0);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);
        m.postTranslate(20, 20);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);


        return bm1;
    }




    private void initEvents(){


        playOrPause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (!CloudActivity.mmediaPlayer.isPlaying()) {
                    CloudActivity.mmediaPlayer.start();
                    animation1.resume();
                    handler.post(updateThread);
                    PlayContent.playCont = true;
                    playNeedle.setVisibility(View.VISIBLE);
                    playNeedlePau.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < PlayContent.bottomPlayOrPauseList.size(); i++) {
                        PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.pause_btn);
                    }

                } else {
                    CloudActivity.mmediaPlayer.pause();
                    animation1.pause();
                    handler.removeCallbacks(updateThread);
                    playNeedle.setVisibility(View.INVISIBLE);
                    playNeedlePau.setVisibility(View.VISIBLE);
                    PlayContent.playCont = false;
                    for (int i = 0; i < PlayContent.bottomPlayOrPauseList.size(); i++) {
                        PlayContent.bottomPlayOrPauseList.get(i).setImageResource(R.drawable.play_btn);
                    }

                }
            }
        });
        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAdapter.CloudMusicAdapter.playNextMusic();
                playNeedle.setVisibility(View.VISIBLE);
                playNeedlePau.setVisibility(View.INVISIBLE);
                musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());
                musicMaxTime.setText(converLongTimeToStr(Long.parseLong(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTime())));
                musicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicName());
                musicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger() + ">");
                selectUri = Uri.parse(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicPath());
                metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
                artwork = metadataRetriever.getEmbeddedPicture();
                if (artwork != null) {
                    bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
                    rawHeight = bitmap.getHeight();
                    rawWidth = bitmap.getWidth();
                    newHeight = rawHeight;
                    newWidth = rawWidth / 2;
                    heightScale = ((float) newHeight) / rawHeight;
                    widthScale = ((float) newWidth) / rawWidth;
                    newBitmap = Bitmap.createBitmap(bitmap, rawWidth/4, 0, newWidth, newHeight);
                    roundBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight);
                }
                bitmap = BlurUtil.doBlur(newBitmap,12,20);
                drawable = new BitmapDrawable(getResources(),bitmap);
                backgroup.setBackground(drawable);
                setPlayRoundDisc(roundBitmap);
                animation1.end();
                animation1.start();
            }

        });
        playPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicAdapter.CloudMusicAdapter.playPreMusic();
                playNeedle.setVisibility(View.VISIBLE);
                playNeedlePau.setVisibility(View.INVISIBLE);
                musicSeek.setMax(CloudActivity.mmediaPlayer.getDuration());
                musicMaxTime.setText(converLongTimeToStr(Long.parseLong(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicTime())));
                musicTitle.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicName());
                musicSinger.setText(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicSinger() + ">");
                selectUri = Uri.parse(MusicAdapter.CloudMusicAdapter.nowPlay().getMusicPath());
                metadataRetriever.setDataSource(MyApplication.getContext(), selectUri);
                artwork = metadataRetriever.getEmbeddedPicture();
                if (artwork != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
                    rawHeight = bitmap.getHeight();
                    rawWidth = bitmap.getWidth();
                    newHeight = rawHeight;
                    newWidth = rawWidth / 2;
                    heightScale = ((float) newHeight) / rawHeight;
                    widthScale = ((float) newWidth) / rawWidth;
                    newBitmap = Bitmap.createBitmap(bitmap, rawWidth/4, 0, newWidth, newHeight);
                    roundBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeight);
                }
                bitmap = BlurUtil.doBlur(newBitmap,12,20);
                drawable = new BitmapDrawable(getResources(),bitmap);
                backgroup.setBackground(drawable);
                setPlayRoundDisc(roundBitmap);
                animation1.end();
                animation1.start();
            }
        });

        musicSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int t;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b==true){
                   t = i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateThread);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CloudActivity.mmediaPlayer.seekTo(t);
                handler.post(updateThread);

            }
        });

        playModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MusicAdapter.CloudMusicAdapter.getPlayModel() == listLoop){
                    MusicAdapter.CloudMusicAdapter.setPlayModel(musicLoop);
                    Toast.makeText(MusicActivity.this,"单曲循环",Toast.LENGTH_SHORT).show();
                }else if (MusicAdapter.CloudMusicAdapter.getPlayModel() == musicLoop){
                    MusicAdapter.CloudMusicAdapter.setPlayModel(playRandom);
                    Toast.makeText(MusicActivity.this,"随机循环",Toast.LENGTH_SHORT).show();
                }else if(MusicAdapter.CloudMusicAdapter.getPlayModel() == playRandom){
                    MusicAdapter.CloudMusicAdapter.setPlayModel(listLoop);
                    Toast.makeText(MusicActivity.this,"列表循环",Toast.LENGTH_SHORT).show();
                }
            }
        });

        playSrc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                showPopupWindow(view);

            }
        });

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
