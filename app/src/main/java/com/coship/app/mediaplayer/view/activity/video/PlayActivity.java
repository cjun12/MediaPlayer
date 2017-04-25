package com.coship.app.mediaplayer.view.activity.video;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.model.impl.VideoModelImpl;
import com.coship.app.mediaplayer.model.interfaces.IVideoModel;
import com.coship.app.mediaplayer.service.PlayerService;
import com.coship.app.mediaplayer.toolkit.StringConvertor;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 980558 on 2017/4/21.
 */

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PlayActivity";
    private MediaPlayer mPlayer;
    private SurfaceView svPlayView;

    private Button btnPlayOrPause;
    private Button btnNext;
    private Button btnPre;
    private SeekBar sbProcess;
    private TextView tvCurProcess;
    private TextView tvTotalTime;

    private Handler mHandler;
    private IVideoModel mModel;
    private int mPos;
    private Runnable mRunnable;


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

        sbProcess = (SeekBar) findViewById(R.id.sb_video_play_video);
        sbProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    sbProcess.setProgress(progress);
                    mPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnPre = (Button) findViewById(R.id.btn_play_or_pause_video);
        btnPlayOrPause = (Button) findViewById(R.id.btn_pre_video);
        btnNext = (Button) findViewById(R.id.btn_next_video);
        btnNext.setOnClickListener(this);
        btnPlayOrPause.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        tvCurProcess = (TextView) findViewById(R.id.tv_cur_process);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);


        mHandler = new Handler() {

        };

        mRunnable = new Runnable() {
            @Override
            public void run() {
                sbProcess.setProgress(mPlayer.getCurrentPosition());
                tvCurProcess.setText(StringConvertor.toFormatTime(mPlayer.getCurrentPosition()));
                mHandler.postDelayed(mRunnable, 100);
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playOrPause();
            }
        });
        mModel = VideoModelImpl.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_video:
                nextVideo();
                break;
            case R.id.btn_pre_video:
                preVideo();
                break;
            case R.id.btn_play_or_pause_video:
                playOrPause();
                break;
        }
    }

    private void nextVideo() {
        if (mPos + 1 != mModel.size()) {
            mPos++;
            playVideo();
        } else {
            Toast.makeText(this, "最后一个", Toast.LENGTH_SHORT).show();
        }
    }

    private void preVideo() {
        if (mPos == 0) {
            Toast.makeText(this, "第一个", Toast.LENGTH_SHORT).show();
        } else {
            mPos--;
            playVideo();
        }
    }

    private void playOrPause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    private void playVideo() {
        mPlayer.reset();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.i(TAG, "playVideo: " + mModel.get(mPos).getPath());
            mPlayer.setDataSource(this, Uri.parse(mModel.get(mPos).getPath()));
            mPlayer.setDisplay(svPlayView.getHolder());
            mPlayer.prepare();
            mPlayer.seekTo(0);
            mPlayer.start();
            WindowManager wManager = getWindowManager();
            DisplayMetrics metrics = new DisplayMetrics();
            wManager.getDefaultDisplay().getMetrics(metrics);
            svPlayView.setLayoutParams(new FrameLayout.LayoutParams(
                    metrics.widthPixels,
                    mPlayer.getVideoHeight() * metrics.widthPixels / mPlayer.getVideoWidth()));

            sbProcess.setMax(mPlayer.getDuration());
            tvTotalTime.setText(StringConvertor.toFormatTime(mPlayer.getDuration()));

            sbProcess.setKeyProgressIncrement(5000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SurfaceListener implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated: ");
            playVideo();
            mHandler.post(mRunnable);
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
