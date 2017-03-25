package com.example.a79069.zhihu.data;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by 79069 on 2017/3/23.
 */

public class NewsSimple extends DataSupport {
    private String id;

    private String title;

    private String imageURL;

    private String shareUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return imageURL;
    }

    public void setImage(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
