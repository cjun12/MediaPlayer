package com.coship.app.mediaplayer.model.impl;

import com.coship.app.mediaplayer.bean.video.VideoBean;
import com.coship.app.mediaplayer.model.interfaces.IVideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 980558 on 2017/4/21.
 */

public class VideoModelImpl implements IVideoModel {
    private static IVideoModel mInstance;
    List<VideoBean> mVideoList = null;

    public static IVideoModel getInstance() {
        if (mInstance == null) {
            mInstance = new VideoModelImpl();
        }
        return mInstance;
    }

    private VideoModelImpl() {
        mVideoList = new ArrayList<>();
    }

    @Override
    public VideoBean get(int pos) {
        int length = mVideoList.size();
        if (pos > length - 1) {
            StringBuilder builder = new StringBuilder();
            builder.append("size:").append(length).append(" position:").append(pos);
            throw new IndexOutOfBoundsException(builder.toString());
        } else {
            return mVideoList.get(pos);
        }
    }

    @Override
    public void setVideoList(List<VideoBean> videoList) {
        mVideoList.clear();
        mVideoList.addAll(videoList);
    }

    @Override
    public int size() {
        return mVideoList.size();
    }
}
