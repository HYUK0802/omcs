package com.ohmycouse.teampractice.vos;

import com.ohmycouse.teampractice.entities.ArticlesEntity;

import java.util.Date;

public class ArticleVo extends ArticlesEntity {
    private String firstImage;
    private String firstText;
    private String firstCreatedAt;
    private int likeCount;

    public String getFirstImage() {
        return firstImage;
    }
    public ArticleVo setFirstImage(String firstImage) {
        this.firstImage = firstImage;
        return this;
    }

    public String getFirstText() {
        return firstText;
    }

    public ArticleVo setFirstText(String firstText) {
        this.firstText = firstText;
        return this;
    }

    public String getFirstCreatedAt() {
        return firstCreatedAt;
    }

    public ArticleVo setFirstCreatedAt(String firstCreatedAt) {
        this.firstCreatedAt = firstCreatedAt;
        return this;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public ArticleVo setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }
}