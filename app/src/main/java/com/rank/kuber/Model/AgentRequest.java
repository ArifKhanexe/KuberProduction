package com.rank.kuber.Model;

public class AgentRequest {

    /**
     * loginId : wrqr2T3QY
     * category : Urgent
     * language : English
     * service : LOAN
     * callOption : apurva
     * location : KOLKATA
     * latitude : 88
     * longitude : 99
     */

    private String loginId;
    private String category;
    private String language;
    private String service;
    private String callOption;
    private String location;
    private String latitude;
    private String longitude;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCallOption() {
        return callOption;
    }

    public void setCallOption(String callOption) {
        this.callOption = callOption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
