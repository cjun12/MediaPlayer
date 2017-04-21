package com.coship.app.mediaplayer.model.impl;

import com.coship.app.mediaplayer.bean.photo.Photo;
import com.coship.app.mediaplayer.model.interfaces.IPhotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 980558 on 2017/4/20.
 */

public class PhotoModelImpl implements IPhotoModel{
    private static IPhotoModel mInstance ;
    List<Photo> mPhotoList =null;
    public static IPhotoModel getInstance(){
        if (mInstance == null){
            mInstance = new PhotoModelImpl();
        }
        return mInstance;
    }

    private PhotoModelImpl(){
        mPhotoList = new ArrayList<>();
    }

    public Photo get(int pos){
        int length = mPhotoList.size();
        if(pos>length-1)
        {
            StringBuilder builder = new StringBuilder();
            builder.append("size:").append(length).append(" position:").append(pos);
            throw new IndexOutOfBoundsException(builder.toString());
        }else
        {
            return mPhotoList.get(pos);
        }
    }

    public void setPhotoList(List<Photo> photoList){
        mPhotoList.clear();
        mPhotoList.addAll(photoList);
    }

    public int size(){
        return mPhotoList.size();
    }
    public void clear(){
        mPhotoList.clear();
    }
}
