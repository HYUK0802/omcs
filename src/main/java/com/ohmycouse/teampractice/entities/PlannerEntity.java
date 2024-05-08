package com.ohmycouse.teampractice.entities;

import java.util.Date;
import java.util.Objects;

public class PlannerEntity {
    private int index;
    private String email;
    private int areaCode;
    private Date createAt;
    private String plannerTitle;
    private Date startDate;
    private Date endDate;
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public PlannerEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public PlannerEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PlannerEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public PlannerEntity setAreaCode(int areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public PlannerEntity setCreateAt(Date createAt) {
        this.createAt = createAt;
        return this;
    }

    public String getPlannerTitle() {
        return plannerTitle;
    }

    public PlannerEntity setPlannerTitle(String plannerTitle) {
        this.plannerTitle = plannerTitle;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public PlannerEntity setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PlannerEntity setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlannerEntity that = (PlannerEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
