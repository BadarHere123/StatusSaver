package com.infusiblecoder.myvideodownloaderv2.model;

import java.util.ArrayList;

public class TikTokNoWaterMarkApi {


    public String
            status,
            name,
            profileurl,
            username,
            flag,
            thumbnailUrl,
            songurl,
            watermark_removed,
            videourl,
            ogvideourl,
            musictitle,
            musicauthor,
            musiccover,
            musicplayurl,
            musicflag,
            musicurl,
            video_full_title;

    public ArrayList<String> profile_pic_url;


    public TikTokNoWaterMarkApi(String status, String name, String profileurl, String username, String flag, String thumbnailUrl, String songurl, String watermark_removed, String videourl, String ogvideourl, String musictitle, String musicauthor, String musiccover, String musicplayurl, String musicflag, String musicurl, String video_full_title, ArrayList<String> profile_pic_url) {
        this.status = status;
        this.name = name;
        this.profileurl = profileurl;
        this.username = username;
        this.flag = flag;
        this.thumbnailUrl = thumbnailUrl;
        this.songurl = songurl;
        this.watermark_removed = watermark_removed;
        this.videourl = videourl;
        this.ogvideourl = ogvideourl;
        this.musictitle = musictitle;
        this.musicauthor = musicauthor;
        this.musiccover = musiccover;
        this.musicplayurl = musicplayurl;
        this.musicflag = musicflag;
        this.musicurl = musicurl;
        this.video_full_title = video_full_title;
        this.profile_pic_url = profile_pic_url;
    }
}