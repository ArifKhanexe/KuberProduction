package com.rank.kuber.Model;

public class GetFeedbackResponse {


    /**
     * questionOne : test?
     * questionTwo : How would you rate the the satisfaction with the resolution provided by our representative?
     * questionThree : Are all your queries get resolved?
     * resStatus : true
     * resMessage : SUCCESS
     */

    private PayloadBean payload;
    /**
     * payload : {"questionOne":"test?","questionTwo":"How would you rate the the satisfaction with the resolution provided by our representative?","questionThree":"Are all your queries get resolved?","resStatus":true,"resMessage":"SUCCESS"}
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
        private String questionOne;
        private String questionTwo;
        private String questionThree;
        private boolean resStatus;
        private String resMessage;

        public String getQuestionOne() {
            return questionOne;
        }

        public void setQuestionOne(String questionOne) {
            this.questionOne = questionOne;
        }

        public String getQuestionTwo() {
            return questionTwo;
        }

        public void setQuestionTwo(String questionTwo) {
            this.questionTwo = questionTwo;
        }

        public String getQuestionThree() {
            return questionThree;
        }

        public void setQuestionThree(String questionThree) {
            this.questionThree = questionThree;
        }

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
