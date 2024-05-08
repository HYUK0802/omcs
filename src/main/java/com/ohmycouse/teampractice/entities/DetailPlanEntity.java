package com.ohmycouse.teampractice.entities;

import java.util.Date;
import java.util.Objects;

public class DetailPlanEntity {
    private int index;
    private int tourIndex;
    private String title;
    private String email;
    private Date date;
    private int day;
    private String memo;
    public int getIndex() {
        return index;
    }

    public DetailPlanEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getTourIndex() {
        return tourIndex;
    }

    public DetailPlanEntity setTourIndex(int tourIndex) {
        this.tourIndex = tourIndex;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DetailPlanEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public DetailPlanEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public DetailPlanEntity setDate(Date date) {
        this.date = date;
        return this;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public String getMemo() {
        return memo;
    }

    public DetailPlanEntity setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailPlanEntity that = (DetailPlanEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }


    @Override
    public String toString() {
        return "DetailPlanEntity{" +
                "date=" + date +
                ", title='" + title + '\'' +
                // 다른 속성들도 추가가능
                '}';
    }
}
