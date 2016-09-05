package com.triptik.dev.triptik.comment;

public class CommentValue {

    private String commentText, triptikID, creation_date, creation_time, userID, username;
    private int commentID, isVisible;

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getTriptikID() {
        return triptikID;
    }

    public void setTriptikID(String triptikID) {
        this.triptikID = triptikID;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getIsVisible() {
        return isVisible;
    }


}



