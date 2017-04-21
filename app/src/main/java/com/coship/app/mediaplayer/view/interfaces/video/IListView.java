package com.coship.app.mediaplayer.view.interfaces.video;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.view.adapter.PhotoListAdapter;
import com.coship.app.mediaplayer.view.adapter.VideoListAdapter;

/**
 * Created by 980558 on 2017/4/21.
 */

public interface IListView {
    Context getContext();
    void showFolderList(SimpleAdapter adapter);
    void showVideo(VideoListAdapter photoAdapter);

}
