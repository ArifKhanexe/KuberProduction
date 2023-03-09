package com.rank.kuber.Common;

import android.content.Context;

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
    public static String SocketHostUrl = "";
    public static String ServiceName = "";
    public static String Agent_id = "";
    public static String Entity_id= "";
    public static String Agent_login_id = "";
    public static String Agent_Name = "";
    public static String RoomName = "";
    public static String RoomKey = "";
    public static String Portal_Address = "";
    // public static String Portal_Address = "";
    public static String DISPLAY_NAME = "";

    public static String Longitude= "";
    public static String Latitude= "";


    public static String BASE_URL= "https://collab.ranktechsolutions.com/videobanking/";


    public static final String REQUEST_TYPE_SERVICE_LIST = "rest/getservicelist";
    public static final String REQUEST_TYPE_REGISTER_CUSTOMER = "rest/registercustomer";
    public static final String REQUEST_TYPE_AVAILABLE_AGENT = "rest/calltoavailabeagent";


}
