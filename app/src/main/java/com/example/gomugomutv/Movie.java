package com.example.gomugomutv;

import androidx.annotation.NonNull;

import java.io.Serializable;

import java.util.ArrayList;

/*
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
public class Movie implements Serializable {
    static final long serialVersionUID = 727566175075960653L;
    private long id;
    private String title;
    private String description;
    private String bgImageUrl;
    private String cardImageUrl;
    private ArrayList<String> videoUrls = new ArrayList<>();
    private String studio;
    private String source;
    private boolean newPageRequester = false;


    public Movie() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudio() {
        System.out.println("STUDIOOOOOOOO");
        return studio;
    }


    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getVideoUrl(int index) {
        return videoUrls.get(index);
    }

    public int getVideoUrlSize() {
        return videoUrls.size();
    }

    public void setVideoUrlArrayList(ArrayList<String> list) {
        this.videoUrls = list;
    }

    public void setVideoUrl(String videoUrl) {
        if (videoUrls.size()>0)
            this.videoUrls.set(0, videoUrl);
        else
            this.videoUrls.add(videoUrl);
    }

    public void addVideoUrl(String videoUrl) {
        this.videoUrls.add(videoUrl);
    }

    public String getBackgroundImageUrl() {
        return bgImageUrl;
    }

    public void setBackgroundImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public void setSource(String source) { this.source = source; }

    public void setRequester(boolean bool) { this.newPageRequester = bool; }

    public boolean isRequester() { return newPageRequester; }

    public String getSource() { return source; }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrls + '\'' +
                ", backgroundImageUrl='" + bgImageUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}