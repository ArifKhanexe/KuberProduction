package com.rank.kuber.Model;

public class SaveFeedbackRequest {

    /**
     * cust_id : Cust_5DFZixsyaYrVo1m
     * callId : 2938
     * question1Val : 5
     * question2Val : 5
     * question3Val : 5
     * comment : abc
     */

    private String cust_id;
    private String callId;
    private String question1Val;
    private String question2Val;
    private String question3Val;
    private String comment;

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getQuestion1Val() {
        return question1Val;
    }

    public void setQuestion1Val(String question1Val) {
        this.question1Val = question1Val;
    }

    public String getQuestion2Val() {
        return question2Val;
    }

    public void setQuestion2Val(String question2Val) {
        this.question2Val = question2Val;
    }

    public String getQuestion3Val() {
        return question3Val;
    }

    public void setQuestion3Val(String question3Val) {
        this.question3Val = question3Val;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
