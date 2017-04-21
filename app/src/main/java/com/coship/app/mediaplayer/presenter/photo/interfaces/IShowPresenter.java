package com.coship.app.mediaplayer.presenter.photo.interfaces;

import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.model.interfaces.IPhotoModel;

/**
 * Created by 980558 on 2017/4/20.
 */

public interface IShowPresenter {
    Photo loadPhoto(int pos);
    boolean isLast(int pos);
}
