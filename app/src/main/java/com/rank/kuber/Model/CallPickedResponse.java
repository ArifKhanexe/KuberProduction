package com.rank.kuber.Model;

public class CallPickedResponse {


    /**
     * succMsg : SUCCESS
     */

    private PayloadBean payload;
    /**
     * errorCode : 412
     * errorMessage : Please Provide valid information!
     */

    private ErrorBean error;
    /**
     * payload : {"succMsg":"SUCCESS"}
     * error : {"errorCode":412,"errorMessage":"Please Provide valid information!"}
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
        private String succMsg;

        public String getSuccMsg() {
            return succMsg;
        }

        public void setSuccMsg(String succMsg) {
            this.succMsg = succMsg;
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
