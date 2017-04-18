package com.coship.app.mediaplayer.bean.music;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 980558 on 2017/4/10.
 */

public class Song implements Serializable {

    private String name;
    private String singer;
    private String album;
    private String path;
    private String albumPath;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public String getFolder() {
        if (path == null) {
            return null;
        }
        int pos = path.lastIndexOf("/");
        if (pos < 0) {
            return null;
        }
        return path.substring(0, pos);
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", path='" + path + '\'' +
                ", albumPath='" + albumPath + '\'' +
                '}';
    }
}
