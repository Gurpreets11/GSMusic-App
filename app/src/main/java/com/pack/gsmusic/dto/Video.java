package com.pack.gsmusic.dto;

public class Video {
    public String id;
    public String title;
    public String url;
    public String playlistId;

    public Video() {
    }

    public Video(String id, String title, String url, String playlistId) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.playlistId = playlistId;
    }
}

