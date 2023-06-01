package com.rank.kuber.Model;

public class UploadResponse {


    /**
     * success : SUCCESS
     * filepath : https://vconnect.ranktechsolutions.com/firstbank/resources/File_Upload/Call/505/511audio_icon.png
     * docTitle : test
     */

    private PayloadBean payload;
    /**
     * errorCode : 412
     * errorMessage : Please Provide valid information22222!
     */

    private ErrorBean error;
    /**
     * payload : {"success":"SUCCESS","filepath":"https://vconnect.ranktechsolutions.com/firstbank/resources/File_Upload/Call/505/511audio_icon.png","docTitle":"test"}
     * error : {"errorCode":412,"errorMessage":"Please Provide valid information22222!"}
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
        private String success;
        private String filepath;
        private String docTitle;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public String getDocTitle() {
            return docTitle;
        }

        public void setDocTitle(String docTitle) {
            this.docTitle = docTitle;
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
