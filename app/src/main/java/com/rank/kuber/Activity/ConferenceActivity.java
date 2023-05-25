package com.rank.kuber.Activity;

import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.al_chat_everyone;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.al_chat_specific_user;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersId;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersName;
import static com.rank.kuber.R.anim.pull_in;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;
import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.ChatModel;
import com.rank.kuber.Model.EmployeeList;
import com.rank.kuber.Model.GetEmployeesRequest;
import com.rank.kuber.Model.GetEmployeesResponse;
import com.rank.kuber.Model.HangUpCustomerRequest;
import com.rank.kuber.Model.HangUpCustomerResponse;
import com.rank.kuber.R;
import com.rank.kuber.Utils.FileUtils;
import com.rank.kuber.Utils.NetworkBroadcast;
import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Connector.ConnectorPkg;
import com.vidyo.VidyoClient.Device.Device;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Device.LocalMicrophone;
import com.vidyo.VidyoClient.Device.LocalSpeaker;
import com.vidyo.VidyoClient.Device.LocalWindowShare;
import com.vidyo.VidyoClient.Endpoint.LogRecord;
import com.vidyo.VidyoClient.Endpoint.Participant;
import com.vidyo.VidyoClient.NetworkInterface;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConferenceActivity extends AppCompatActivity implements Connector.IConnect,
        Connector.IRegisterParticipantEventListener,
        Connector.IRegisterLogEventListener,
        Connector.IRegisterNetworkInterfaceEventListener, View.OnLayoutChangeListener,
        Connector.IRegisterLocalCameraEventListener, Connector.IRegisterLocalMicrophoneEventListener, Connector.IRegisterLocalSpeakerEventListener, Connector.IRegisterLocalWindowShareEventListener {

    private static final int FILE_UPLOAD_REQUEST_CODE =4556 ;
    String TAG = "ConferenceActivity";
    public PopupWindow mypopupWindow;
    private LinearLayout llFunctionality;
    private ImageView tvEndCall,tvhold, tvagentimage;
    private ImageView ivMicOn, ivMicOff, ivCamOn, ivCamOff, ivCamRotateBack, ivCamRotateFront, iv_menu, ivSpeakerOn, ivSpeakerOff, ivMenuOption;
    private TextView jointext,agenttext;
    ViewGroup frame;
    private FrameLayout fl_videoFrame;
    private HangUpCustomerRequest hangUpCustomerRequest;
    BroadcastReceiver networkBroadcastReceiver;
    private HoldCallReceiver holdCallReceiver;
    private UnHoldCallReceiver unHoldCallReceiver;
    private HangUpReceiver hangUpReceiver;
    private DownloadFileReceiver downloadFileReceiver;
    private boolean doRender = false;
    private boolean callStarted = false;
    private ProgressBar joinProgress;
    public static boolean isSpeakerOnMute, isVideoOnMute, isMicOnMute;
    private long joinCountDown = 0L;
    private boolean showStatistic = false;
    RelativeLayout relativeLayout;
    String randomString1, randomString2;
    String generatedRandomString;
    private Handler handler;
    String imageFilePath="";
    private static boolean isOnHold;
    public List<EmployeeList> employeeLists;
//    String Host = "https://ranktechsolutions.platform.vidyo.io";
    String Host = "https://ranktechsolutions.platform.vidyo.io";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //code that displays the content in full screen mode
        setContentView(R.layout.activity_conference);
        AppData.currentContext = ConferenceActivity.this;

        initObjects();

        /*Keep Screen Light On*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);




 //       joinForm = findViewById(R.id.join_params_frame);
/*        promoVideoLayout = findViewById(R.id.promoVideolayout);
        txtAgentInfo = promoVideoLayout.findViewById(R.id.txtAgentInfo);
        promotional_videovw = promoVideoLayout.findViewById(R.id.videoPlayer);
        holdUnholdReceiver = new HoldUnholdReceiver();
        endCallReceiver = new EndCallReceiver();*/

        joinProgress = findViewById(R.id.join_progress);
        jointext=findViewById(R.id.join_text);
        agenttext=findViewById(R.id.agenttext);
        agenttext.setText(AppData.Agent_Name);
        frame.setBackgroundColor(getResources().getColor(R.color.black));
        joinProgress.setVisibility(View.VISIBLE);
        jointext.setVisibility(View.VISIBLE);

        /*AppData.mVidyoconnector.registerLocalCameraEventListener(this);
        AppData.mVidyoconnector.registerLocalMicrophoneEventListener(this);
        AppData.mVidyoconnector.registerLocalSpeakerEventListener(this);
        AppData.mVidyoconnector.registerParticipantEventListener(this);*/
//        frame.addOnLayoutChangeListener(this);

        buttonClickEvent();
        registerNetworkBroadcastReceiver();
        registerReceivers();

    }


    private void registerReceivers() {
        registerReceiver(holdCallReceiver,new IntentFilter(AppData._intentFilter_HOLD));
        registerReceiver(unHoldCallReceiver,new IntentFilter(AppData._intentFilter_UNHOLD));
        registerReceiver(hangUpReceiver, new IntentFilter(AppData._intentFilter_ENDCALL));
        registerReceiver(downloadFileReceiver, new IntentFilter(AppData._intentFilter_FILERECEIVED));
    }

    private void registerNetworkBroadcastReceiver() {
        networkBroadcastReceiver= new NetworkBroadcast();
        registerReceiver(networkBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void initObjects() {
        Log.e(TAG, "Initobject");
        handler = new Handler();
        relativeLayout=findViewById(R.id.RelativeLayoutContainerConference);
        frame = findViewById(R.id.main_content);
        iv_menu = findViewById(R.id.imgvw_menu_plus);
        ivMenuOption=findViewById(R.id.iv_menu);
        fl_videoFrame = findViewById(R.id.join_params_frame);
        llFunctionality = findViewById(R.id.llFunctionality);
        tvhold=findViewById(R.id.holdimage);
        tvEndCall = findViewById(R.id.endCall);
        ivMicOn = findViewById(R.id.ivMicOn);
        ivMicOff = findViewById(R.id.ivMicOff);
        ivCamOn = findViewById(R.id.ivCamOn);
        ivCamOff = findViewById(R.id.ivCamOff);
//        ivCamRotateBack = findViewById(R.id.ivCamRotateBack);
//        ivCamRotateFront = findViewById(R.id.ivCamRotateFront);
        ivSpeakerOn=findViewById(R.id.ivSpeakeron);
        ivSpeakerOff=findViewById(R.id.ivSpeakeroff);
        tvagentimage=findViewById(R.id.agentimage);


        holdCallReceiver= new ConferenceActivity.HoldCallReceiver();
        unHoldCallReceiver=new ConferenceActivity.UnHoldCallReceiver();
        hangUpReceiver=new ConferenceActivity.HangUpReceiver();
        downloadFileReceiver = new DownloadFileReceiver();

        initVideoconnectorObj();
    }

    private void buttonClickEvent() {
        

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llFunctionality.isShown()) {
                    llFunctionality.setVisibility(View.GONE);
                } else {
                    llFunctionality.setVisibility(View.VISIBLE);
//                    iv_menu.setVisibility(View.GONE);
                }
            }
        });


        tvEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEnded();
            }
        });

        ivMicOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    jniBridge.MuteMicrophone(true);
                AppData.mVidyoconnector.setMicrophonePrivacy(true);
                isMicOnMute = true;
                ivMicOn.setVisibility(View.GONE);
                ivMicOff.setVisibility(View.VISIBLE);

            }
        });

        ivMicOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    jniBridge.MuteMicrophone(false);
                AppData.mVidyoconnector.setMicrophonePrivacy(false);
                isMicOnMute = false;
                ivMicOn.setVisibility(View.VISIBLE);
                ivMicOff.setVisibility(View.GONE);

            }
        });

        ivCamOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    jniBridge.MuteCamera(true);
                AppData.mVidyoconnector.setCameraPrivacy(true);
//                        jniBridge.SetPreviewMode(0);
                isVideoOnMute = true;
                ivCamOn.setVisibility(View.GONE);
                ivCamOff.setVisibility(View.VISIBLE);

            }
        });

        ivCamOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    jniBridge.MuteCamera(false);
                AppData.mVidyoconnector.setCameraPrivacy(false);
//                        jniBridge.SetPreviewMode(2);
                isVideoOnMute = false;
                ivCamOff.setVisibility(View.GONE);
                ivCamOn.setVisibility(View.VISIBLE);

            }
        });

//        ivCamRotateBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    jniBridge.SetCameraDevice(AppUtils.BACK_CAMERA);
//                AppData.mVidyoconnector.cycleCamera();
//                if (isVideoOnMute) {
//                    AppData.mVidyoconnector.setCameraPrivacy(true);
//                } else {
//                    AppData.mVidyoconnector.setCameraPrivacy(false);
//                }
//                ivCamRotateBack.setVisibility(View.GONE);
//                ivCamRotateFront.setVisibility(View.VISIBLE);
//            }
//        });
//
//        ivCamRotateFront.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppData.mVidyoconnector.cycleCamera();
//                if (isVideoOnMute) {
//                    AppData.mVidyoconnector.setCameraPrivacy(true);
//                } else {
//                    AppData.mVidyoconnector.setCameraPrivacy(false);
//                }
//                ivCamRotateBack.setVisibility(View.VISIBLE);
//                ivCamRotateFront.setVisibility(View.GONE);
//
//            }
//        });

        ivSpeakerOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.mVidyoconnector.setSpeakerPrivacy(true);
                isSpeakerOnMute=true;
                ivSpeakerOn.setVisibility(View.GONE);
                ivSpeakerOff.setVisibility(View.VISIBLE);
            }
        });

        ivSpeakerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.mVidyoconnector.setSpeakerPrivacy(false);
                isSpeakerOnMute=false;
                ivSpeakerOn.setVisibility(View.VISIBLE);
                ivSpeakerOff.setVisibility(View.GONE);
            }
        });

        ivMenuOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
//              mypopupWindow.setAnimationStyle(R.style.animation);
                mypopupWindow.showAtLocation(view,Gravity.BOTTOM|Gravity.RIGHT,0,170);

            }
        });
    }

    /**
     * Initialize Vidyo Connector Object
     */
    private void initVideoconnectorObj() {
        Log.e(TAG, "Initvideoconnectorobj");
         try {
             ConnectorPkg.setApplicationUIContext(getApplicationContext());
             ConnectorPkg.initialize();
             String logLevel = "info@VidyoClient info@VidyoConnector warning";

             AppData.mVidyoconnector = new Connector(fl_videoFrame, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default, 15, logLevel, "", 0);
             Log.e(TAG, "Library Version" + AppData.mVidyoconnector.getVersion());

         } catch (Exception e){
             e.printStackTrace();
             Log.e(TAG,"Error : " + e.toString());
         }

        Log.d("VidyoIOLibrary","Version"+AppData.mVidyoconnector.getVersion());

        AppData.mVidyoconnector.registerLocalCameraEventListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /*AppData.Connector = new Connector(fl_videoFrame, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default, 16, "", "", 0);*/
                AppData.mVidyoconnector.showViewAt(fl_videoFrame, 0, 0, fl_videoFrame.getWidth(), fl_videoFrame.getHeight());

            }
        }, 2000);
        joinConference();
        unmuteAudioVideo();
    }

    private void joinConference() {
        Log.e(TAG, AppData.RoomKey);
        Log.e(TAG, AppData.CustName);
//        final String portalParam = portal.getText().toString();
        Log.e(TAG, "Token: " + AppData.RoomKey);
        Log.e(TAG, "Portal: " + AppData.Portal_Address);
        String display_name = AppData.CustFName + "~" + AppData.CustID ;
        AppData.mVidyoconnector.connectToRoomAsGuest(Host, display_name, AppData.RoomKey, "",this);

               /* if (AppData.DIAL_CALL_TOKEN != null && AppData.DIAL_CALL_TOKEN.length() > 0) {
                    Log.e("joinConference", "DialCallToken: " + AppData.DIAL_CALL_TOKEN);
                    AppData.Connector.connectToRoomAsGuest(
                            portalParam, AppData.custName, AppData.DIAL_CALL_TOKEN,
                            "", GuestJoinActivity.this);
                }*/

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeOrientation();
//        updateView();
    }

    private void changeOrientation() {
        ViewTreeObserver viewTreeObserver = fl_videoFrame.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    fl_videoFrame.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Width or height values of views not updated at this point so need to wait
                    // before refreshing UI

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AppData.mVidyoconnector.showViewAt(fl_videoFrame, 0, 0, fl_videoFrame.getWidth(), fl_videoFrame.getHeight());
                        }
                    }, 2000);
                }
            });
        }
    }



    private void callEnded() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        callHangupApiCall();

        callStarted = false;
      //  AppData.Call_ID="";
        AppData.Agent_id = "";
        AppData.RoomKey = "";
        AppData.Portal_Address = "";
        AppData.CallType="";

        /*joinButton.setEnabled(true);
        cancelButton.setEnabled(false);*/

        joinProgress.setVisibility(View.GONE);

//        lmiDeviceManagerView.setVisibility(View.GONE);

//        controlForm.setVisibility(View.GONE);
        llFunctionality.setVisibility(View.GONE);

        stopDevices();


//     Setting username, id and all displayed messages to null on exit. It is important to do so otherwise
        listOfUsersId=null;
        listOfUsersName=null;
        al_chat_everyone=null;
        al_chat_specific_user=null;

        // Checks if Activity is not null then Destroy the ShowGuestPromotionalVideoActivity remotely from ChatConferenceActivity. This will unregister(chatMsgReceiver);
       if( ShowGuestPromotionalVideoActivity.SGPA != null) {
           ShowGuestPromotionalVideoActivity.SGPA.finish();
       }
        AppData.Agent_login_id="";
        AppData.Agent_id="";
        AppData.CallType="";
        AppData.socketClass.removeSocket();

        // Checks if Activity is not null then Destroy the EveryoneChatActivity remotely from ChatConferenceActivity.
        if(EveryoneChatActivity.ECA !=null ) {
            EveryoneChatActivity.ECA.finish();
        }

        if (AppData.mVidyoconnector != null) {
//            jniBridge.RenderRelease();

//            jniBridge.LeaveConference();
//            callCancel();

            startActivity(new Intent(ConferenceActivity.this, FeedbackActivity.class));
            finish();
        }
    }

    private void callHangupApiCall() {
        hangUpCustomerRequest = new HangUpCustomerRequest();
        hangUpCustomerRequest.setCallId(AppData.Call_ID);
        hangUpCustomerRequest.setCustId(AppData.CustID);

        ApiClient.getApiClient().gethangupcustomer(hangUpCustomerRequest).enqueue(new Callback<HangUpCustomerResponse>() {
            @Override
            public void onResponse(Call<HangUpCustomerResponse> call, Response<HangUpCustomerResponse> response) {

            }

            @Override
            public void onFailure(Call<HangUpCustomerResponse> call, Throwable t) {

            }
        });
    }

    void stopDevices() {
        doRender = false;
    }


    private void setAudioVideoDataToFalse() {
        AppData.isVideoOnMute = false;
        AppData.isMicOnMute = false;
        AppData.isSpeakerOnMute = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != AppData.mVidyoconnector) {
            /*if (!AppData.isVideoOnMute)
                AppData.mVidyoconnector.setCameraPrivacy(false);*/

            unmuteAudioVideo();
            AppData.mVidyoconnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Foreground);
            Log.e("Resume", "ResumeCalled: Foreground");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        audioVideoCheckingForHold();
        AppData.mVidyoconnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Foreground);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setAudioVideoDataToFalse();
        AppData.RoomKey = "";
        ConnectorPkg.setApplicationUIContext(null);
        ConnectorPkg.uninitialize();
        AppData.mVidyoconnector.disconnect();
        try {
            AppData.mVidyoconnector.unregisterLocalSpeakerEventListener();
            AppData.mVidyoconnector.unregisterParticipantEventListener();
            AppData.mVidyoconnector.unregisterLocalWindowShareEventListener();
            AppData.mVidyoconnector.disconnect();
            AppData.mVidyoconnector.disable();
        } catch (Exception e) {
            Log.e("onDestroy_3", e.toString());
        }

        unregisterReceiver(holdCallReceiver);
        unregisterReceiver(unHoldCallReceiver);
        unregisterReceiver(hangUpReceiver);
        unregisterReceiver(downloadFileReceiver);

//        AppData.SOCKET_MSG_UNHOLD="unhold";
//        AppData.SOCKET_MSG_HOLD="hold";

        unregisterReceiver(networkBroadcastReceiver);
        ShowGuestPromotionalVideoActivity.SGPA.finish();
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        view.removeOnLayoutChangeListener(this);

        int width = view.getWidth();
        int height = view.getHeight();

        AppData.mVidyoconnector.showViewAt(view, 0, 0, width, height);
    }

    @Override
    public void onSuccess() {
        Log.e("onSuccess", "Join Conference Successfully");



        /*Register ParticipantEventListener Interface*/
        AppData.mVidyoconnector.registerLocalMicrophoneEventListener(this);
        AppData.mVidyoconnector.registerParticipantEventListener(this);
        AppData.mVidyoconnector.registerLocalSpeakerEventListener(this);
        AppData.mVidyoconnector.registerLocalWindowShareEventListener(this);
        ConferenceActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                joinProgress.setVisibility(View.GONE);
                jointext.setVisibility(View.GONE);
                iv_menu.setVisibility(View.VISIBLE);
                frame.setBackgroundColor(getResources().getColor(R.color.black));

                if(AppData.CallType.equalsIgnoreCase("audio")){
                    AppData.mVidyoconnector.setCameraPrivacy(true);
                    isVideoOnMute = true;
                    ivCamOn.setVisibility(View.GONE);
                    ivCamOff.setVisibility(View.VISIBLE);
                }
                /*llFunctionality.setVisibility(View.VISIBLE);
                llFunctionality.bringToFront();*/
//                    }
//                }
            }
        });
    }

    private void unmuteAudioVideo() {

        if (AppData.CallType.equalsIgnoreCase("audio")) {

            AppData.mVidyoconnector.setCameraPrivacy(true);

            if (!AppData.isMicOnMute) {
                AppData.mVidyoconnector.setMicrophonePrivacy(false);
            }
            if (!AppData.isSpeakerOnMute) {
                AppData.mVidyoconnector.setSpeakerPrivacy(false);
            }
        } else {
            if (!AppData.isMicOnMute) {
                AppData.mVidyoconnector.setMicrophonePrivacy(false);
            }
            if (!AppData.isSpeakerOnMute) {
                AppData.mVidyoconnector.setSpeakerPrivacy(false);
            }
            if (!AppData.isVideoOnMute) {
                AppData.mVidyoconnector.setCameraPrivacy(false);
            }
        }
    }

   private void audioVideoCheckingForHold() {
        if ((AppData.isMicOnMute) && (AppData.isSpeakerOnMute) && (AppData.isVideoOnMute)) {
            muteFunction();
        } else if ((!AppData.isMicOnMute) && (AppData.isSpeakerOnMute) && (AppData.isVideoOnMute)) {
            AppData.mVidyoconnector.setMicrophonePrivacy(false);
            AppData.mVidyoconnector.setSpeakerPrivacy(true);
            AppData.mVidyoconnector.setCameraPrivacy(true);
        } else if ((AppData.isMicOnMute) && (!AppData.isSpeakerOnMute) && (AppData.isVideoOnMute)) {
            AppData.mVidyoconnector.setMicrophonePrivacy(true);
            AppData.mVidyoconnector.setSpeakerPrivacy(false);
            AppData.mVidyoconnector.setCameraPrivacy(true);
        } else if ((AppData.isMicOnMute) && (AppData.isSpeakerOnMute) && (!AppData.isVideoOnMute)) {
            AppData.mVidyoconnector.setMicrophonePrivacy(true);
            AppData.mVidyoconnector.setSpeakerPrivacy(true);
            AppData.mVidyoconnector.setCameraPrivacy(false);
        } else if ((!AppData.isMicOnMute) && (!AppData.isSpeakerOnMute) && (AppData.isVideoOnMute)) {
            AppData.mVidyoconnector.setMicrophonePrivacy(false);
            AppData.mVidyoconnector.setSpeakerPrivacy(false);
            AppData.mVidyoconnector.setCameraPrivacy(true);
        } else if ((!AppData.isMicOnMute) && (AppData.isSpeakerOnMute) && (!AppData.isVideoOnMute)) {
            AppData.mVidyoconnector.setMicrophonePrivacy(false);
            AppData.mVidyoconnector.setSpeakerPrivacy(true);
            AppData.mVidyoconnector.setCameraPrivacy(false);
        } else if ((AppData.isMicOnMute) && (!AppData.isSpeakerOnMute) && (!AppData.isVideoOnMute)) {
            AppData.mVidyoconnector.setMicrophonePrivacy(true);
            AppData.mVidyoconnector.setSpeakerPrivacy(false);
            AppData.mVidyoconnector.setCameraPrivacy(false);
        } else if ((!AppData.isMicOnMute) && (!AppData.isSpeakerOnMute) && (!AppData.isVideoOnMute)) {
            unmuteAudioVideo();
        }
    }

 /*   private void audioVideoCheckingForHold2(){
        if ((!AppData.isMicOnMute) && (!AppData.isSpeakerOnMute) && (!AppData.isVideoOnMute)) {
            unmuteAudioVideo();
        } else{
            muteFunction();
        }
    }*/
    private void muteFunction() {
        AppData.mVidyoconnector.setMicrophonePrivacy(true);
        AppData.mVidyoconnector.setSpeakerPrivacy(true);
        AppData.mVidyoconnector.setCameraPrivacy(true);
    }


    @Override
    public void onFailure(Connector.ConnectorFailReason reason) {

    }

    @Override
    public void onDisconnected(Connector.ConnectorDisconnectReason reason) {

    }

    @Override
    public void onLocalCameraAdded(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraRemoved(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraSelected(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraStateUpdated(LocalCamera localCamera, Device.DeviceState state) {

    }

    @Override
    public void onLocalMicrophoneAdded(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneRemoved(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneSelected(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneStateUpdated(LocalMicrophone localMicrophone, Device.DeviceState state) {

    }

    @Override
    public void onLocalSpeakerAdded(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerRemoved(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerSelected(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerStateUpdated(LocalSpeaker localSpeaker, Device.DeviceState state) {

    }

    @Override
    public void onLocalWindowShareAdded(LocalWindowShare localWindowShare) {
        AppData.mVidyoconnector.selectLocalWindowShare(localWindowShare);
    }

    @Override
    public void onLocalWindowShareRemoved(LocalWindowShare localWindowShare) {

    }

    @Override
    public void onLocalWindowShareSelected(LocalWindowShare localWindowShare) {

    }

    @Override
    public void onLocalWindowShareStateUpdated(LocalWindowShare localWindowShare, Device.DeviceState state) {

    }

    @Override
    public void onLog(LogRecord logRecord) {

    }

    @Override
    public void onNetworkInterfaceAdded(NetworkInterface networkInterface) {

    }

    @Override
    public void onNetworkInterfaceRemoved(NetworkInterface networkInterface) {

    }

    @Override
    public void onNetworkInterfaceSelected(NetworkInterface networkInterface, NetworkInterface.NetworkInterfaceTransportType transportType) {

    }

    @Override
    public void onNetworkInterfaceStateUpdated(NetworkInterface networkInterface, NetworkInterface.NetworkInterfaceState state) {

    }

    @Override
    public void onParticipantJoined(Participant participant) {

    }

    @Override
    public void onParticipantLeft(Participant participant) {

    }

    @Override
    public void onDynamicParticipantChanged(ArrayList<Participant> participants) {

    }

    @Override
    public void onLoudestParticipantChanged(Participant participant, boolean audioOnly) {

    }

    /**
     * BroadCast Receiver For Hold Call
     */
    private class HoldCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            try {
                AppData.mVidyoconnector.setSpeakerPrivacy(true);
                AppData.mVidyoconnector.setMicrophonePrivacy(true);
                AppData.mVidyoconnector.setCameraPrivacy(true);

                frame.setBackgroundColor(getResources().getColor(R.color.white));
                jointext.setText("Call is on Hold");
                jointext.setVisibility(View.VISIBLE);
                tvhold.setVisibility(View.VISIBLE);
                agenttext.setVisibility(View.VISIBLE);
                tvagentimage.setVisibility(View.VISIBLE);
                fl_videoFrame.setVisibility(View.GONE);

//                Toast.makeText(ConferenceActivity.this,"Call is on HOLD",Toast.LENGTH_SHORT).show();


//            } catch (Exception e) {
//                Log.e("HoldCallReceiver", "ExceptionCause: " + e.getMessage());
//            }
        }
    }

    /**
     * Broadcast Receiver For UnHoldCall
     */
    private class UnHoldCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                AppData.mVidyoconnector.setSpeakerPrivacy(false);
                AppData.mVidyoconnector.setMicrophonePrivacy(false);
                AppData.mVidyoconnector.setCameraPrivacy(false);
                frame.setBackgroundColor(getResources().getColor(R.color.black));
                jointext.setVisibility(View.GONE);
                tvhold.setVisibility(View.GONE);
                agenttext.setVisibility(View.GONE);
                tvagentimage.setVisibility(View.GONE);
                fl_videoFrame.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Log.e("UnHoldCallReceiver", "ExceptionCause: " + e.getMessage());
            }
        }
    }
    /**
     * Broadcast Receiver For Call end by the agent
     */
    private class HangUpReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            AppData.mVidyoconnector.setSpeakerPrivacy(true);
            AppData.mVidyoconnector.setMicrophonePrivacy(true);
            AppData.mVidyoconnector.setCameraPrivacy(true);
            frame.setBackgroundColor(getResources().getColor(R.color.black));
            fl_videoFrame.setVisibility(View.GONE);
            tvhold.setVisibility(View.GONE);
            jointext.setVisibility(View.VISIBLE);
            jointext.setText(R.string.callend_text);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(i);
                }
            }, 3000);
        }
    }

    public void showMenu(){
        View view ;
        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.menua_layout, null);

        ImageView Chat, Rotate,Upload;
        Chat = view.findViewById(R.id.chat);
        Rotate = view.findViewById(R.id.camera_rotate);
        Upload = view.findViewById(R.id.upload);

//        WindowManager.LayoutParams layoutParams = mypopupWindow

//        mypopupWindow = new PopupWindow(view,480, 460, true);  //Closed due to test 14-10-2022
        mypopupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);//new added 14-10-2022

        Rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.mVidyoconnector.cycleCamera();
                if (isVideoOnMute) {
                    AppData.mVidyoconnector.setCameraPrivacy(true);
                } else {
                    AppData.mVidyoconnector.setCameraPrivacy(false);
                }
              mypopupWindow.dismiss();
            }
        });

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EveryoneChatActivity.class);
                startActivity(i);
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ConferenceActivity.this)
                        .setTitle("Upload")
                        .setMessage("Select the type of file")
                        .setPositiveButton("Images", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showFileChooser();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Documents", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showFileChooser();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }
    private void showFileChooser() {
        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // The MIME data type filter
        intent.setType("*/*");

        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.choose_file));

        try {
            startActivityForResult(chooserIntent, FILE_UPLOAD_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FILE_UPLOAD_REQUEST_CODE:
                //If the file selection was successful
                if ((resultCode == RESULT_OK) && (data != null)) {
                    final Uri uri = data.getData();
                    try {


                        imageFilePath = FileUtils.getPath(ConferenceActivity.this, uri);
                        String fileExtension = FileUtils.getExtension(imageFilePath);
                        int lastIndexOfSlash = imageFilePath.lastIndexOf("/");
                        String fileName = imageFilePath.substring((lastIndexOfSlash + 1), imageFilePath.length());
                        Log.e("UploadFile", "UploadFileName: " + fileName);
                        Log.e("EMRRecordActivity", "File Path: " + imageFilePath);
                        uploadFileDuringCall(imageFilePath,uri);

                    } catch (Exception e) {
                        Log.e("patFileUploadDuringCl", "ExceptionCause: " + e.getMessage());
                        Toast.makeText(ConferenceActivity.this, "Please select a proper file manager to select file", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void uploadFileDuringCall(String filePath,Uri uri) {
        final File file = new File(filePath);
        double lengthInBytes = file.length();
        double length = lengthInBytes / (1024 * 1024);
        if (length <= 5.0) {
//            if (file.exists()) {
//                String image_name = file.getAbsolutePath();
            String image_name = filePath.substring(filePath.lastIndexOf("/") + 1);
            getincallemployees();
            fileUploadDuringCallService(file,image_name,uri);

//            }else{
//                Toast.makeText(ConferenceActivity.this, "File doesn't exist", Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(ConferenceActivity.this, "File size should be less than 5 MB", Toast.LENGTH_LONG).show();
        }
    }



    private void getincallemployees() {
        GetEmployeesRequest getEmployeesRequest= new GetEmployeesRequest();
        getEmployeesRequest.setCallId(AppData.Call_ID);

        ApiClient.getApiClient().getincallemployees(getEmployeesRequest).enqueue(new Callback<GetEmployeesResponse>() {
            @Override
            public void onResponse(Call<GetEmployeesResponse> call, Response<GetEmployeesResponse> response) {
                if(response.isSuccessful()) {
                    GetEmployeesResponse getEmployeesResponse = response.body();
                    if(getEmployeesResponse.isStatus()){
                        List<GetEmployeesResponse.PayloadBean> payloadBeanList= response.body().getPayload();
                        employeeLists = new ArrayList<EmployeeList>();
                        for(GetEmployeesResponse.PayloadBean a:payloadBeanList){
                            EmployeeList employeeList = new EmployeeList();
                            employeeList.setId(a.getId());
                            employeeList.setLoginId(a.getLoginId());
                            employeeLists.add(employeeList);
                        }
                        Toast.makeText(ConferenceActivity.this, employeeLists.get(0).getId() + employeeLists.get(0).getLoginId(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ConferenceActivity.this, "Error Message : " + getEmployeesResponse.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ConferenceActivity.this, "Couldn't fetch employee details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetEmployeesResponse> call, Throwable t) {
                Toast.makeText(ConferenceActivity.this, "Cannot upload due to server issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fileUploadDuringCallService(File file, String image_name, Uri uri) {
        
    }

    /**
     * BroadCast Receiver For File Receive During Call
     */
    private class DownloadFileReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConferenceActivity.this, R.style.AlertDialogTheme);

                alertDialog.setTitle("File Receive");
                alertDialog.setMessage("You have received a file");
                alertDialog.setIcon(R.drawable.ic_receive_file);
                alertDialog.setCancelable(false)
                        .setPositiveButton("View", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                 /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppData.FILE_RECEIVE_URL));
                        startActivity(Intent.createChooser(browserIntent, "Select your choice"));*/

                                Log.e("FileReceiveURL: ",AppData.FILE_RECEIVE_URL);
                                Intent launchGoogleChrome = new Intent(Intent.ACTION_VIEW, Uri.parse(AppData.FILE_RECEIVE_URL));
//                        launchGoogleChrome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        launchGoogleChrome.setPackage("com.android.chrome");
//                        launchGoogleChrome.putExtra("com.android.chrome.EXTRA_OPEN_NEW_INCOGNITO_TAB", true);

                                try {
                                    startActivity(Intent.createChooser(launchGoogleChrome,"Select your choice"));
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
//                            launchGoogleChrome.setPackage(null);
//                            startActivity(launchGoogleChrome);
                                    startActivity(Intent.createChooser(launchGoogleChrome,"Select your choice"));
                                }

                                dialog.cancel();
                            }
                        }).setNeutralButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DownloadManager.Request request= new DownloadManager.Request(Uri.parse(AppData.FILE_RECEIVE_URL));
                                String title = URLUtil.guessFileName(AppData.FILE_RECEIVE_URL,null,null);
                                request.setTitle(title);
                                request.setDescription("Downloading file. Please wait...");
                                String cookies= CookieManager.getInstance().getCookie(AppData.FILE_RECEIVE_URL);
                                request.addRequestHeader("cookies",cookies);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                                DownloadManager downloadManager= (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);

                                Toast.makeText(context, "Downloading started.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = alertDialog.create();

                dialog.show();


            } catch (Exception e) {
                Log.e("DownloadFileReceiver", "ExceptionCause: " + e.getMessage());
            }
        }
    }


}