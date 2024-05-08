package com.ohmycouse.teampractice.entities;

import java.util.Objects;

public class PlaceEntity {
    private String addr1;
    private int areaCode;
    //재혁이 디질랭? 테이블 지역번호 string으로 해놔서 한참 헤맸자나
    private int contentId;
    private String firstImage;
    private double mapX;
    private double mapY;
    private String title;
    private String overview;

    public String getAddr1() {
        return addr1;
    }

    public PlaceEntity setAddr1(String addr1) {
        this.addr1 = addr1;
        return this;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public PlaceEntity setAreaCode(int areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public int getContentId() {
        return contentId;
    }

    public PlaceEntity setContentId(int contentId) {
        this.contentId = contentId;
        return this;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public PlaceEntity setFirstImage(String firstImage) {
        this.firstImage = firstImage;
        return this;
    }

    public double getMapX() {
        return mapX;
    }

    public PlaceEntity setMapX(double mapX) {
        this.mapX = mapX;
        return this;
    }

    public double getMapY() {
        return mapY;
    }

    public PlaceEntity setMapY(double mapY) {
        this.mapY = mapY;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PlaceEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public PlaceEntity setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceEntity place = (PlaceEntity) o;
        return contentId == place.contentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId);
    }
}
