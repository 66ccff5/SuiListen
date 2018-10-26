package com.example.administrator.playaudiotest.other;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.administrator.playaudiotest.bean.MyApplication;
import com.example.administrator.playaudiotest.R;
import com.example.administrator.playaudiotest.adapter.MusicAdapter;

/**
 * Created by Administrator on 2018/6/7 0007.
 */

public class ListPop extends PopupWindow{

    private Context mContext;

    private View view;

    private LinearLayoutManager layoutManager;

    RecyclerView listView;



    private LinearLayout listLin;


    public ListPop(Context mContext) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_window, null);

//        btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
//        btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
        listLin = (LinearLayout) view.findViewById(R.id.play_model_pop);
        // 取消按钮
        listLin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
//        btn_pick_photo.setOnClickListener(itemsOnClick);
//        btn_take_photo.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.view.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = view.findViewById(R.id.pop_layout).getTop();
//
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });

        listView = (RecyclerView) view.findViewById(R.id.pop_music_list_view);
        layoutManager = new LinearLayoutManager(view.getContext());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(MusicAdapter.CloudMusicAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(view.getContext(),R.drawable.mine_own_item_devide));
        listView.addItemDecoration(dividerItemDecoration);



        this.setAnimationStyle(R.style.popup_window_anim);
        // TODO: 2016/5/17 设置背景颜色
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        this.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        this.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        this.update();


        WindowManager wm = (WindowManager) MyApplication.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(height * 3 / 5);
        this.setWidth(width);






    }


}
