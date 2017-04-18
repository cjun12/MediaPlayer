package com.coship.app.mediaplayer.view.interfaces.photo;

import android.content.Context;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

import com.coship.app.mediaplayer.view.adapter.PhotoListAdapter;

/**
 * Created by 980558 on 2017/4/5.
 */

public interface IListView {
    Context getContext();
    void showAllPhotoList();
    void showFolderList(SimpleAdapter adapter);
    void showPhoto(PhotoListAdapter photoAdapter);
}
