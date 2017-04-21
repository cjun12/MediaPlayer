package com.coship.app.mediaplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coship.app.mediaplayer.toolkit.AudioHelper;
import com.coship.app.mediaplayer.toolkit.StringConvertor;
import com.coship.app.mediaplayer.bean.music.Song;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by 980558 on 2017/4/11.
 */

public class PlayerService extends Service {
    public static final int LOOP = 0;
    public static final int SINGLE = 1;
    public static final int SHUFFLE =2;

    private static final String TAG = "PlayerService";
    public static MediaPlayer mp = new MediaPlayer();

    private String albumPath;
    private AudioHelper audioHelper;
    public final IBinder binder = new PlayerBinder();
    private static List<Song> songs;
    private static int pos;
    private static int mode = 0;

    public static List<Song> getSongs() {
        return songs;
    }

    public static void setSongs(List<Song> songs) {
        PlayerService.songs = songs;
    }

    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        PlayerService.pos = pos;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        if (mp == null) {
            mp = new MediaPlayer();
        }
        loadAlbumPath();
        intent = new Intent();
        intent.setAction("com.coship.app.mediaplayer.SONG_CHANGE");
        intent.putExtra("msg", "load_image");
        sendBroadcast(intent);
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(mode!=SINGLE){
                    next();
                }
                Intent intent = new Intent();
                intent.setAction("com.coship.app.mediaplayer.SONG_CHANGE");
                intent.putExtra("msg", "next_song");
                sendBroadcast(intent);
            }
        });
        Log.i(TAG, "onCreate: "+this);
    }


    public void next() {
        if(mode == SHUFFLE)
        {
            Random random = new Random(new Date().getTime());
            pos = random.nextInt(songs.size());
        }else{
            pos = (pos + 1) % songs.size();
        }
        Log.i(TAG, "next: "+pos);
        play(pos);
        loadAlbumPath();
    }


    public void play() {
        if (mp.isPlaying()) {
            return;
        } else {
            mp.start();
        }
    }

    public void toggleLoop() {
        mp.setLooping(!mp.isLooping());
    }

    public void play(int pos) {
        String path = getSong().getPath();
        PlayerService.pos = pos;
        try {
            mp.reset();
            mp.setDataSource(path);
            mp.prepare();
            mp.seekTo(0);
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAlbumPath() {
        if (audioHelper == null) {
            audioHelper = new AudioHelper(this);
        }
        albumPath = audioHelper.getAlbumImagePath(songs.get(pos).getAlbumId());
        Log.i(TAG, "loadAlbumPath: "+albumPath);
    }

    public String getAlbumPath() {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();

        String method = Thread.currentThread().getStackTrace()[3].getMethodName();
        Log.i(TAG, "getAlbumPath: "+className+" "+method);
        Log.i(TAG, "getAlbumPath: "+albumPath);
        return albumPath;
    }

    public void pause() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    public void stop() {
        if (mp != null) {
            mp.stop();
//            try {
//                mp.prepare();
//                mp.seekTo(0);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    public String loadLrcPath(){
        Log.i(TAG, "loadLrcPath: "+songs.get(pos).getName());
        String result = null;
        Song song = songs.get(pos);
        String path = song.getFolder();
        File file = new File(path);
        File [] fileList= file.listFiles();
        for(File item : fileList){
            if(item.isDirectory()){
                continue;
            }
            String FileName=  item.getName();
            System.out.println(item);
            String fileFormat = FileName.substring(FileName.lastIndexOf("."));
            if(".lrc".equals(fileFormat)&&FileName.contains(song.getName())){
                result = FileName;
            }
        }
        return song.getFolder()+"/gbqq.lrc";
    }

    public void toggle() {
        if (mp.isPlaying()) {
            mp.pause();
        } else {
            mp.start();
        }
    }

    public void prefix() {
        if(mode == SHUFFLE)
        {
            Random random = new Random(new Date().getTime());
            pos = random.nextInt(songs.size());
        }else{
            pos = (pos + songs.size() - 1) % songs.size();
        }
        play(pos);
        loadAlbumPath();
    }

    public void setMode() {
        mode = (mode+1)%3;
        Log.i(TAG, "setMode: "+mode);
        if(mode==SINGLE){
            mp.setLooping(true);
        }else{
            mp.setLooping(false);
        }
    }
    public int getMode() {
        return mode;
    }
    public class PlayerBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    public Song getSong() {
        return songs.get(pos);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        stop();
        return true;
    }

    public List<Object> getSongNameList() {
        return StringConvertor.beanListToBeanFieldList(songs, "Name");
    }
}
