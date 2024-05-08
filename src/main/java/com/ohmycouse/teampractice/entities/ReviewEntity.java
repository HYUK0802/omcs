package com.ohmycouse.teampractice.entities;

import java.util.Date;
import java.util.Objects;

public class ReviewEntity {
    private int index;
    private int contentId;
    private String nickname;
    private String content;
    private boolean isDeleted;
    private Date createdAt;
    private int score;
    private String clientIp;
    private String clientUa;

    public int getIndex() {
        return index;
    }

    public ReviewEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getContentId() {
        return contentId;
    }

    public ReviewEntity setContentId(int contentId) {
        this.contentId = contentId;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public ReviewEntity setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ReviewEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public ReviewEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ReviewEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getScore() {
        return score;
    }

    public ReviewEntity setScore(int score) {
        this.score = score;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public ReviewEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getClientUa() {
        return clientUa;
    }

    public ReviewEntity setClientUa(String clientUa) {
        this.clientUa = clientUa;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntity that = (ReviewEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
