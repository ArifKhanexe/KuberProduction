package com.rank.kuber.socket;

import android.content.Intent;
import android.util.Log;

import com.rank.kuber.Common.AppData;
import com.socket.SocketListener;

import org.json.JSONObject;

public class SocketParser {

    String message;
    String tempPushString;
    String eventForChat;

    public void createSocket() {
        try {

            AppData.socketClass.createSocket(AppData.CustID, new SocketListener() {
                @Override
                public void onReceive(String event, JSONObject jsonObject) {
                    try {
                        AppData.socketMSG = jsonObject.getString("msg");

                        eventForChat = event;
                        Log.e("onReceive", "SocketParseHandler: " + AppData.socketMSG);
                        Log.e("OnReceive", "Event: " + event);

                        if (eventForChat.equalsIgnoreCase("private-chat") || eventForChat.equalsIgnoreCase("group-chat")) {
                            Intent intentChat = new Intent(AppData._intentFilter_CHATMSG_RECEIVED);
                            intentChat.putExtra("senderId", jsonObject.getString("nick"));
                            intentChat.putExtra("chatMsg", jsonObject.getString("msg"));
                            intentChat.putExtra("event", eventForChat);
                            AppData.currentContext.sendBroadcast(intentChat);
                        }

                    else {
                            parseSocket();
                        }
                    } catch (Exception e) {
                        Log.e("OnReceiveCreateSocket", "ExceptionCause: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e("CreateSocketException", "ExceptionCause: " + e.getMessage());
        }
    }

    public void parseSocket() {
        message = AppData.socketMSG;



        /**
         * @task Incoming Call
         */
//        if (message.contains(AppData.SOCKET_MSG_SCHEDULE_CALL_FROM_EMPLOYEE)) {
//            tempPushString = message;
//
//            try {
//                String[] splitString = tempPushString.split("#");
//
////                AppData.INCOMING_CALL_TOKEN = splitString[1];
////                AppData.RESOURCE_ID = splitString[2];
////                AppData.CALL_MST_ID = splitString[3];
////                AppData.employeeID = splitString[4];
//
//                AppData.INCOMING_SCHEDULE_CALL_ROOMLINK = splitString[1];
////                AppData.SCHEDULE_CALL_ROOMNAME = splitString[2];
//                AppData.CALL_MST_ID = splitString[2];
//                AppData.employeeID = splitString[3];
//
//                Log.e("incomingCallToken", "Token: " + AppData.INCOMING_SCHEDULE_CALL_ROOMLINK + "\nCallMstId: " + AppData.CALL_MST_ID);
//
//                if (AppData.INCOMING_SCHEDULE_CALL_ROOMLINK.length() > 0) {
//                    Intent incomingIntent = new Intent(AppData._intentFilter_INCOMINGCALL);
//                    AppData.currentContext.sendBroadcast(incomingIntent);
//                }
//            } catch (Exception e) {
//                Log.e("IncomingCallSocketEx", "ExceptionCause: " + e.getMessage());
//            }
//        }

        /**
         * @task Patient Join In Schedule Call
         */
        if (message.contains(AppData.SOCKET_MSG_PATIENT_JOIN_SCHEDULE_CALL)) {

        }

        /**
         * @task Patient Dial Call Received By Employee
         */
//        if (message.contains(AppData.SOCKET_MSG_DIAL_CALL_RECEIVED_BY_EMPLOYEE)) {
//            tempPushString = message;
//
//            try {
//                String[] splitString = tempPushString.split("#");
//
//                AppData.DIAL_CALL_TOKEN = splitString[1];
//                AppData.RESOURCE_ID = splitString[2];
//                AppData.CALL_MST_ID = splitString[3];
//                AppData.employeeID = splitString[4];
//
//                Log.e("dialCallToken", "Token: " + AppData.DIAL_CALL_TOKEN + "\nCallMstId: " + AppData.CALL_MST_ID);
//
//                if (AppData.DIAL_CALL_TOKEN.length() > 0) {
//                    Intent dialCallIntent = new Intent(AppData._intentFilter_DIALCALL);
//                    AppData.currentContext.sendBroadcast(dialCallIntent);
//                }
//            } catch (Exception e) {
//                Log.e("DialCallSocketEx", "ExceptionCause: " + e.getMessage());
//            }
//        }

        /**
         * @task Record Started Successfully
         */
        if (message.contains(AppData.SOCKET_MSG_RECORD_STARTED)) {

        }

        /**
         * @task Hold/UnHold Call
         */
        if (message.contains(AppData.SOCKET_MSG_HOLD)) {
                Intent i = new Intent(AppData._intentFilter_HOLD);
                AppData.currentContext.sendBroadcast(i);
        }

        /**
         * @task Hold/UnHold Call
         */
            if (message.contains(AppData.SOCKET_MSG_UNHOLD)) {
                Intent i = new Intent(AppData._intentFilter_UNHOLD);
                AppData.currentContext.sendBroadcast(i);
        }

        /**
         * @task HangUp By Employee
         */
        if (message.contains(AppData.SOCKET_MSG_CALL_ENDED_BY_EMPLOYEE)) {

            try {
                Intent hangUpIntent = new Intent(AppData._intentFilter_ENDCALL);
                AppData.currentContext.sendBroadcast(hangUpIntent);
            } catch (Exception e) {
                Log.e("HangUpCallSocketEx", "ExceptionCause: " + e.getMessage());
            }
        }


        /**
         * @task Call received By Agent
         */
        if (message.contains(AppData.SOCKET_MSG_INCOMING_CALL_RECEIVED)) {

            try {
                Intent i = new Intent(AppData._intentFilter_INCOMING_CALL_RECEIVED);
                AppData.currentContext.sendBroadcast(i);
            } catch (Exception e) {
                Log.e("IncomingCallSocket", "ExceptionCause: " + e.getMessage());
            }
        }

        /**
         * @task Call missed By Agent
         */
        if (message.contains(AppData.SOCKET_MSG_CALL_MISSED_BY_AGENT)) {

            try {
                Intent i = new Intent(AppData._intentFilter_CALL_MISSED_BY_AGENT);
                AppData.currentContext.sendBroadcast(i);
            } catch (Exception e) {
                Log.e("IncomingCallSocket", "ExceptionCause: " + e.getMessage());
            }
        }




        /**
         * @task File Receive During Call
         */
//        if (message.contains(AppData.SOCKET_MSG_FILE_RECEIVED_DURING_CALL)) {
//            tempPushString = message;
//
//            try {
//                String[] splitString = tempPushString.split("#");
//                String filePath = splitString[1].trim();
//                String fileName = splitString[2].trim();
////                AppData.FILE_TYPE = splitString[3].trim();
//                AppData.FILE_TYPE = "";
//
//                Log.e("FileReceivedPath", "filePath: " + filePath);
//                if (filePath.trim().length() > 0) {
//                    AppData.FILE_RECEIVE_URL = filePath.trim();
//
//                    Intent fileReceiveIntent = new Intent(AppData._intentFilter_FILERECEIVED);
//                    AppData.currentContext.sendBroadcast(fileReceiveIntent);
//                }
//            } catch (Exception e) {
//                Log.e("FileReceiveSocketEx", "ExceptionCause: " + e.getMessage());
//            }
//        }

        /**
         * @task File Receive During Call
         */
//        if (message.contains(AppData.SOCKET_MSG_PRESCRIPTION_RECEIVED_DURING_CALL)) {
//            tempPushString = message;
//
//            try {
//                String[] splitString = tempPushString.split("#");
//                String filePath = splitString[1].trim();
//                String fileName = splitString[2].trim();
//                AppData.FILE_TYPE = "prescription";
//
//                Log.e("FileReceivedPath", "filePath: " + filePath);
//                if (filePath.trim().length() > 0) {
//                    AppData.FILE_RECEIVE_URL = filePath.trim();
//
//                    Intent fileReceiveIntent = new Intent(AppData._intentFilter_FILERECEIVED);
//                    AppData.currentContext.sendBroadcast(fileReceiveIntent);
//                }
//
//            } catch (Exception e) {
//                Log.e("FileReceiveSocketEx", "ExceptionCause: " + e.getMessage());
//            }
//        }




        /**
         * @task Patient Force logout:- Login with same credentials on another device.
         */
        if (message.contains("forceLogout")){
            Intent i = new Intent(AppData._intentFilter_FORCE_LOGOUT);
            AppData.currentContext.sendBroadcast(i);
        }
    }

}
