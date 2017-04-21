package com.coship.app.mediaplayer.presenter.photo.impl;

import android.util.Log;

import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.model.impl.PhotoModelImpl;
import com.coship.app.mediaplayer.model.interfaces.IPhotoModel;
import com.coship.app.mediaplayer.presenter.photo.interfaces.IShowPresenter;
import com.coship.app.mediaplayer.view.interfaces.photo.IShowPhotoView;


/**
 * Created by 980558 on 2017/4/20.
 */

public class ShowPresenterImpl implements IShowPresenter {
    private static final String TAG = "ShowPresenterImpl";
    private IShowPhotoView mView;
    private IPhotoModel mPhotoModel;
    public ShowPresenterImpl(IShowPhotoView view){
        mView = view;
        mPhotoModel = PhotoModelImpl.getInstance();
    }

    @Override
    public Photo loadPhoto(int pos) {
        return mPhotoModel.get(pos);
    }

    @Override
    public boolean isLast(int pos) {
        Log.i(TAG, "isLast: "+pos);
        pos+=1;
        return mPhotoModel.size() == pos;
    }
}
