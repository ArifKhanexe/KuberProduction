package com.rank.kuber.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class conferenceUsersDtoList {

    /**
     * id : 3
     * loginId : cc1
     */
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("loginId")
    @Expose
    private String loginId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
