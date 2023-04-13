package com.rank.kuber.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.ChatModel;
import com.rank.kuber.Model.HangUpCustomerRequest;
import com.rank.kuber.Model.HangUpCustomerResponse;
import com.rank.kuber.R;
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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConferenceActivity extends AppCompatActivity implements Connector.IConnect,
        Connector.IRegisterParticipantEventListener,
        Connector.IRegisterLogEventListener,
        Connector.IRegisterNetworkInterfaceEventListener, View.OnLayoutChangeListener,
        Connector.IRegisterLocalCameraEventListener, Connector.IRegisterLocalMicrophoneEventListener, Connector.IRegisterLocalSpeakerEventListener, Connector.IRegisterLocalWindowShareEventListener {

    String TAG = "ConferenceActivity";
    private View joinForm;
    private LinearLayout llFunctionality;
    private ImageView tvEndCall;
    private ImageView ivMicOn, ivMicOff, ivCamOn, ivCamOff, ivCamRotateBack, ivCamRotateFront, iv_menu, ivSpeakerOn, ivSpeakerOff;
    private View promoVideoLayout;
    private TextView txtAgentInfo;
    private VideoView promotional_videovw;
    ViewGroup frame;
    private FrameLayout fl_videoFrame;

    private HangUpCustomerRequest hangUpCustomerRequest;



    private boolean doRender = false;
    private boolean callStarted = false;
    private ProgressBar joinProgress;
    public static boolean isSpeakerOnMute, isVideoOnMute, isMicOnMute;
    private long joinCountDown = 0L;
    private boolean showStatistic = false;
    String randomString1, randomString2;
    String generatedRandomString;
    private Handler handler;
    private static boolean isOnHold;
//    String Host = "https://ranktechsolutions.platform.vidyo.io";
    String Host = "https://ranktechsolutions.platform.vidyo.io";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //code that displays the content in full screen mode
        setContentView(R.layout.activity_conference);

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
        joinProgress.setVisibility(View.VISIBLE);

        /*AppData.mVidyoconnector.registerLocalCameraEventListener(this);
        AppData.mVidyoconnector.registerLocalMicrophoneEventListener(this);
        AppData.mVidyoconnector.registerLocalSpeakerEventListener(this);
        AppData.mVidyoconnector.registerParticipantEventListener(this);*/
//        frame.addOnLayoutChangeListener(this);

        initObjects();
        buttonClickEvent();

    }

    private void initObjects() {
        Log.e(TAG, "Initobject");
        handler = new Handler();

        frame = findViewById(R.id.main_content);
        iv_menu = findViewById(R.id.imgvw_menu_plus);
        fl_videoFrame = findViewById(R.id.join_params_frame);
        llFunctionality = findViewById(R.id.llFunctionality);
        tvEndCall = findViewById(R.id.endCall);
        ivMicOn = findViewById(R.id.ivMicOn);
        ivMicOff = findViewById(R.id.ivMicOff);
        ivCamOn = findViewById(R.id.ivCamOn);
        ivCamOff = findViewById(R.id.ivCamOff);
        ivCamRotateBack = findViewById(R.id.ivCamRotateBack);
        ivCamRotateFront = findViewById(R.id.ivCamRotateFront);
        ivSpeakerOn=findViewById(R.id.ivSpeakeron);
        ivSpeakerOff=findViewById(R.id.ivSpeakeroff);

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

        ivCamRotateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    jniBridge.SetCameraDevice(AppUtils.BACK_CAMERA);
                AppData.mVidyoconnector.cycleCamera();
                if (isVideoOnMute) {
                    AppData.mVidyoconnector.setCameraPrivacy(true);
                } else {
                    AppData.mVidyoconnector.setCameraPrivacy(false);
                }
                ivCamRotateBack.setVisibility(View.GONE);
                ivCamRotateFront.setVisibility(View.VISIBLE);
            }
        });

        ivCamRotateFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.mVidyoconnector.cycleCamera();
                if (isVideoOnMute) {
                    AppData.mVidyoconnector.setCameraPrivacy(true);
                } else {
                    AppData.mVidyoconnector.setCameraPrivacy(false);
                }
                ivCamRotateBack.setVisibility(View.VISIBLE);
                ivCamRotateFront.setVisibility(View.GONE);

            }
        });

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

             AppData.mVidyoconnector = new Connector(fl_videoFrame, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Tiles, 15, logLevel, "", 0);
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

        /*joinButton.setEnabled(true);
        cancelButton.setEnabled(false);*/

        joinProgress.setVisibility(View.GONE);

//        lmiDeviceManagerView.setVisibility(View.GONE);

//        controlForm.setVisibility(View.GONE);
        llFunctionality.setVisibility(View.GONE);

        stopDevices();

        if (AppData.mVidyoconnector != null) {
//            jniBridge.RenderRelease();

//            jniBridge.LeaveConference();
//            callCancel();
            finish();
            startActivity(new Intent(ConferenceActivity.this, FeedbackActivity.class));

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
                iv_menu.setVisibility(View.VISIBLE);

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
}