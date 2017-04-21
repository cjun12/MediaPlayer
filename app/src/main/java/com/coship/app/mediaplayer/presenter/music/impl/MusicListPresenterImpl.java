package com.coship.app.mediaplayer.presenter.music.impl;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.coship.app.mediaplayer.R;
import com.coship.app.mediaplayer.bean.music.Song;
import com.coship.app.mediaplayer.view.adapter.SongAdapter;
import com.coship.app.mediaplayer.view.interfaces.music.IListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by 980558 on 2017/4/18.
 */

public class MusicListPresenterImpl {
    //存储所有音乐
    private List<Song> mSongs = new ArrayList<>();
    //视图接口
    private IListView mView;


    public MusicListPresenterImpl(IListView mView) {
        this.mView = mView;
    }

    /**
     * 加载所有音乐
     */
    private void queryAllSong() {
        Song song = null;
        String albumId;
        //清空之前保留的结果
        mSongs.clear();
        //查询media的所有音乐，获取cursor
        Cursor cursor = mView.getContext().getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null, null);
        //判断查询的结果数是否为0
        if (cursor.getCount() <= 0) {
            Toast.makeText(mView.getContext(), "未找到歌曲", Toast.LENGTH_SHORT);
            return;
        }
        //遍历结果集
        while (cursor.moveToNext()) {
            song = new Song();
            //歌曲名
            song.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            //歌手
            song.setSinger(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            //专辑名
            song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            //歌曲路径
            song.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            //专辑ID
            albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            song.setAlbumPath(queryAlbumImagePath(albumId));
            mSongs.add(song);
        }
        cursor.close();
    }

    private String queryAlbumImagePath(String albumId) {
        String path = null;
        //设置查询URI
        Uri album = Uri.parse("content://media/external/audio/albums/" + albumId);
        //查询ContentProviders
        Cursor cursor = mView.getContext().getContentResolver()
                .query(album, new String[]{"album_art"}, null, null, null);
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));
        }
        cursor.close();
        return path;
    }

    public void initData() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                queryAllSong();
                handler.sendEmptyMessage(0x0001);
            }
        });
    }

    public void loadAllSongs() {
        SongAdapter songAdapter = new SongAdapter(mView.getContext()
                , R.layout.music_list_allsong_item
                , new int[]{R.id.item_song_name,
                SongAdapter.NOCONTROL,
                SongAdapter.NOCONTROL,
                SongAdapter.NOCONTROL,
                SongAdapter.NOCONTROL}
                , mSongs);
//        mView.showAllSong(songAdapter);
    }

    public void destory() {
        mSongs.clear();
        mSongs = null;
    }
}
