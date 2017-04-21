package com.coship.app.mediaplayer.bean.photo;

/**
 * Created by 980558 on 2017/4/17.
 */

public class Photo {

    /**
     * id :
     * folder :
     * fileName :
     */

    private String id;
    private String path;
    private String thumbPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getFolder() {
        String folder=null;
        if(path!=null){
            folder = path.substring(0,path.lastIndexOf("/"));
        }
        return folder;
    }
    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                '}';
    }

}
