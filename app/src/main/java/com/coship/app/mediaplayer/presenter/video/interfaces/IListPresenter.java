package com.coship.app.mediaplayer.presenter.video.interfaces;

/**
 * Created by 980558 on 2017/4/21.
 */

public interface IListPresenter {
    void loadFolderList();
    void loadAllVideo();

    void loadVideo(int position);

    void destroy();
}
