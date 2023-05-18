package com.rank.kuber.Model;

import java.util.List;

public class GetEmployeesResponse {


    /**
     * errorCode : 412
     * errorMessage : Please Provide valid information!
     */

    private ErrorBean error;
    /**
     * payload : [{"id":3,"name":"Agent One","loginId":"cc1","participantType":"Employee"}]
     * error : {"errorCode":412,"errorMessage":"Please Provide valid information!"}
     * status : true
     */

    private boolean status;
    /**
     * id : 3
     * name : Agent One
     * loginId : cc1
     * participantType : Employee
     */

    private List<PayloadBean> payload;

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

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
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

    public static class PayloadBean {
        private int id;
        private String name;
        private String loginId;
        private String participantType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getParticipantType() {
            return participantType;
        }

        public void setParticipantType(String participantType) {
            this.participantType = participantType;
        }
    }
}
