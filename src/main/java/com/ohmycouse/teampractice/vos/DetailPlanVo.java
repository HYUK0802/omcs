package com.ohmycouse.teampractice.vos;

import com.ohmycouse.teampractice.entities.DetailPlanEntity;

public class DetailPlanVo extends DetailPlanEntity {
    private String addr1;
    private String firstImage;
    private Double mapX;
    private Double mapY;

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public Double getMapX() {
        return mapX;
    }

    public void setMapX(Double mapX) {
        this.mapX = mapX;
    }

    public Double getMapY() {
        return mapY;
    }

    public void setMapY(Double mapY) {
        this.mapY = mapY;
    }
}
