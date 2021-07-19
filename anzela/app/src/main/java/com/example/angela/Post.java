package com.example.angela;

public class Post {
    private int id;
    private String title;
    private String content;
    private int curCnt;
    private String startDate;
    private String startPoint;
    private double startLat;
    private double startLng;
    private String endPoint;
    private double endLat;
    private double endLng;
    private int cmtCnt;
    private String regDate;
    private String uid;
    private String profileUrl;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCurCnt() {
        return curCnt;
    }

    public void setCurCnt(int curCnt) {
        this.curCnt = curCnt;
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

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", curCnt=" + curCnt +
                ", startDate='" + startDate + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", startLat=" + startLat +
                ", startLng=" + startLng +
                ", endPoint='" + endPoint + '\'' +
                ", endLat=" + endLat +
                ", endLng=" + endLng +
                ", cmtCnt=" + cmtCnt +
                ", regDate='" + regDate + '\'' +
                ", uId='" + uid + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }


}
