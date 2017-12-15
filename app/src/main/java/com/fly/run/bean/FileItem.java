package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class FileItem implements Serializable {
    public String imageId;
    public String fileName;
    public String filePath;
    public boolean isCheck = false;

    public FileItem(String imageId, String filePath, String fileName){
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getImageId() {
        return imageId;
    }

    public FileItem setImageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileItem setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public FileItem setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public FileItem setCheck(boolean check) {
        isCheck = check;
        return this;
    }
}
