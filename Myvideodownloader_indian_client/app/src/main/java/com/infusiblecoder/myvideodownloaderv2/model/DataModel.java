package com.infusiblecoder.myvideodownloaderv2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataModel {
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("mobile_ad")
    @Expose
    private String mobile_ad;
    @SerializedName("mp3")
    @Expose
    private String mp3;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("original_video")
    @Expose
    private String originalVideo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("video_title")
    @Expose
    private String videoTitle;
    @SerializedName("watermark_video")
    @Expose
    private String watermarkVideo;

    public DataModel() {
    }

    public DataModel(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, Integer num) {
        this.avatar = str;
        this.nickname = str2;
        this.username = str3;
        this.videoTitle = str4;
        this.originalVideo = str5;
        this.watermarkVideo = str6;
        this.status = num;
        this.msg = str7;
        this.mp3 = str9;
        this.mobile_ad = str8;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String str) {
        this.nickname = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(String str) {
        this.videoTitle = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getMobile_ad() {
        return this.mobile_ad;
    }

    public void setMobile_ad(String str) {
        this.mobile_ad = str;
    }

    public String getmp3() {
        return this.mp3;
    }

    public void setmp3(String str) {
        this.mp3 = str;
    }

    public String getOriginalVideo() {
        return this.originalVideo;
    }

    public void setOriginalVideo(String str) {
        this.originalVideo = str;
    }

    public String getWatermarkVideo() {
        return this.watermarkVideo;
    }

    public void setWatermarkVideo(String str) {
        this.watermarkVideo = str;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer num) {
        this.status = num;
    }
}
