package com.coship.app.mediaplayer.presenter.photo.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by 980558 on 2017/4/5.
 */

public interface IListPresenter{
    List<Map<String, String>> loadAllPhotoList();
    void loadFolderList();
    void loadfileList(String path);
    void loadAllPhoto();

    void loadPhoto(int position);
}
