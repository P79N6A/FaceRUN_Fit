package com.fly.run.bean;

import java.io.Serializable;
import java.util.Date;

public class FitBean implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.image
     *
     * @mbg.generated
     */
    private String image;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.video
     *
     * @mbg.generated
     */
    private String video;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.videocover
     *
     * @mbg.generated
     */
    private String videocover;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.videoduration
     *
     * @mbg.generated
     */
    private String videoduration;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fit.time
     *
     * @mbg.generated
     */
    private Date time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fit
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.id
     *
     * @return the value of fit.id
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.id
     *
     * @param id the value for fit.id
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.title
     *
     * @return the value of fit.title
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.title
     *
     * @param title the value for fit.title
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.description
     *
     * @return the value of fit.description
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.description
     *
     * @param description the value for fit.description
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.image
     *
     * @return the value of fit.image
     * @mbg.generated
     */
    public String getImage() {
        return image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.image
     *
     * @param image the value for fit.image
     * @mbg.generated
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.video
     *
     * @return the value of fit.video
     * @mbg.generated
     */
    public String getVideo() {
        return video;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.video
     *
     * @param video the value for fit.video
     * @mbg.generated
     */
    public void setVideo(String video) {
        this.video = video == null ? null : video.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.videocover
     *
     * @return the value of fit.videocover
     * @mbg.generated
     */
    public String getVideocover() {
        return videocover;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.videocover
     *
     * @param videocover the value for fit.videocover
     * @mbg.generated
     */
    public void setVideocover(String videocover) {
        this.videocover = videocover == null ? null : videocover.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.videoduration
     *
     * @return the value of fit.videoduration
     * @mbg.generated
     */
    public String getVideoduration() {
        return videoduration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.videoduration
     *
     * @param videoduration the value for fit.videoduration
     * @mbg.generated
     */
    public void setVideoduration(String videoduration) {
        this.videoduration = videoduration == null ? null : videoduration.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fit.time
     *
     * @return the value of fit.time
     * @mbg.generated
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fit.time
     *
     * @param time the value for fit.time
     * @mbg.generated
     */
    public void setTime(Date time) {
        this.time = time;
    }
}