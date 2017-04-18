package com.coship.app.mediaplayer.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coship.app.mediaplayer.R;

import org.w3c.dom.Text;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.ACTION_UP;
import static android.view.KeyEvent.KEYCODE_DPAD_DOWN;
import static android.view.KeyEvent.KEYCODE_DPAD_UP;

/**
 * Created by 980558 on 2017/4/1.
 */

public class HomeActivity extends AppCompatActivity implements View.OnKeyListener {
    private static final String TAG = "HomeActivity";
    private TextView tv_music;
    private TextView tv_all_music;
    private TextView tv_singer;
    private TextView tv_album;
    private TextView tv_music_folder;
    private TextView tv_music_search;
    private LinearLayout ll_music;
    private LinearLayout ll_music_container;

    private TextView tv_photo;
    private TextView tv_all_photo;
    private TextView tv_photo_folder;
    private LinearLayout ll_photo;
    private LinearLayout ll_photo_container;

    private TextView tv_video;
    private TextView tv_all_video;
    private TextView tv_video_folder;
    private LinearLayout ll_video;
    private LinearLayout ll_video_container;


    private LinearLayout ll_file_container;
    private TextView tv_file;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initControl();
    }
    //初始化所有需要使用的控件
    private void initControl() {
        ll_photo_container=(LinearLayout)findViewById(R.id.home_ll_photo_container);
        ll_photo_container.setFocusable(true);
        ll_photo_container.requestFocus();
        ll_photo_container.setOnKeyListener(this);

        ll_music_container=(LinearLayout)findViewById(R.id.home_ll_music_container);
        ll_music_container.setFocusable(true);
        ll_music_container.setOnKeyListener(this);

        ll_video_container=(LinearLayout)findViewById(R.id.home_ll_video_container);
        ll_video_container.setFocusable(true);
        ll_video_container.setOnKeyListener(this);

        ll_file_container=(LinearLayout)findViewById(R.id.home_ll_file_container);
        ll_file_container.setFocusable(true);
        ll_file_container.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getAction()==ACTION_DOWN){
            return false;
        }
        switch (v.getId()) {
            case R.id.home_ll_photo_container:
                if (event.getKeyCode() == KEYCODE_DPAD_DOWN) {
                    changeMenu(ll_photo_container, ll_music_container);
                } else if (event.getKeyCode() == KEYCODE_DPAD_UP) {
                    changeMenu(ll_photo_container, ll_file_container);
                }
                break;
            case R.id.home_ll_music_container:
                if (event.getKeyCode() == KEYCODE_DPAD_DOWN) {
                    changeMenu(ll_music_container, ll_video_container);
                }else if (event.getKeyCode() == KEYCODE_DPAD_UP) {
                    changeMenu(ll_music_container, ll_photo_container);
                }
                break;
            case R.id.home_ll_video_container:
                if (event.getKeyCode() == KEYCODE_DPAD_DOWN) {
                    changeMenu(ll_video_container, ll_file_container);
                }else if (event.getKeyCode() == KEYCODE_DPAD_UP) {
                    changeMenu(ll_video_container, ll_music_container);
                }
                break;
            case R.id.home_ll_file_container:
                if(event.getKeyCode() == KEYCODE_DPAD_DOWN){
                    changeMenu(ll_file_container, ll_photo_container);
                }else if (event.getKeyCode() == KEYCODE_DPAD_UP) {
                    changeMenu(ll_file_container, ll_video_container);
                }
                break;
        }
        return true;
    }

    private void changeMenu(LinearLayout cur, LinearLayout next) {
        TextView curMenu = ((TextView) cur.getChildAt(0));
        curMenu.setTextColor(ContextCompat.getColor(this, R.color.menu_default));
        curMenu.setShadowLayer(16, 0, 0, 0xffaaaaaa);
        cur.getChildAt(1).setVisibility(View.GONE);
        cur.clearFocus();

        TextView nextMenu = ((TextView) next.getChildAt(0));
        nextMenu.setTextColor(ContextCompat.getColor(this, R.color.menu_activity));
        nextMenu.setShadowLayer(16, 0, 0, 0xff009688);
        next.getChildAt(1).setVisibility(View.VISIBLE);
        next.requestFocus();
    }
}
