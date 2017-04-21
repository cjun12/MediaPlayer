package com.coship.app.mediaplayer.model.interfaces;

import com.coship.app.mediaplayer.bean.video.VideoBean;

import java.util.List;

/**
 * Created by 980558 on 2017/4/21.
 */

public interface IVideoModel {
    VideoBean get(int pos);
    void setVideoList(List<VideoBean> videoList);
    int size();
}
