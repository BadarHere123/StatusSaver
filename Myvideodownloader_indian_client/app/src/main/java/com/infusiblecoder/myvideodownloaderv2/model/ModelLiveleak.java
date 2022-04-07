package com.infusiblecoder.myvideodownloaderv2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLiveleak {
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
    private Object videoTitle;

    public ModelLiveleak() {
    }

    public ModelLiveleak(Object obj, String str, String str2, String str3, Integer num) {
        this.videoTitle = obj;
        this.originalVideo = str;
        this.mobileAd = str2;
        this.msg = str3;
        this.status = num;
    }

    public Object getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(Object obj) {
        this.videoTitle = obj;
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
