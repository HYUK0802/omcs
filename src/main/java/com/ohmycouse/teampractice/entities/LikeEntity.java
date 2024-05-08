package com.ohmycouse.teampractice.entities;

import java.util.Objects;

public class LikeEntity {
    private int index;
    private int articleIndex;
    private String nickname;
    private int likes;

    public int getIndex() {
        return index;
    }

    public LikeEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public LikeEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public LikeEntity setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public int getLikes() {
        return likes;
    }

    public LikeEntity setLikes(int likes) {
        this.likes = likes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntity that = (LikeEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
