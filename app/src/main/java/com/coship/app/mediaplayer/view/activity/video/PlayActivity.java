package com.coship.app.mediaplayer.view.activity.video;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.model.impl.VideoModelImpl;
import com.coship.app.mediaplayer.model.interfaces.IVideoModel;

import java.io.IOException;

/**
 * Created by 980558 on 2017/4/21.
 */

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";
    private MediaPlayer mPlayer;
    private SurfaceView svPlayView;
    private IVideoModel mModel;
    private int mPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        mPos = getIntent().getIntExtra("pos", 0);
        svPlayView = (SurfaceView) findViewById(R.id.sv_video_play);
        svPlayView.getHolder().setKeepScreenOn(true);
        svPlayView.getHolder().addCallback(new SurfaceListener());

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });
        mModel = VideoModelImpl.getInstance();
    }

    private void playVideo() {
        mPlayer.reset();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.i(TAG, "playVideo: "+mModel.get(mPos).getPath());
            mPlayer.setDataSource(this, Uri.parse(mModel.get(mPos).getPath()));
            mPlayer.setDisplay(svPlayView.getHolder());
            mPlayer.prepare();
            mPlayer.seekTo(0);
            mPlayer.start();
//            WindowManager wManager = getWindowManager();
//            DisplayMetrics metrics = new DisplayMetrics();
//            wManager.getDefaultDisplay().getMetrics(metrics);
//            svPlayView.setLayoutParams(new WindowManager.LayoutParams(
//                    metrics.widthPixels,
//                    mPlayer.getVideoHeight() * metrics.widthPixels / mPlayer.getVideoWidth()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SurfaceListener implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated: ");
            playVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "surfaceCreated: ");

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}
