package com.coship.app.mediaplayer.view.interfaces.music;

import android.content.Context;
import android.widget.Adapter;

import com.coship.app.mediaplayer.bean.music.Song;

import java.util.List;
import java.util.Map;

/**
 * Created by 980558 on 2017/4/10.
 */

public interface IListView {
    Context getContext();
    void showAllSong(List<Song> songs);

    void showField(Map<String, Integer> albumMap, String field);
    void showPlayer(int position);
}
