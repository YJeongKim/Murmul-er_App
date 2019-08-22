package com.murmuler.organicstack.com.murmuler.organicstack.vo;

import java.math.BigDecimal;
import java.sql.Date;

public class RoomSummaryViewVO {
    private int roomId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String postType;
    private String title;
    private String sido;
    private String sigungu;
    private String roadname;
    private int periodNum;
    private String periodUnit;
    private String roomType;
    private String rentType;
    private double area;
    private int deposit;
    private int monthlyCost;
    private int manageCost;
    private Date writeDate;
    private int views;
    private String roomImg;

    public RoomSummaryViewVO() { }

    public RoomSummaryViewVO(int roomId, BigDecimal latitude, BigDecimal longitude, String postType, String title, String sido, String sigungu, String roadname, int periodNum, String periodUnit, String roomType, String rentType, double area, int deposit, int monthlyCost, int manageCost, Date writeDate, int views, String roomImg) {
        this.roomId = roomId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postType = postType;
        this.title = title;
        this.sido = sido;
        this.sigungu = sigungu;
        this.roadname = roadname;
        this.periodNum = periodNum;
        this.periodUnit = periodUnit;
        this.roomType = roomType;
        this.rentType = rentType;
        this.area = area;
        this.deposit = deposit;
        this.monthlyCost = monthlyCost;
        this.manageCost = manageCost;
        this.writeDate = writeDate;
        this.views = views;
        this.roomImg = roomImg;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSido() {
        return sido;
    }

    public void setSido(String sido) {
        this.sido = sido;
    }

    public String getSigungu() {
        return sigungu;
    }

    public void setSigungu(String sigungu) {
        this.sigungu = sigungu;
    }

    public String getRoadname() {
        return roadname;
    }

    public void setRoadname(String roadname) {
        this.roadname = roadname;
    }

    public int getPeriodNum() {
        return periodNum;
    }

    public void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public int getManageCost() {
        return manageCost;
    }

    public void setManageCost(int manageCost) {
        this.manageCost = manageCost;
    }

    public Date getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }
}
