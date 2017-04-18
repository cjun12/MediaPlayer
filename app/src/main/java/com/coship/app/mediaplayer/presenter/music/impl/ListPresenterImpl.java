package com.coship.app.mediaplayer.presenter.music.impl;

import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.coship.app.mediaplayer.bean.music.Song;
import com.coship.app.mediaplayer.presenter.music.interfaces.IListPresenter;
import com.coship.app.mediaplayer.view.interfaces.music.IListView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by 980558 on 2017/4/10.
 */

public class ListPresenterImpl implements IListPresenter {
    IListView view;

    List<Song> songs = new ArrayList<>();
    List<Song> curList = new ArrayList<>();

    public ListPresenterImpl(IListView view) {
        this.view = view;
    }


    @Override
    public void loadAllMusic() {
        songs.clear();
        Cursor cursor = view.getContext().getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null, null);
        Log.i(TAG, "loadAllMusic: " + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        while (cursor.moveToNext()) {
            Song song = new Song();
            song.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            song.setSinger(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            song.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            songs.add(song);
        }
        cursor.close();
        curList = songs;
        view.showAllSong(songs);
    }


    @Override
    public void loadField(String fieldName) {
        Map<String, Integer> fieldMap = getField(fieldName);
        view.showField(fieldMap, fieldName);
    }


    @Override
    public void resetList() {
        curList = songs;
    }

    public Map<String, Integer> getField(String fieldName) {
        Map<String, Integer> result = new HashMap<>();
        for (Song song : songs) {
            try {
                String field = (String) Song.class.getMethod("get" + fieldName).invoke(song);
                if (result.get(field) == null) {
                    result.put(field, 1);
                } else {
                    int cnt = result.get(field);
                    result.put(field, ++cnt);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Song getPath(String whois, int position) {
        return songs.get(position);
    }

    @Override
    public List<Song> getList() {
        return curList;
    }

    @Override
    public boolean getPosition() {
        return false;
    }

    @Override
    public void loadFieldSong(String key, String value) {
        curList = new ArrayList<>();
        try {
            for (Song song : songs) {
                String fieldValue = Song.class.getMethod("get" + key).invoke(song).toString();
                Log.i(TAG, "loadFieldSong: "+song);
                if (fieldValue != null && fieldValue.equals(value)) {
                    curList.add(song);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
