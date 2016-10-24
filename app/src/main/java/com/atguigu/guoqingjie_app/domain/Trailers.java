package com.atguigu.guoqingjie_app.domain;

import java.util.List;

/**
 * Created by xinpengfei on 2016/10/7.
 * <p>
 * Function :
 */

public class Trailers {

    private int id;

    private String movieName;

    private String coverImg;

    private int movieId;

    private String url;

    private String hightUrl;

    private String videoTitle;

    private int videoLength;

    private int rating;

    private List<String> type ;

    private String summary;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setMovieName(String movieName){
        this.movieName = movieName;
    }
    public String getMovieName(){
        return this.movieName;
    }
    public void setCoverImg(String coverImg){
        this.coverImg = coverImg;
    }
    public String getCoverImg(){
        return this.coverImg;
    }
    public void setMovieId(int movieId){
        this.movieId = movieId;
    }
    public int getMovieId(){
        return this.movieId;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setHightUrl(String hightUrl){
        this.hightUrl = hightUrl;
    }
    public String getHightUrl(){
        return this.hightUrl;
    }
    public void setVideoTitle(String videoTitle){
        this.videoTitle = videoTitle;
    }
    public String getVideoTitle(){
        return this.videoTitle;
    }
    public void setVideoLength(int videoLength){
        this.videoLength = videoLength;
    }
    public int getVideoLength(){
        return this.videoLength;
    }
    public void setRating(int rating){
        this.rating = rating;
    }
    public int getRating(){
        return this.rating;
    }
    public void setString(List<String> type){
        this.type = type;
    }
    public List<String> getString(){
        return this.type;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getSummary(){
        return this.summary;
    }
}
