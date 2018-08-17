package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class ImageUrlWHBean implements Serializable {

    public String imageUrl;
    public int imageWidth;
    public int imageHeight;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
}
