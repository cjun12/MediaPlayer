package com.coship.app.mediaplayer.bean.video;

/**
 * Created by 980558 on 2017/4/21.
 */

public class VideoBean {

    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolder(){
        String folder=null;
        if(path!=null){
            folder = path.substring(0,path.lastIndexOf("/"));
        }
        return folder;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
