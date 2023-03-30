package com.rank.kuber.Model;

import java.util.List;

public class ServiceModel {


    /**
     * payload : [{"id":3,"serviceCd":"CASA","serviceName":"New Account","serviceDesc":"CASA New Account"},{"id":2,"serviceCd":"CC","serviceName":"Credit Card","serviceDesc":"Credit Card"},{"id":1,"serviceCd":"LNM","serviceName":"Loan","serviceDesc":"Loan"}]
     * error : null
     * status : true
     */

    private Object error;
    private boolean status;
    /**
     * id : 3
     * serviceCd : CASA
     * serviceName : New Account
     * serviceDesc : CASA New Account
     */

    private List<PayloadBean> payload;

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

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
    }

    public static class PayloadBean {
        private int id;
        private String serviceCd;
        private String serviceName;
        private String serviceDesc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getServiceCd() {
            return serviceCd;
        }

        public void setServiceCd(String serviceCd) {
            this.serviceCd = serviceCd;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceDesc() {
            return serviceDesc;
        }

        public void setServiceDesc(String serviceDesc) {
            this.serviceDesc = serviceDesc;
        }
    }
}
