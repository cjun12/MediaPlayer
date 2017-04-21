package com.coship.app.mediaplayer.view.activity.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.presenter.photo.impl.ShowPresenterImpl;
import com.coship.app.mediaplayer.presenter.photo.interfaces.IShowPresenter;
import com.coship.app.mediaplayer.view.interfaces.photo.IShowPhotoView;

import java.net.URI;
import java.util.Date;
import java.util.Random;

/**
 * Created by 980558 on 2017/4/7.
 */

public class ShowActivity extends AppCompatActivity implements IShowPhotoView, View.OnKeyListener, View.OnClickListener {
    private static final String TAG = "ShowActivity";
    private IShowPresenter mPresenter;
    private int mPos;
    private Handler mHandler;
    private Runnable mPlayerRunable;
    private Runnable mToggleRunable;
    private Matrix mMatrix;
    private boolean mIsPlayer = false;

    private ImageSwitcher imgsPhoto;
    private RelativeLayout rlControlBar;
    private Button btnPlayOrPause;
    private Button btnZoomIn;
    private Button btnZoomOut;
    private Button btnRotateLeft;
    private Button btnRotateRight;
    private int[] mAnimArray = {
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right,
            R.anim.slide_in_right,
            R.anim.slide_out_left};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_show_activity);
        mPresenter = new ShowPresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initSwitcher();
        initControlBar();
        mHandler = new Handler() {

        };
        mPlayerRunable = new Runnable() {
            @Override
            public void run() {
                if (mPresenter.isLast(mPos)) {
                    mPos = 0;
                } else {
                    mPos++;
                }
                changeImage(mPos);
                mHandler.postDelayed(mPlayerRunable, 5000);
            }
        };

        mToggleRunable = new Runnable() {
            @Override
            public void run() {
                rlControlBar.setVisibility(View.GONE);
                imgsPhoto.requestFocus();
            }
        };
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            int curId = getCurrentFocus().getId();
            if (curId == R.id.btn_rotate_left || curId == R.id.btn_play_or_pause
                    || curId == R.id.btn_rotate_right
                    || curId == R.id.btn_zoom_in
                    || curId == R.id.btn_zoom_out) {
                rlControlBar.setVisibility(View.GONE);
                imgsPhoto.requestFocus();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void initSwitcherParam() {
        imgsPhoto.setRotation(0f);
        imgsPhoto.setScaleX(1f);
        imgsPhoto.setScaleY(1f);
    }

    private void initControlBar() {
        rlControlBar = (RelativeLayout) findViewById(R.id.rl_control_bar);

        btnPlayOrPause = (Button) findViewById(R.id.btn_play_or_pause);
        btnPlayOrPause.setOnClickListener(this);

        btnZoomIn = (Button) findViewById(R.id.btn_zoom_in);
        btnZoomIn.setOnClickListener(this);

        btnZoomOut = (Button) findViewById(R.id.btn_zoom_out);
        btnZoomOut.setOnClickListener(this);

        btnRotateLeft = (Button) findViewById(R.id.btn_rotate_left);
        btnRotateLeft.setOnClickListener(this);

        btnRotateRight = (Button) findViewById(R.id.btn_rotate_right);
        btnRotateRight.setOnClickListener(this);
    }

    private void initSwitcher() {
        imgsPhoto = (ImageSwitcher) findViewById(R.id.switcher_photo_show);
        imgsPhoto.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(ShowActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
        mPos = getIntent().getIntExtra("pos", 0);
        Uri uri = Uri.parse(mPresenter.loadPhoto(mPos).getPath());
        imgsPhoto.setImageURI(uri);
        imgsPhoto.setFocusable(true);
        imgsPhoto.requestFocus();
        imgsPhoto.setOnKeyListener(this);

        mMatrix = new Matrix();
    }

    private void changeImage(int pos) {
        initSwitcherParam();
        Uri uri = Uri.parse(mPresenter.loadPhoto(mPos).getPath());
        imgsPhoto.setImageURI(uri);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Uri uri;
        Log.i(TAG, "onKey: " + v);
        if (v.getId() == imgsPhoto.getId() && event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mPresenter.isLast(mPos)) {
                        Toast.makeText(this, "最后一张", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    mPos++;
                    imgsPhoto.setInAnimation(AnimationUtils.loadAnimation(this,mAnimArray[4]));
                    imgsPhoto.setOutAnimation(AnimationUtils.loadAnimation(this,mAnimArray[5]));
                    changeImage(mPos);
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (mPos == 0) {
                        Toast.makeText(this, "第一张", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    mPos--;
                    imgsPhoto.setInAnimation(AnimationUtils.loadAnimation(this,mAnimArray[2]));
                    imgsPhoto.setOutAnimation(AnimationUtils.loadAnimation(this,mAnimArray[3]));
                    changeImage(mPos);
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    rlControlBar.setVisibility(View.VISIBLE);
                    btnPlayOrPause.requestFocus();
                    if(mIsPlayer)
                    {
                        mHandler.postDelayed(mToggleRunable,5000);
                    }
                    break;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        float scale;
        float rotate;
        mMatrix.set(imgsPhoto.getMatrix());
        switch (v.getId()) {
            case R.id.btn_play_or_pause:
                if (mIsPlayer) {
                    mIsPlayer = false;
                    mHandler.removeCallbacks(mPlayerRunable);
                    btnPlayOrPause.setBackgroundResource(R.drawable.btn_play_or_pause_selector);
                    mHandler.removeCallbacks(mToggleRunable);
                } else {
                    mIsPlayer = true;
                    mHandler.post(mPlayerRunable);
                    btnPlayOrPause.setBackgroundResource(R.drawable.btn_pause_selector);
                    imgsPhoto.setInAnimation(AnimationUtils.loadAnimation(this,mAnimArray[4]));
                    imgsPhoto.setOutAnimation(AnimationUtils.loadAnimation(this,mAnimArray[5]));
                    mHandler.postDelayed(mToggleRunable,5000);
                }
                break;
            case R.id.btn_rotate_left:
                resetHiddenTimer();
                rotate = imgsPhoto.getRotation();
                imgsPhoto.setRotation(rotate - 90f);
                break;
            case R.id.btn_rotate_right:
                resetHiddenTimer();
                rotate = imgsPhoto.getRotation();
                imgsPhoto.setRotation(rotate + 90f);
                break;
            case R.id.btn_zoom_in:
                resetHiddenTimer();
                scale = imgsPhoto.getScaleX();
                imgsPhoto.setScaleX(1.2f * scale);
                imgsPhoto.setScaleY(1.2f * scale);
                break;
            case R.id.btn_zoom_out:
                resetHiddenTimer();
                scale = imgsPhoto.getScaleX();
                imgsPhoto.setScaleX(0.8f * scale);
                imgsPhoto.setScaleY(0.8f * scale);
                break;
        }
    }

    private  void resetHiddenTimer(){
        if(mIsPlayer){
            mHandler.removeCallbacks(mToggleRunable);
            mHandler.postDelayed(mToggleRunable,5000);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mPlayerRunable);
    }
}
