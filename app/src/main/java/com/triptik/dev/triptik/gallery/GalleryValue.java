package com.triptik.dev.triptik.gallery;

import java.lang.String;

public class GalleryValue {

    private String username, triptikTitle, triptikID, creation_date, creation_time, userID, commentTotal, likesTotal;

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getTriptikTitle() {
        return triptikTitle;
    }

    public void setTriptikTitle(String triptikTitle) {
        this.triptikTitle = triptikTitle;
    }

    public String getTriptikID() {
        return triptikID;
    }

    public void setTriptikID(String triptikID) {
        this.triptikID = triptikID;
    }

    public String getCreationDate() {
        return creation_date;
    }

    public void setCreationDate(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getCreationTime() {
        return creation_time;
    }

    public void setCreationTime(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCommentTotal() {
        return commentTotal;
    }

    public void setCommentTotal(String commentTotal) {
        this.commentTotal = commentTotal;
    }

    public String getLikesTotal() {
        return likesTotal;
    }

    public void setLikesTotal(String likesTotal) {
        this.likesTotal = likesTotal;
    }
}
