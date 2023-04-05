package com.rank.kuber.Model;

public class ServiceDownTimeResponse {


    /**
     * downTimeCheck : false
     */

    private PayloadBean payload;
    /**
     * errorCode : 412
     * errorMessage : We are unavailable
     */

    private ErrorBean error;
    /**
     * payload : {"downTimeCheck":"false"}
     * error : {"errorCode":412,"errorMessage":"We are unavailable "}
     * status : true
     */

    private boolean status;

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class PayloadBean {
        private String downTimeCheck;

        public String getDownTimeCheck() {
            return downTimeCheck;
        }

        public void setDownTimeCheck(String downTimeCheck) {
            this.downTimeCheck = downTimeCheck;
        }
    }

    public static class ErrorBean {
        private int errorCode;
        private String errorMessage;

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
