package com.example.angela;

public class DetailPost {
    int id;
    String title;
    String content;
    int cruCnt;
    String startDate;
    String startPoint;
    double startLat;
    double startLng;
    String endPoint;
    double endLat;
    double endLng;
    int cmtCnt;
    String regDate;
    String profileUrl;
    String uId;
    int cmId;
    String cmContent;
    int cmDepth;
    String cmregDate;
    String cmprofilUrl;
    String cmUid;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCruCnt() {
        return cruCnt;
    }

    public void setCruCnt(int cruCnt) {
        this.cruCnt = cruCnt;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public int getCmtCnt() {
        return cmtCnt;
    }

    public void setCmtCnt(int cmtCnt) {
        this.cmtCnt = cmtCnt;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }



    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getCmId() {
        return cmId;
    }

    public void setCmId(int cmId) {
        this.cmId = cmId;
    }

    public String getCmContent() {
        return cmContent;
    }

    public void setCmContent(String cmContent) {
        this.cmContent = cmContent;
    }

    public int getCmDepth() {
        return cmDepth;
    }

    public void setCmDepth(int cmDepth) {
        this.cmDepth = cmDepth;
    }

    public String getCmregDate() {
        return cmregDate;
    }

    public void setCmregDate(String cmregDate) {
        this.cmregDate = cmregDate;
    }

    public String getUid() {
        return uId;
    }

    public void setUid(String uId) {
        this.uId = uId;
    }

    public String getCmprofilUrl() {
        return cmprofilUrl;
    }

    public void setCmprofilUrl(String cmprofilUrl) {
        this.cmprofilUrl = cmprofilUrl;
    }

    public String getCmUid() {
        return cmUid;
    }

    public void setCmUid(String cmUid) {
        this.cmUid = cmUid;
    }

    @Override
    public String toString() {
        return "DetailPost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", cruCnt=" + cruCnt +
                ", startDate='" + startDate + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", startLat=" + startLat +
                ", startLng=" + startLng +
                ", endPoint='" + endPoint + '\'' +
                ", endLat=" + endLat +
                ", endLng=" + endLng +
                ", cmtCnt=" + cmtCnt +
                ", regDate='" + regDate + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", uId='" + uId + '\'' +
                ", cmId=" + cmId +
                ", cmContent='" + cmContent + '\'' +
                ", cmDepth=" + cmDepth +
                ", cmregDate='" + cmregDate + '\'' +
                ", cmprofilUrl='" + cmprofilUrl + '\'' +
                ", cmUid='" + cmUid + '\'' +
                '}';
    }
}
