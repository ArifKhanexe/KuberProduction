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
                        } else {
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
//
//        /**
//         * @task Incoming Call
//         */
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


//        /**
//         * @task Hold/UnHold Call
//         */
//        if (message.contains(AppData.SOCKET_MSG_HOLD)) {
//            if (message.contains(AppData.SOCKET_MSG_UNHOLD)) {
//                Intent i = new Intent(AppData._intentFilter_UNHOLD);
//                AppData.currentContext.sendBroadcast(i);
//            } else if (message.contains(AppData.SOCKET_MSG_HOLD)) {
//                Intent i = new Intent(AppData._intentFilter_HOLD);
//                AppData.currentContext.sendBroadcast(i);
//            }
//        }
//
//        /**
//         * @task Hold/UnHold Call
//         */
//        if (message.contains(AppData.SOCKET_MSG_UNHOLD)) {
//            if (message.contains(AppData.SOCKET_MSG_UNHOLD)) {
//                Intent i = new Intent(AppData._intentFilter_UNHOLD);
//                AppData.currentContext.sendBroadcast(i);
//            } else if (message.contains(AppData.SOCKET_MSG_HOLD)) {
//                Intent i = new Intent(AppData._intentFilter_HOLD);
//                AppData.currentContext.sendBroadcast(i);
//            }
//        }

//        /**
//         * @task HangUp By Employee
//         */
//        if (message.contains(AppData.SOCKET_MSG_CALL_ENDED_BY_EMPLOYEE)) {
//
//            try {
//                Intent hangUpIntent = new Intent(AppData._intentFilter_ENDCALL);
//                AppData.currentContext.sendBroadcast(hangUpIntent);
//            } catch (Exception e) {
//                Log.e("HangUpCallSocketEx", "ExceptionCause: " + e.getMessage());
//            }
//        }


    }
}
