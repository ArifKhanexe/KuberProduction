package com.rank.kuber.Model;

public class ChatModel {

    private String senderId, msg, time;
    private Boolean isLeft = false;

    public Boolean getLeft() {
        return isLeft;
    }

    public void setLeft(Boolean left) {
        isLeft = left;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
