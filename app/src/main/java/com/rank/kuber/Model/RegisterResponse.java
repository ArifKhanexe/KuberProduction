package com.rank.kuber.Model;

public class RegisterResponse {


    /**
     * socketHostPublic : https://collab.ranktechsolutions.com:3001
     * customerId : Cust_hza7zaz32Y2ORz8
     * custFname : apurva
     * custLname :
     * custMstId : 1494
     * promotionalVideo : https://collab.ranktechsolutions.com/videobanking/PromotionalVideo/48c012e7a2356a43bf26a8ab01eea12a6kYwj1iBdIn+this+financial+year-+take+control+of+your+finances.+SheBanksOnHerself.mp4
     * mobileNo : 9051926974
     * customerType : new_customer
     */

    private PayloadBean payload;
    /**
     * payload : {"socketHostPublic":"https://collab.ranktechsolutions.com:3001","customerId":"Cust_hza7zaz32Y2ORz8","custFname":"apurva","custLname":"","custMstId":1494,"promotionalVideo":"https://collab.ranktechsolutions.com/videobanking/PromotionalVideo/48c012e7a2356a43bf26a8ab01eea12a6kYwj1iBdIn+this+financial+year-+take+control+of+your+finances.+SheBanksOnHerself.mp4","mobileNo":"9051926974","customerType":"new_customer"}
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
        private String socketHostPublic;
        private String customerId;
        private String custFname;
        private String custLname;
        private int custMstId;
        private String promotionalVideo;
        private String mobileNo;
        private String customerType;

        public String getSocketHostPublic() {
            return socketHostPublic;
        }

        public void setSocketHostPublic(String socketHostPublic) {
            this.socketHostPublic = socketHostPublic;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustFname() {
            return custFname;
        }

        public void setCustFname(String custFname) {
            this.custFname = custFname;
        }

        public String getCustLname() {
            return custLname;
        }

        public void setCustLname(String custLname) {
            this.custLname = custLname;
        }

        public int getCustMstId() {
            return custMstId;
        }

        public void setCustMstId(int custMstId) {
            this.custMstId = custMstId;
        }

        public String getPromotionalVideo() {
            return promotionalVideo;
        }

        public void setPromotionalVideo(String promotionalVideo) {
            this.promotionalVideo = promotionalVideo;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getCustomerType() {
            return customerType;
        }

        public void setCustomerType(String customerType) {
            this.customerType = customerType;
        }
    }
}
