package com.coship.app.mediaplayer.presenter.photo.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by 980558 on 2017/4/5.
 */

public interface IListPresenter{
    void loadFolderList();
    void loadAllPhoto();

    void loadPhoto(int position);

    void destroy();
}
