package com.coship.app.mediaplayer.view.activity.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.toolkit.StringConvertor;
import com.coship.app.mediaplayer.bean.music.Song;
import com.coship.app.mediaplayer.control.FocusLayout;
import com.coship.app.mediaplayer.control.LyricView;
import com.coship.app.mediaplayer.service.PlayerService;

import java.util.List;

/**
 * Created by 980558 on 2017/4/5.
 */

public class PlayActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnKeyListener {
    private static final String TAG = "PlayActivity";

    private TextView tv_songName;
    private TextView tv_singerName;
    private TextView tv_albumName;
    private TextView tv_curTime;
    private TextView tv_totalTime;

    private Button btn_playList;
    private Button btn_rewind;
    private Button btn_play;
    private Button btn_fastword;
    private Button btn_volume;
    private Button btn_playMode;

    private SeekBar sb_song;
    private SeekBar sb_volume;

    private LyricView lrc_container;
    private ListView lv_playList;
    private ImageView img_album_image;

    private FocusLayout mFocusLayout;
    private AudioManager mAudioManager;
    private PlayerService playerService;
    private SongReceiver songReceiver;
    private Handler handler = new Handler();
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playerService = ((PlayerService.PlayerBinder) service).getService();
            showImage();
            tv_totalTime.setText(StringConvertor.toFormatTime(PlayerService.mp.getDuration()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.music_player_activity);
        Bundle data = getIntent().getExtras();
        List<Song> songs = (List<Song>) data.getSerializable("list");
        int position = data.getInt("position");

        playerService = new PlayerService();
        bindServiceConnection();
        PlayerService.setSongs(songs);
        PlayerService.setPos(position);
        playerService.play(position);
        songReceiver = new SongReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.coship.app.mediaplayer.SONG_CHANGE");
        registerReceiver(songReceiver, filter);

        initUI(songs.get(position));
        changeSongInfo(songs.get(position));
    }


    private void initUI(Song song) {
        //更新歌曲信息
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        tv_songName = (TextView) findViewById(R.id.music_player_tv_songName);
//        tv_songName.setText(song.getName() + " - " + song.getSinger());

        tv_singerName = (TextView) findViewById(R.id.tv_music_list_singerName);
//        tv_singerName.setText(song.getSinger());
        tv_albumName = (TextView) findViewById(R.id.tv_music_list_albumName);
//        tv_albumName.setText(song.getAlbum());

        tv_curTime = (TextView) findViewById(R.id.tv_music_list_curTime);
        tv_totalTime = (TextView) findViewById(R.id.tv_music_list_totalTime);

        //初始化音量控件
        initVolumnBar();

        sb_song = (SeekBar) findViewById(R.id.sb_music_play_song);
        sb_song.setOnKeyListener(this);

        btn_play = (Button) findViewById(R.id.btn_music_play_play);
        btn_play.setOnClickListener(this);
        btn_play.requestFocus();

        btn_fastword = (Button) findViewById(R.id.btn_music_play_fast_forward);
        btn_fastword.setOnClickListener(this);

        btn_rewind = (Button) findViewById(R.id.btn_music_play_rewind);
        btn_rewind.setOnClickListener(this);

        btn_volume = (Button) findViewById(R.id.btn_music_play_volume);
        btn_volume.setOnClickListener(this);

        btn_playList = (Button) findViewById(R.id.btn_music_play_list);
        btn_playList.setOnClickListener(this);

        btn_playMode = (Button) findViewById(R.id.btn_music_play_mode);
        btn_playMode.setOnClickListener(this);

        lrc_container = (LyricView) findViewById(R.id.lyricView_lrc_container);
        lrc_container.setFocusable(false);

        img_album_image = (ImageView) findViewById(R.id.img_music_list_album_image);


        initPlayList();
    }

    private void initVolumnBar() {
        //更新音量大小进度条
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        sb_volume = (SeekBar) findViewById(R.id.sb_music_play_volume);
        sb_volume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sb_volume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initPlayList() {
        lv_playList = (ListView) findViewById(R.id.lv_music_play_list);
        Object[] data = playerService.getSongNameList().toArray();
        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, data);
        lv_playList.setAdapter(adapter);
        lv_playList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerService.play(position);
                playerService.loadAlbumPath();
                changeSongInfo();
            }
        });
    }


    private void bindServiceConnection() {
        Intent intent = new Intent(PlayActivity.this, PlayerService.class);
        bindService(intent, sc, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (PlayerService.mp.isPlaying()) {
            btn_play.setBackgroundResource(R.mipmap.pause_128px);
        } else {
            btn_play.setBackgroundResource(R.mipmap.play_128px);
        }

        sb_song.setProgress(PlayerService.mp.getCurrentPosition());
        sb_song.setMax(PlayerService.mp.getDuration());
        String path = playerService.loadLrcPath();
        lrc_container.read(path);
        lrc_container.SetTextSize();
        lrc_container.setOffsetY(350);
        handler.post(runnable);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_music_play_fast_forward:
                playerService.next();
                changeSongInfo();
                break;
            case R.id.btn_music_play_list:
                togglePlaylist();
                break;
            case R.id.btn_music_play_play:
                playerService.toggle();
                break;
            case R.id.btn_music_play_volume:
                int volumn = mAudioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
                Log.i(TAG, "onClick: "+volumn);
                if (volumn <= 0) {
                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    btn_volume.setBackgroundResource(R.mipmap.mute_128px);
                } else {
                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    btn_volume.setBackgroundResource(R.mipmap.volume_up_128px);
                }
                sb_volume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                break;
            case R.id.btn_music_play_rewind:
                playerService.prefix();
                changeSongInfo();
                break;
            case R.id.btn_music_play_mode:
                playerService.setMode();
                int mode = playerService.getMode();
                if(mode == PlayerService.SINGLE){
                    btn_playMode.setBackgroundResource(R.mipmap.loop);
                    Toast.makeText(this,"单曲循环",Toast.LENGTH_SHORT).show();
                }else if(mode == PlayerService.LOOP){
                    btn_playMode.setBackgroundResource(R.mipmap.loop_alt);
                    Toast.makeText(this,"列表循环",Toast.LENGTH_SHORT).show();;
                }else{
                    btn_playMode.setBackgroundResource(R.mipmap.shuffle);
                    Toast.makeText(this,"随机播放",Toast.LENGTH_SHORT).show();;
                }
                break;
        }
    }

    private void togglePlaylist() {
        if (lv_playList.getVisibility() == View.VISIBLE) {
            btn_playList.setNextFocusRightId(btn_rewind.getId());
            btn_playList.setNextFocusUpId(sb_song.getId());
            lv_playList.setVisibility(View.GONE);
            lv_playList.setFocusable(false);
        } else {
            lv_playList.setVisibility(View.VISIBLE);
            lv_playList.setFocusable(true);
            lv_playList.setNextFocusDownId(btn_playList.getId());
            lv_playList.setNextFocusUpId(btn_playList.getId());
            lv_playList.setNextFocusLeftId(btn_playList.getId());
            lv_playList.setNextFocusRightId(btn_playList.getId());

            btn_playList.setNextFocusDownId(lv_playList.getId());
            btn_playList.setNextFocusUpId(lv_playList.getId());
            btn_playList.setNextFocusLeftId(lv_playList.getId());
            btn_playList.setNextFocusRightId(lv_playList.getId());
        }
    }

    public void changeSongInfo() {
        Song song = playerService.getSong();
        tv_songName.setText(song.getName() + " - " + song.getSinger());
        tv_singerName.setText(song.getSinger());
        tv_albumName.setText(song.getAlbum());
        tv_totalTime.setText(StringConvertor.toFormatTime(PlayerService.mp.getDuration()));
        showImage();
        lrc_container.SetTextSize();
        lrc_container.setOffsetY(200);
        sb_song.setMax(PlayerService.mp.getDuration());
    }

    public void changeSongInfo(Song song) {
        tv_songName.setText(song.getName() + " - " + song.getSinger());
        tv_singerName.setText(song.getSinger());
        tv_albumName.setText(song.getAlbum());
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int cur = PlayerService.mp.getCurrentPosition();
            if (playerService.mp.isPlaying()) {
                btn_play.setBackgroundResource(R.mipmap.pause_128px);
            } else {
                btn_play.setBackgroundResource(R.mipmap.play_128px);
            }
            tv_curTime.setText(StringConvertor.toFormatTime(cur));
            sb_song.setProgress(cur);
            sb_song.setOnKeyListener(PlayActivity.this);
            lrc_container.setOffsetY(lrc_container.getOffsetY() - lrc_container.SpeedLrc());
            lrc_container.SelectIndex(PlayerService.mp.getCurrentPosition());
            lrc_container.invalidate();
            sb_song.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        PlayerService.mp.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    sb_volume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + AudioManager.ADJUST_RAISE);
                    break;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    sb_volume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + AudioManager.ADJUST_LOWER);
                    break;
                case KeyEvent.KEYCODE_VOLUME_MUTE:
                    int volumn = mAudioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
                    if (volumn <= 0) {
                        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        btn_volume.setBackgroundResource(R.mipmap.volume_up_128px);
                    } else {
                        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        btn_volume.setBackgroundResource(R.mipmap.mute_128px);
                    }
                    sb_volume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    return true;
            }
            Log.d(TAG, "dispatchKeyEvent: " + getCurrentFocus());
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()) {
            case R.id.btn_music_play_list:
                break;
        }
        return false;
    }


    private void bindListener() {
        //获取根元素
        View mContainerView = this.getWindow().getDecorView();//.findViewById(android.R.id.content);
        //得到整个view树的viewTreeObserver
        ViewTreeObserver viewTreeObserver = mContainerView.getViewTreeObserver();
        //给观察者设置焦点变化监听
        viewTreeObserver.addOnGlobalFocusChangeListener(mFocusLayout);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mFocusLayout = new FocusLayout(this);
        bindListener();//绑定焦点变化事件
        addContentView(mFocusLayout,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));//添加焦点层
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        playerService.stop();
        handler.removeCallbacks(runnable);
        unbindService(sc);
        unregisterReceiver(songReceiver);
        super.onDestroy();

    }

    public class SongReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Log.d(TAG, "onReceive: " + msg);
            if ("next_song".equals(msg)) {
                changeSongInfo();
            }
        }
    }

    public void showImage() {
        String albumPath = playerService.getAlbumPath();
        if (albumPath == null) {
            img_album_image.setBackgroundResource(R.drawable.default_image);
        } else {
            Log.i(TAG, "showImage: " + albumPath);
            Bitmap bm = BitmapFactory.decodeFile(albumPath);
            BitmapDrawable bmpDraw = new BitmapDrawable(getResources(), bm);
            img_album_image.setImageDrawable(bmpDraw);
        }
    }

}
