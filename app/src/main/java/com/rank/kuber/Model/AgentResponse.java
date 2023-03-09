package com.rank.kuber.Model;

public class AgentResponse {
    /**
     * url : https://RanktechSolutions.vidyocloud.com/join/1XCmL3Szno
     * callId : 1879
     * status : Initiated||apurba maity
     * agentId : 5
     * socketHostPublic : Cust_NN9rIN95Dl2uXfR
     * customerId : Cust_Y8VtwSGV5CDvuVJ
     * custFname : Neha Bhardwaj
     * custLname :
     * roomName : cc22889013
     * entityId : 881589
     */

    private PayloadDTO payload;
    /**
     * payload : {"url":"https://RanktechSolutions.vidyocloud.com/join/1XCmL3Szno","callId":"1879","status":"Initiated||apurba maity","agentId":"5","socketHostPublic":"Cust_NN9rIN95Dl2uXfR","customerId":"Cust_Y8VtwSGV5CDvuVJ","custFname":"Neha Bhardwaj","custLname":"","roomName":"cc22889013","entityId":"881589"}
     * error : null
     * status : true
     */

    private Object error;
    private boolean status;

    public PayloadDTO getPayload() {
        return payload;
    }

    public void setPayload(PayloadDTO payload) {
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

    public static class PayloadDTO {
        private String url;
        private String callId;
        private String status;
        private String agentId;
        private String socketHostPublic;
        private String customerId;
        private String custFname;
        private String custLname;
        private String roomName;
        private String entityId;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCallId() {
            return callId;
        }

        public void setCallId(String callId) {
            this.callId = callId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

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

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }
    }


    /**
     * payload : {"callId":"1700","status":"Not Initiated",
     * "socketHostPublic":"https://webrtc.ranktechsolutions.com:3006",
     * "custFname":"Sudip Sarkar","custLname":""}
     * error : null
     * status : true
     */


}
