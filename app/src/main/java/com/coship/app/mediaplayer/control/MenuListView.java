package com.coship.app.mediaplayer.control;


import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * Created by 980558 on 2017/4/1.
 */

public class MenuListView extends View {
    private int mMenuColor;
    private int mSubMenuColor;
    public MenuListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
