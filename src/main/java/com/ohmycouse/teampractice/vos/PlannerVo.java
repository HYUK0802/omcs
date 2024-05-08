package com.ohmycouse.teampractice.vos;

import com.ohmycouse.teampractice.entities.PlannerEntity;

public class PlannerVo extends PlannerEntity {
    private String areaName;
    private String areaImg;

    public String getAreaName() {
        return areaName;
    }

    public PlannerVo setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public String getAreaImg() {
        return areaImg;
    }

    public PlannerVo setAreaImg(String areaImg) {
        this.areaImg = areaImg;
        return this;
    }
}
