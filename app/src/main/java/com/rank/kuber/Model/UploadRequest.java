package com.rank.kuber.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;


public class UploadRequest {
    @SerializedName("file")
    public MultipartBody.Part file;
    @SerializedName("conferenceUsersDtoList")
    public List<conferenceUsersDtoList> conferenceUsersDtoList;
    @SerializedName("custId")
    public String custId;
    @SerializedName("callId")
    public String callId;
    @SerializedName("documentTitle")
    public String documentTitle;

    public MultipartBody.Part getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }

    public List<conferenceUsersDtoList> getConferenceUsersDtoList() {
        return conferenceUsersDtoList;
    }

    public void setConferenceUsersDtoList(List<conferenceUsersDtoList> conferenceUsersDtoList) {
        this.conferenceUsersDtoList = conferenceUsersDtoList;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }


}
