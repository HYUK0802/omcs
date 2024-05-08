package com.ohmycouse.teampractice.entities;

public class CourseEntity {
    private String addr1;
    private int contentId;
    private double mapX;
    private double mapY;
    private String title;

    public String getAddr1() {
        return addr1;
    }

    public CourseEntity setAddr1(String addr1) {
        this.addr1 = addr1;
        return this;
    }

    public int getContentId() {
        return contentId;
    }

    public CourseEntity setContentId(int contentId) {
        this.contentId = contentId;
        return this;
    }

    public double getMapX() {
        return mapX;
    }

    public CourseEntity setMapX(double mapX) {
        this.mapX = mapX;
        return this;
    }

    public double getMapY() {
        return mapY;
    }

    public CourseEntity setMapY(double mapY) {
        this.mapY = mapY;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CourseEntity setTitle(String title) {
        this.title = title;
        return this;
    }

}
