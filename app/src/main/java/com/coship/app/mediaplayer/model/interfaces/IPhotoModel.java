package com.coship.app.mediaplayer.model.interfaces;

import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.model.impl.PhotoModelImpl;

import java.util.List;

/**
 * Created by 980558 on 2017/4/20.
 */

public interface IPhotoModel {
    Photo get(int pos);
    void setPhotoList(List<Photo> photoList);
    int size();
}
