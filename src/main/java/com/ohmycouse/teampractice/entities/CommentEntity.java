package com.ohmycouse.teampractice.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CommentEntity {
    private int index;
    private int articleIndex;
    private Integer commentIndex;
    private String content;
    private String nickname;
    private boolean isDeleted;
    private Date createdAt;
    private String clientIp;
    private String clientUa;


    public int getIndex() {
        return index;
    }

    public CommentEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public CommentEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public Integer getCommentIndex() {
        return commentIndex;
    }

    public void setCommentIndex(Integer commentIndex) {
        this.commentIndex = commentIndex;
    }

    public String getContent() {
        return content;
    }

    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public CommentEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public CommentEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }


    public String getClientIp() {
        return clientIp;
    }

    public CommentEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getClientUa() {
        return clientUa;
    }

    public CommentEntity setClientUa(String clientUa) {
        this.clientUa = clientUa;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
    public String getFormattedCreatedAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return sdf.format(createdAt);
    }
}
