package com.example.administrator.playaudiotest.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.playaudiotest.other.MineMusicList;
import com.example.administrator.playaudiotest.R;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LaunchActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else {
           // 初始化MediaPlayer
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MineMusicList.mineMusicList.refeshMusicList(LaunchActivity.this);
                }
            }).start();
        }
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
        Handler launch = new Handler();

        launch.postDelayed(new splashhandler(),4000);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MineMusicList.mineMusicList.refeshMusicList(LaunchActivity.this);
//                            MusicAdapter.CloudMusicAdapter.notifyDataSetChanged();
                        }
                    }).start();



                } else {
                    Toast.makeText(getApplicationContext(), "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    LaunchActivity.this.finish();
                }
                break;
            default:
        }
    }


class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(LaunchActivity.this,MainActivity.class));// 这个线程的作用3秒后就是进入到你的主界面
            LaunchActivity.this.finish();// 把当前的LaunchActivity结束掉
        }
    }

}
