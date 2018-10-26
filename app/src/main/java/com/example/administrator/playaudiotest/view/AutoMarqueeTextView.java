package com.example.administrator.playaudiotest.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/5/17 0017.
 */

public class AutoMarqueeTextView extends android.support.v7.widget.AppCompatTextView {

    public AutoMarqueeTextView(Context context){
        super(context);
        setFocusable(true);
    }

    public AutoMarqueeTextView(Context context, AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        setFocusable(true);
    }

    public AutoMarqueeTextView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
