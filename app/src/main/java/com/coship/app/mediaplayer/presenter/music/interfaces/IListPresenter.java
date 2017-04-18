package com.coship.app.mediaplayer.presenter.music.interfaces;

import com.coship.app.mediaplayer.bean.music.Song;

import java.util.List;
import java.util.Map;

/**
 * Created by 980558 on 2017/4/10.
 */

public interface IListPresenter {
    void loadAllMusic();
    void loadField(String fieldName);
    Song getPath(String whois , int position);

    List<Song> getList();

    boolean getPosition();

    Map<String, Integer> getField(String fieldName);
    void loadFieldSong(String key, String value);

    void resetList();
}
