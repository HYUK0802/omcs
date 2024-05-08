package com.ohmycouse.teampractice.entities;

import java.util.Objects;


public class MyPickEntity {
    private int index;
    private int areaCode;
    private String addr1;
    private String firstImage;
    private int contentId;
    private String email;
    private String title;

    public int getIndex() {
        return index;
    }

    public MyPickEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public MyPickEntity setAreaCode(int areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getAddr1() {
        return addr1;
    }

    public MyPickEntity setAddr1(String addr1) {
        this.addr1 = addr1;
        return this;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public MyPickEntity setFirstImage(String firstImage) {
        this.firstImage = firstImage;
        return this;
    }

    public int getContentId() {
        return contentId;
    }

    public MyPickEntity setContentId(int contentId) {
        this.contentId = contentId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MyPickEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MyPickEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPickEntity that = (MyPickEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
