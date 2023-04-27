package com.rank.kuber.Common;

import android.content.Context;

import com.rank.kuber.socket.SocketClass;
import com.rank.kuber.socket.SocketParser;
import com.socket.SocketLibrary;
import com.vidyo.VidyoClient.Connector.Connector;

import io.socket.client.Socket;

public class AppData {

    public static String TAG = "KUBER_TAG";

//  category and language will have default values

    public static boolean isSpeakerOnMute = false;
    public static boolean isMicOnMute = false;
    public static boolean isVideoOnMute = false;
    public static boolean isHold = false;

    public static Context currentContext;
    public static String category="Urgent";
    public static String language="English";
    public static String location= "";

    public static String name="" ;
    public static String email="" ;
    public static String phone="" ;
    public static String nationality="" ;
    public static String selectedService="" ;
    public static String CallType = "";
    public static String USER_TYPE ="";
    public static String PROMOTIONAL_VIDEO = "";
    public static String CustID = "";
    public static String CustName = "";
    public static String CustFName = "";
    public static String CustLName = "";
    public static String Call_ID = "";

    public static String ServiceName = "";
    public static String Status ="";
    public static String Agent_id = "";
    public static String Entity_id= "";
    public static String Agent_login_id = "";
    public static String Agent_Name = "";
    public static String RoomName = "";
    public static String RoomKey = "";
    public static String Portal_Address = "";
    // public static String Portal_Address = "";
    public static String DISPLAY_NAME = "";

    // Socket handling
    public static String SocketHostUrl = "https://vconnect.ranktechsolutions.com:3004";
//    public static String SOCKET_URL = "https://vconnect.ranktechsolutions.com";//TODO Correct Url 8-11-2022
//    public static String SOCKET_PORT = "3004";
    public static String SOCKET_URL = "https://vconnect.ranktechsolutions.com";//TODO Correct Url 8-11-2022
    public static String SOCKET_PORT = "3004";
    public static String socketMSG = "";
    public static SocketParser socketParser;
    public static SocketLibrary socketLibrary;
    public static SocketClass socketClass;
    public static Socket socket;

    /* Socket MSG Initialization */
    public static final String SOCKET_MSG_INCOMING_CALL_RECEIVED = "incoming_call#received";
    public static final String SOCKET_MSG_CALL_MISSED_BY_AGENT = "missed#";
//    public static String SOCKET_MSG_HOLD = "hold";
//    public static String SOCKET_MSG_UNHOLD = "unhold";
    public static final String SOCKET_MSG_HOLD = "hold#";
    public static final String SOCKET_MSG_UNHOLD = "unhold#";

    public static final String SOCKET_MSG_SCHEDULE_CALL_FROM_EMPLOYEE = "scheduleCallFromEmployee";
    public static final String SOCKET_MSG_PATIENT_JOIN_SCHEDULE_CALL = "callJoinedByPatient";
    public static final String SOCKET_MSG_CALL_ENDED_BY_EMPLOYEE = "callEndedByEmployee";
    public static final String SOCKET_MSG_RECORD_STARTED = "recordConfirm~recordOn";
    public static final String SOCKET_MSG_MULTIWAY_EMP_LEAVE_CONFERECE = "leaveConferenceByMultiwayEmployee";

    public static final String SOCKET_MSG_FILE_RECEIVED_DURING_CALL = "fileSent";
    public static final String SOCKET_MSG_PRESCRIPTION_RECEIVED_DURING_CALL = "prescriptionSent";



    /* Intent Filters For Broadcast Receiver */

    public static String _intentFilter_INCOMING_CALL_RECEIVED = "incoming_call#received";
    public static String _intentFilter_CALL_MISSED_BY_AGENT= "missed";
    public static String _intentFilter_CHATMSG_RECEIVED = "chatMsgReceived";
    public static String _intentFilter_INDIVIDUAL_CHATMSG = "individualChatMsgReceived";
    public static String _intentFilter_HOLD = "hold#";
    public static String _intentFilter_UNHOLD = "unhold#";

    public static String _intentFilter_MISSED_CALL = "callMissedByDoctor";
    public static String _intentFilter_INCOMINGCALL = "scheduleCallFromEmployee";


    public static String _intentFilter_FORCE_LOGOUT = "forcelogout";
    public static String _intentFilter_FILERECEIVED = "fileReceivedDuringCall";
    public static String _intentFilter_MULTIWAY = "multiwayCallFromEmployee";
    public static String _intentFilter_ENDCALL = "callEndedByEmployee";

    public static String Longitude= "";
    public static String Latitude= "";

    public static Connector mVidyoconnector=null;

 //   public static String BASE_URL= "https://collab.ranktechsolutions.com/videobanking/";
 //    public static String BASE_URL= "https://vconnect.ranktechsolutions.com/videobanking/";
    public static String BASE_URL= "https://vconnect.ranktechsolutions.com/firstbank/"; // URL Changed on 3/30/23 by Arif Khan.


    public static final String REQUEST_TYPE_SERVICE_LIST = "rest/getservicelist";
    public static final String REQUEST_TYPE_REGISTER_CUSTOMER = "rest/registercustomer";
    public static final String REQUEST_TYPE_AVAILABLE_AGENT = "rest/calltoavailabeagent";
    public static final String REQUEST_TYPE_HANGUP_CUSTOMER = "rest/hangupcustomer";
    public static final String REQUEST_TYPE_GET_FEEDBACK = "rest/getfeedback";
    public static final String REQUEST_TYPE_SAVE_FEEDBACK = "rest/savecustomerfeedback";
    public static final String REQUEST_TYPE_GET_SERVICEDOWNTIME = "rest/getservicedowntime";
    public static final String REQUEST_TYPE_PICKED_CALL_BY_CUSTOMER = "rest/pickedcallbycustomer";



}
