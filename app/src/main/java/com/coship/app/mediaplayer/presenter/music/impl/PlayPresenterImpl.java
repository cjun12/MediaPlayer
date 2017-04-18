package com.coship.app.mediaplayer.presenter.music.impl;

import com.coship.app.mediaplayer.bean.music.Song;
import com.coship.app.mediaplayer.presenter.music.interfaces.IPlayPresenter;

/**
 * Created by 980558 on 2017/4/14.
 */

public class PlayPresenterImpl implements IPlayPresenter{
    @Override
    public void loadImage(Song song) {
        String id = song.getAlbumId();

    }
}
