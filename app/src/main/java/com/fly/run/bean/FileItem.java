package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class FileItem implements Serializable {
    public String imageId;
    public String fileName;
    public String filePath;
    public String fileType;
    public int fileDuration;
    public long fileDate;
    public boolean isCheck = false;

    public FileItem(String imageId, String filePath, String fileName){
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public FileItem(String imageId, String filePath, String fileName,long fileDate,String type){
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileType = type;
    }

    public FileItem(String imageId, String filePath, String fileName,long fileDate,String type,int fileDuration){
        this.imageId = imageId;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.fileType = type;
        this.fileDuration = fileDuration;
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

    public long getFileDate() {
        return fileDate;
    }

    public void setFileDate(long fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(int fileDuration) {
        this.fileDuration = fileDuration;
    }
}
