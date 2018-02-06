package com.vssh.tmdbdemo.models;

/**
 * Semi-generated Bean-style class corresponding to a single Movie result from TMDb.
 */

public class MovieItem extends GenericItem {
    /**
     * poster_path : /IfB9hy4JH1eH6HEfIgIGORXi5h.jpg
     * adult : false
     * overview : Jack Reacher must uncover the truth behind a major government conspiracy in order to clear his name. On the run as a fugitive from the law, Reacher uncovers a potential secret from his past that could change his life forever.
     * release_date : 2016-10-19
     * genre_ids : [53,28,80,18,9648]
     * id : 343611
     * original_title : Jack Reacher: Never Go Back
     * original_language : en
     * title : Jack Reacher: Never Go Back
     * backdrop_path : /4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg
     * popularity : 26.818468
     * vote_count : 201
     * video : false
     * vote_average : 4.19
     */

    private boolean adult;
    private String release_date;
    private String original_title;
    private String title;
    private boolean video;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
