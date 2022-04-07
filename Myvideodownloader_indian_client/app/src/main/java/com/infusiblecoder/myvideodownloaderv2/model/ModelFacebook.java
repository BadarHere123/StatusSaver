package com.infusiblecoder.myvideodownloaderv2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelFacebook {
    @SerializedName("mobile_ad")
    @Expose
    private String mobileAd;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("original_video")
    @Expose
    private String originalVideo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("video_title")
    @Expose
    private String videoTitle;

    public ModelFacebook() {
    }

    public ModelFacebook(String str, String str2, String str3, String str4, Integer num) {
        this.videoTitle = str;
        this.originalVideo = str2;
        this.mobileAd = str3;
        this.msg = str4;
        this.status = num;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(String str) {
        this.videoTitle = str;
    }

    public String getOriginalVideo() {
        return this.originalVideo;
    }

    public void setOriginalVideo(String str) {
        this.originalVideo = str;
    }

    public String getMobileAd() {
        return this.mobileAd;
    }

    public void setMobileAd(String str) {
        this.mobileAd = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer num) {
        this.status = num;
    }
}
