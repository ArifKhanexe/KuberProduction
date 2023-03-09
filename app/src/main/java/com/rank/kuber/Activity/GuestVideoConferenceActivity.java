package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.rank.kuber.Common.AppData;
import com.rank.kuber.Interfaces.IVideoFrameListener;
import com.rank.kuber.Interfaces.RoomActivityListener;
import com.rank.kuber.Library.VidyoioLibrary;
import com.rank.kuber.R;
import com.rank.kuber.VideoFrame.VideoFrameLayout;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Endpoint.Participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class GuestVideoConferenceActivity extends AppCompatActivity implements
        View.OnClickListener, RoomActivityListener, IVideoFrameListener {


    // Define the various states of this application.
    enum VidyoConnectorState {
        Connecting,
        Connected,
        Disconnecting,
        Disconnected,
        DisconnectedUnexpected,
        Failure,
        FailureInvalidResource
    }

    // Map the application state to the status to display in the toolbar.
    private static final Map<VidyoConnectorState, String> mStateDescription = new HashMap<VidyoConnectorState, String>() {{
        put(VidyoConnectorState.Connecting, "Connecting...");
        put(VidyoConnectorState.Connected, "Connected");
        put(VidyoConnectorState.Disconnecting, "Disconnecting...");
        put(VidyoConnectorState.Disconnected, "Disconnected");
        put(VidyoConnectorState.DisconnectedUnexpected, "Unexpected disconnection");
        put(VidyoConnectorState.Failure, "Connection failed");
        put(VidyoConnectorState.FailureInvalidResource, "Invalid Resource ID");
    }};

    // - This arbitrary, app-internal constant represents a group of requested permissions.
    // - For simplicity, this app treats all desired permissions as part of a single group.
    private final int PERMISSIONS_REQUEST_ALL = 1988;

    private VidyoConnectorState mVidyoConnectorState = VidyoConnectorState.Disconnected;
    //    private Logger mLogger = Logger.getInstance();
//    private Connector mVidyoConnector = null;
    private VidyoioLibrary mVidyoioLibrary = null;

    private LocalCamera mLastSelectedCamera = null;
    private ToggleButton mToggleConnectButton;
    private ToggleButton mMicrophonePrivacyButton;
    private ToggleButton mSpeakerPrivacyButton;
    private ToggleButton mCameraPrivacyButton;

    private ImageView menu_imageview;
    private ProgressBar mConnectionSpinner;
    private LinearLayout mControlsLayout;
    private LinearLayout mToolbarLayout;
    private EditText mHost;
    private EditText mDisplayName;
    private EditText mToken;
    private EditText mResourceId;
    private TextView mToolbarStatus;
    private TextView mClientVersion;
    private RelativeLayout conference_layout;
    private VideoFrameLayout mVideoFrame;
    private TextView timerText;

    private boolean mHideConfig = false;
    private boolean mAutoJoin = false;
    private boolean mAllowReconnect = true;
    private boolean mCameraPrivacy = false;
    private boolean mMicrophonePrivacy = false;
    private boolean mSpeakerPrivacy = false;
    private boolean mEnableDebug = false;
    private String mReturnURL = null;
    private String mExperimentalOptions = null;
    private boolean mRefreshSettings = true;
    private boolean mDevicesSelected = true;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = null;
    private boolean mVidyoCloudJoin = false;
    private Handler handler;
    private String mPortal;  // Used for VidyoCloud systems, not Vidyo.io
    private String mRoomKey; // Used for VidyoCloud systems, not Vidyo.io
    private String mRoomPin; // Used for VidyoCloud systems, not Vidyo.io

    /*To handle video related menu data*/
//    private DialogPlus dialogPlus;
    //    private TextView mMicrophonePrivacyButton, mSpeakerPrivacyButton,mCameraPrivacyButton,dialogplus_invite,dialogplus_chat;
    private TextView dialogplus_upload, dialogplus_switchcamera, dialogplus_mutespeaker,
            dialogplus_unmute_speaker, dialogplus_mute_mic, dialogplus_unmute_mic,
            dialogplus_mutevideo, dialogplus_unmute_video,dialogplus_hold,dialogplus_unhold,
            dialogplus_chat,dialogplus_generate_prescription,dialogplus_download;



    //    int deniedPermissionCount = 0; // Used to count denied permission;
    String TAG = "ConferenceActivity";
    //    String Host =  "https://RanktechSolutions.vidyocloud.com";   //TODO old Portal
    Boolean callEndedByHeadEmployee = false;
    //TODO new Portal Host vidyo 7-01-2022
    String Host = "ranktechsolutions.platform.vidyo.io";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE); //code that displays the content in full screen mode
        setContentView(R.layout.activity_guest_video_conference);

        conference_layout = findViewById(R.id.conference_layout);
        mToolbarLayout = (LinearLayout) findViewById(R.id.toolbarLayout);
        mVideoFrame = (VideoFrameLayout) findViewById(R.id.videoFrame);
        mVideoFrame.Register(this);

        mToolbarStatus = (TextView) findViewById(R.id.toolbarStatusText);
        timerText = (TextView) findViewById(R.id.timer_txt);

        // Set the onClick listeners for the buttons
        mToggleConnectButton = (ToggleButton) findViewById(R.id.connect);
        mToggleConnectButton.setOnClickListener(this);
        mMicrophonePrivacyButton = (ToggleButton) findViewById(R.id.microphone_privacy);
        mMicrophonePrivacyButton.setOnClickListener(this);
        mSpeakerPrivacyButton = (ToggleButton) findViewById(R.id.speaker_privacy);
        mSpeakerPrivacyButton.setOnClickListener(this);
        mCameraPrivacyButton = (ToggleButton) findViewById(R.id.camera_privacy);
        mCameraPrivacyButton.setOnClickListener(this);
        ToggleButton button = (ToggleButton) findViewById(R.id.camera_switch);
        button.setOnClickListener(this);
//        button = (ToggleButton) findViewById(R.id.toggle_debug);
//        button.setOnClickListener(this);
        menu_imageview =  findViewById(R.id.iv_menuplus);
        menu_imageview.setOnClickListener(this);


        /* TODO Init Recyler View  */
        //fileListRecyclerView = (RecyclerView) findViewById(R.id.fileListRecyclerView);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConferenceActivity.this);
        //fileListRecyclerView.setLayoutManager(linearLayoutManager);

        //TODO use this Method for new API to get Downlaod response path  to Downlaod
        //CallGetpatientDocumets();

        /** TODO This patientDocumentTypeCallbackService(); method using for avoiding Hardcoding for file Uploading 19-01-2022  **/
        //patientDocumentTypeCallbackService();

        //handler = new Handler();
        //Get the instance of singleton class VidyoioLibrary.
        mVidyoioLibrary = VidyoioLibrary.getInstance(GuestVideoConferenceActivity.this,this);

        // Set the application's UI context to this activity.
//        ConnectorPkg.setApplicationUIContext(this);
        mVidyoioLibrary.setApplicationUIContext(GuestVideoConferenceActivity.this);


        Boolean b= mVidyoioLibrary.initialize();
        // Initialize the VidyoClient library - this should be done once in the lifetime of the application.
//
        if (b) {
            // Construct Connector and register for events.
            try {
                Log.d(TAG, "Constructing Connector");

                mVidyoioLibrary.constructConnector(mVideoFrame);
                mVidyoioLibrary.setEventListener();
                Log.d(TAG, "EVENT LISTENER Already CALLED");

                // Beginning in Android 6.0 (API level 23), users grant permissions to an app while
                // the app is running, not when they install the app. Check whether app has permission
                // to access what is declared in its manifest.
                // mVidyoioLibrary.showViewAt(mVideoFrame);
//                requestPermission();
                this.startVideoViewSizeListener();

            } catch (Exception e) {
                Log.d(TAG, "Connector Construction failed");
                Log.d(TAG, e.getMessage());
            }
//                mVidyoioLibrary.setRoomActivityListener(this);

            joinConference();

        }
    }


    private void startVideoViewSizeListener() {
        Log.d(TAG,"EVENT LISTENER startVideoViewSizeListener");

        // Render the video each time that the video view (mVideoFrame) is resized. This will
        // occur upon activity creation, orientation changes, and when foregrounding the app.
        ViewTreeObserver viewTreeObserver = mVideoFrame.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Specify the width/height of the view to render to.
                    Log.d(TAG,"EVENT LISTENER showViewAt: width = " + mVideoFrame.getWidth() + ", height = " + mVideoFrame.getHeight());
//                    mVidyoConnector.showViewAt(mVideoFrame, 0, 0, mVideoFrame.getWidth(), mVideoFrame.getHeight());
                    mVidyoioLibrary.showViewAt(mVideoFrame);
                    mOnGlobalLayoutListener = this;
                }
            });
        } else {
            Log.d(TAG,"ERROR in startVideoViewSizeListener! Video will not be rendered.");
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent");
        super.onNewIntent(intent);

        // Set the refreshSettings flag so the app settings are refreshed in onStart
        mRefreshSettings = true;

        // New intent was received so set it to use in onStart
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        Log.d(TAG,"onStart");
        super.onStart();

        // Initialize or refresh the app settings.
        // When app is first launched, mRefreshSettings will always be true.
        // Each successive time that onStart is called, app is coming back to foreground so check if the
        // settings need to be refreshed again, as app may have been launched via URI.
        Log.d(TAG,"Refresh settings "+mRefreshSettings);
//
//           this.applySettings();
//        joinConference();
//        }
        //mRefreshSettings = false;
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        AppData.currentContext = this;
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause");

//        if (!AppData.openPrescriptionPage){
        mVidyoioLibrary.selectLocalCamera(null);
        mVidyoioLibrary.selectLocalMicrophone(null);
        mVidyoioLibrary.selectLocalSpeaker(null);
        mDevicesSelected = false;
//        }
        mVidyoioLibrary.setMode("background");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop");

        if (mVidyoConnectorState != VidyoConnectorState.Connected &&
                mVidyoConnectorState != VidyoConnectorState.Connecting) {
            Log.d(TAG,"onStop CALL NOT CONNECTED");

            mVidyoioLibrary.selectLocalCamera(null);
            mVidyoioLibrary.selectLocalMicrophone(null);
            mVidyoioLibrary.selectLocalSpeaker(null);
            mDevicesSelected = false;
        }
        mVidyoioLibrary.setMode("background");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG,"onRestart");

        mVidyoioLibrary.setMode("foreground");
//            Toast.makeText(MainActivity.this,"Restart called",Toast.LENGTH_SHORT).show();
        if (!mDevicesSelected) {
            // Devices have been released when backgrounding (in onStop). Re-select them.
            mDevicesSelected = true;
//                Toast.makeText(MainActivity.this,"Devices selected "+mDevicesSelected,Toast.LENGTH_SHORT).show();

            // Select the previously selected local camera and default mic/speaker
            mVidyoioLibrary.selectLocalCamera("restart");
            mVidyoioLibrary.selectLocalMicrophone("restart");
            mVidyoioLibrary.selectLocalSpeaker("restart");

            mVidyoioLibrary.selectDefaultMicrophone();
            mVidyoioLibrary.selectDefaultSpeaker();

            mVidyoioLibrary.setCameraPrivacy(mCameraPrivacy);
            mVidyoioLibrary.setMicrophonePrivacy(mMicrophonePrivacy);
            mVidyoioLibrary.setSpeakerPrivacy(mSpeakerPrivacy);
        }
//        }
        super.onRestart();

//        mDevicesSelected = mVidyoioLibrary.restartVidyoConnector(mDevicesSelected,mCameraPrivacy,mMicrophonePrivacy);
    }

    @Override
    public void onBackPressed(){

//        if (AppData.conferenceCallOn) {
//            showAlertDialogOnBackPressed();
//        }else{
//            super.onBackPressed();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setAudioVideoDataToFalse();

        // Uninitialize the VidyoClient library - this should be done once in the lifetime of the application.
        mVidyoioLibrary.unInitialize();
        mVideoFrame = null;
        mVidyoioLibrary.setApplicationUIContext(null);

        AppData.CallType = "";
        AppData.Portal_Address = "";
        AppData.Agent_id= "";
        AppData.SocketHostUrl = "";
        AppData.CustFName="";
        AppData.CustLName="";
        AppData.RoomName= "";
        AppData.Entity_id="";
        AppData.Agent_Name = "";
        AppData.Call_ID = "";
        AppData.RoomKey = "";


//        listOfUsersId = null;
//        listOfUsersName = null;
        //clearAllLists();
        mVidyoioLibrary.unregisterEventListener();



//        AppData.clearCallData();


    }

    // The device interface orientation has changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG,"onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


    private void setAudioVideoDataToFalse() {
        AppData.isVideoOnMute = false;
        AppData.isMicOnMute = false;
        AppData.isSpeakerOnMute = false;
    }



    private void joinConference(){

            changeState(VidyoConnectorState.Connecting);
            try {
                mVidyoioLibrary.vidyoioCallConnect(
                        AppData.Portal_Address,
                        AppData.CustName,
                        AppData.RoomKey,
                        AppData.RoomName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    /*
     * Button Event Callbacks
     */

    @Override
    public void onClick(View v) {
//        if (mVidyoConnector != null) {
        switch (v.getId()) {
            case R.id.connect:
                // Connect or disconnect.
//                    this.toggleConnect();
                showAlertDialogOnBackPressed();
                break;

            case R.id.camera_switch:

                mVidyoioLibrary.cycleCamera();
                break;

            case R.id.camera_privacy:
                // Toggle the camera privacy.
                mCameraPrivacy = mCameraPrivacyButton.isChecked();
                mVidyoioLibrary.setCameraPrivacy(mCameraPrivacy);
                break;

            case R.id.microphone_privacy:
                // Toggle the microphone privacy.
                mMicrophonePrivacy = mMicrophonePrivacyButton.isChecked();
                mVidyoioLibrary.setMicrophonePrivacy(mMicrophonePrivacy);
                break;

            case R.id.speaker_privacy:
                // Toggle the speaker privacy.
                mSpeakerPrivacy = mSpeakerPrivacyButton.isChecked();
                mVidyoioLibrary.setSpeakerPrivacy(mSpeakerPrivacy);
                break;

            case R.id.iv_menuplus:
                //showUpwardDialogPlus();
                break;

            default:
                Log.d(TAG,"onClick: Unexpected click event, id=" + v.getId());
                break;
        }
    }


 /*   // The state of the VidyoConnector connection changed, reconfigure the UI.
    // If connected, dismiss the controls language_layout
    private void changeState(VidyoConnectorState state) {
        Log.d(TAG,"changeState: " + state.toString());

        mVidyoConnectorState = state;

        // Execute this code on the main thread since it is updating the UI language_layout.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set the status text in the toolbar.
//                mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));

                // Depending on the state, do a subset of the following:
                // - update the toggle connect button to either start call or end call image: mToggleConnectButton
                // - display toolbar in case it is hidden: mToolbarLayout
                // - show/hide the connection spinner: mConnectionSpinner
                // - show/hide the input form: mControlsLayout
                switch (mVidyoConnectorState) {
                    case Connecting:
//                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        mToggleConnectButton.setChecked(true);
//                        mConnectionSpinner.setVisibility(View.VISIBLE);
                        break;

                    case Connected:
                        //mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        showToast(mStateDescription.get(mVidyoConnectorState));
                        mToolbarStatus.setVisibility(View.GONE);
                        mToggleConnectButton.setChecked(true);
                        // Keep the device awake if connected.
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        break;

                    case Disconnecting:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        // The button just switched to the callStart image.
                        // Change the button back to the callEnd image because do not want to assume that the Disconnect
                        // call will actually end the call. Need to wait for the callback to be received
                        // before swapping to the callStart image.
                        mToggleConnectButton.setChecked(true);
                        break;

                    case Disconnected:
                    case DisconnectedUnexpected:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                    case Failure:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                    case FailureInvalidResource:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        mToggleConnectButton.setChecked(false);
                        mToolbarLayout.setVisibility(View.VISIBLE);
//                        mConnectionSpinner.setVisibility(View.INVISIBLE);

                        // If a return URL was provided as an input parameter, then return to that application
                        if (mReturnURL != null) {
                            // Provide a callstate of either 0 or 1, depending on whether the call was successful
                            Intent returnApp = getPackageManager().getLaunchIntentForPackage(mReturnURL);
                            returnApp.putExtra("callstate", (mVidyoConnectorState == VidyoConnectorState.Disconnected) ? 1 : 0);
                            startActivity(returnApp);
                        }

                        // If the allow-reconnect flag is set to false and a normal (non-failure) disconnect occurred,
                        // then disable the toggle connect button, in order to prevent reconnection.
                        if (!mAllowReconnect && (mVidyoConnectorState == VidyoConnectorState.Disconnected)) {
                            mToggleConnectButton.setEnabled(false);
                            mToolbarStatus.setText("Call ended");
                        }

                        // Allow the device to sleep if disconnected.
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        break;
                }
            }
        });
    }
*/

    public void showAlertDialogOnBackPressed(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GuestVideoConferenceActivity.this);
        alertDialog.setTitle("Exit");

        alertDialog.setMessage("Do you want to exit or disconnect the call");

        alertDialog.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // hangup by customer

                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = alertDialog.create();

        dialog.show();

    }


    // The state of the VidyoConnector connection changed, reconfigure the UI.
    // If connected, dismiss the controls language_layout
    private void changeState(VidyoConnectorState state) {
        Log.d(TAG,"changeState: " + state.toString());

        mVidyoConnectorState = state;

        // Execute this code on the main thread since it is updating the UI language_layout.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Set the status text in the toolbar.
//                mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));

                // Depending on the state, do a subset of the following:
                // - update the toggle connect button to either start call or end call image: mToggleConnectButton
                // - display toolbar in case it is hidden: mToolbarLayout
                // - show/hide the connection spinner: mConnectionSpinner
                // - show/hide the input form: mControlsLayout
                switch (mVidyoConnectorState) {
                    case Connecting:
//                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        mToggleConnectButton.setChecked(true);
//                        mConnectionSpinner.setVisibility(View.VISIBLE);
                        break;

                    case Connected:
                        //mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        Toast.makeText(GuestVideoConferenceActivity.this, mStateDescription.get(mVidyoConnectorState), Toast.LENGTH_SHORT).show();;
                        mToolbarStatus.setVisibility(View.GONE);
                        mToggleConnectButton.setChecked(true);
                        // Keep the device awake if connected.
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        break;

                    case Disconnecting:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        // The button just switched to the callStart image.
                        // Change the button back to the callEnd image because do not want to assume that the Disconnect
                        // call will actually end the call. Need to wait for the callback to be received
                        // before swapping to the callStart image.
                        mToggleConnectButton.setChecked(true);
                        break;

                    case Disconnected:
                    case DisconnectedUnexpected:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                    case Failure:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                    case FailureInvalidResource:
                        mToolbarStatus.setText(mStateDescription.get(mVidyoConnectorState));
                        mToggleConnectButton.setChecked(false);
                        mToolbarLayout.setVisibility(View.VISIBLE);
//                        mConnectionSpinner.setVisibility(View.INVISIBLE);

                        // If a return URL was provided as an input parameter, then return to that application
                        if (mReturnURL != null) {
                            // Provide a callstate of either 0 or 1, depending on whether the call was successful
                            Intent returnApp = getPackageManager().getLaunchIntentForPackage(mReturnURL);
                            returnApp.putExtra("callstate", (mVidyoConnectorState == VidyoConnectorState.Disconnected) ? 1 : 0);
                            startActivity(returnApp);
                        }

                        // If the allow-reconnect flag is set to false and a normal (non-failure) disconnect occurred,
                        // then disable the toggle connect button, in order to prevent reconnection.
                        if (!mAllowReconnect && (mVidyoConnectorState == VidyoConnectorState.Disconnected)) {
                            mToggleConnectButton.setEnabled(false);
                            mToolbarStatus.setText("Call ended");
                        }

                        // Allow the device to sleep if disconnected.
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        break;
                }
            }
        });
    }

    @Override
    public void onVideoFrameClicked() {

    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onRoomConnected() {
        this.changeState(VidyoConnectorState.Connected);

    }

    @Override
    public void onRoomDisconnected(String message) {
        if (message.equals(mStateDescription.get(VidyoConnectorState.Failure))){
            this.changeState(VidyoConnectorState.Failure);
            finish();
        }
        else if(message.equals(mStateDescription.get(VidyoConnectorState.Disconnected))){
            this.changeState(VidyoConnectorState.Disconnected);
            finish();
        }
        else if (message.equals(mStateDescription.get(VidyoConnectorState.DisconnectedUnexpected))){
            this.changeState(VidyoConnectorState.DisconnectedUnexpected);
            finish();
        }
    }

    @Override
    public void onActiveParticipantChanged(List streamList) {

    }

    @Override
    public void onScreenShareStopped() {

    }

    @Override
    public void onMicMuted() {

    }

    @Override
    public void onMicUnMuted() {

    }

    @Override
    public void onVideoMuted() {

    }

    @Override
    public void onVideoUnMuted() {

    }

    @Override
    public void onSpeakerMuted() {

    }

    @Override
    public void onSpeakerUnMuted() {

    }

    @Override
    public void onScreenShotCaptured(Bitmap bitmapImage) {

    }

    @Override
    public void onParticipantJoined(Participant participant) {

    }

    @Override
    public void onParticipantLeft(Participant participant) {

    }
}