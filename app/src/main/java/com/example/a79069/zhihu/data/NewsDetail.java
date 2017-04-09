package com.example.a79069.zhihu.data;

import java.util.ArrayList;

/**
 * Created by 79069 on 2017/3/23.
 */

public class NewsDetail {
    private String id;

    private String body;

    /**
     * 图片版权信息
     */
    private String imageSource;

    private String title;

    private String imageURL;

    private String shareURL;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 这篇文章的推荐者
     */
    private ArrayList<String> recommenderList = new ArrayList<>();

    private String cssURL;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public ArrayList<String> getRecommenderList() {
        return recommenderList;
    }

    public void setRecommenderList(ArrayList<String> recommenderList) {
        this.recommenderList = recommenderList;
    }

    public String getCssURL() {
        return cssURL;
    }

    public void setCssURL(String cssURL) {
        this.cssURL = cssURL;
    }
}
