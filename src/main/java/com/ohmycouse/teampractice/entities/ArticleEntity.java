package com.ohmycouse.teampractice.entities;

import java.util.Date;
import java.util.Objects;

public class ArticleEntity {
    private int index;
    private String email;
    private String nickname;
    private String title;
    private String content;
    private int view;
    private Date createdAt;
    private boolean isDeleted;
    private String clientIp;
    private String clientUa;

    public int getIndex() {
        return index;
    }

    public ArticleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ArticleEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public ArticleEntity setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public int getView() {
        return view;
    }

    public ArticleEntity setView(int view) {
        this.view = view;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ArticleEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public ArticleEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public ArticleEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getClientUa() {
        return clientUa;
    }

    public ArticleEntity setClientUa(String clientUa) {
        this.clientUa = clientUa;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleEntity that = (ArticleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
