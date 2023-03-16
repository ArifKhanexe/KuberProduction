package com.rank.kuber.Model;

public class HangUpCustomerResponse {

    /**
     * resStatus : false
     * resMessage : FAILED
     */

    private PayloadBean payload;
    /**
     * payload : {"resStatus":false,"resMessage":"FAILED"}
     * error : null
     * status : true
     */

    private Object error;
    private boolean status;

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class PayloadBean {
        private boolean resStatus;
        private String resMessage;

        public boolean isResStatus() {
            return resStatus;
        }

        public void setResStatus(boolean resStatus) {
            this.resStatus = resStatus;
        }

        public String getResMessage() {
            return resMessage;
        }

        public void setResMessage(String resMessage) {
            this.resMessage = resMessage;
        }
    }
}
