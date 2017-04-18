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
    private String folder;
    private String fileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", folder='" + folder + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
