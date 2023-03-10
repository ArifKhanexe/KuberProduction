//package com.rank.kuber.Library;
//
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.rank.kuber.Interfaces.IVideoFrameListener;
//import com.rank.kuber.Interfaces.RoomActivityListener;
//import com.rank.kuber.VideoFrame.VideoFrameLayout;
//import com.vidyo.VidyoClient.Connector.Connector;
//import com.vidyo.VidyoClient.Connector.ConnectorPkg;
//import com.vidyo.VidyoClient.Device.Device;
//import com.vidyo.VidyoClient.Device.LocalCamera;
//import com.vidyo.VidyoClient.Device.LocalMicrophone;
//import com.vidyo.VidyoClient.Device.LocalSpeaker;
//import com.vidyo.VidyoClient.Endpoint.LogRecord;
//import com.vidyo.VidyoClient.Endpoint.Participant;
//import com.vidyo.VidyoClient.NetworkInterface;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class VidyoioLibrary implements Connector.IConnect,
//        Connector.IRegisterLogEventListener,
//        Connector.IRegisterNetworkInterfaceEventListener,
//        Connector.IRegisterLocalCameraEventListener,
//        Connector.IRegisterLocalMicrophoneEventListener,
//        Connector.IRegisterLocalSpeakerEventListener,
//        Connector.IRegisterParticipantEventListener,
//        IVideoFrameListener {
//
//    // Define the various states of this application.
//    enum VidyoConnectorState {
//        Connecting,
//        Connected,
//        Disconnecting,
//        Disconnected,
//        DisconnectedUnexpected,
//        Failure,
//        FailureInvalidResource
//    }
//
//    // Map the application state to the status to display in the toolbar.
//    private static final Map<VidyoConnectorState, String> mStateDescription = new HashMap<VidyoConnectorState, String>() {{
//        put(VidyoConnectorState.Connecting, "Connecting...");
//        put(VidyoConnectorState.Connected, "Connected");
//        put(VidyoConnectorState.Disconnecting, "Disconnecting...");
//        put(VidyoConnectorState.Disconnected, "Disconnected");
//        put(VidyoConnectorState.DisconnectedUnexpected, "Unexpected disconnection");
//        put(VidyoConnectorState.Failure, "Connection failed");
//        put(VidyoConnectorState.FailureInvalidResource, "Invalid Resource ID");
//    }};
//
//    // Helps check whether app has permission to access what is declared in its manifest.
//    // - Permissions from app's manifest that have a "protection level" of "dangerous".
//    private static final String[] mPermissions = new String[] {
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
//    };
//    // - This arbitrary, app-internal constant represents a group of requested permissions.
//    // - For simplicity, this app treats all desired permissions as part of a single group.
//    private final int PERMISSIONS_REQUEST_ALL = 1988;
//
//    private VidyoConnectorState mVidyoConnectorState = VidyoConnectorState.Disconnected;
//
//    private static Context context;
//    //    private static Logger mLogger = Logger.getInstance();
//    private static volatile VidyoioLibrary instance = null;
//
//    private static Connector mVidyoConnector = null;
//    private static RoomActivityListener roomActivityListener;
//
//    private LocalCamera mLastSelectedCamera = null;
//    private LocalMicrophone mLastSelectedMicrophone = null;
//    private LocalSpeaker mLastSelectedSpeaker = null;
//    private boolean connectorNotNull;
//    private boolean connectedORNot;
//    private boolean allPermissionsAllowed = true;
//    private String errorMessage = "VidyoIO connector is null";
//
//    String TAG = "VidyoioLibrary";
//
//
//    private VidyoioLibrary() {
//
//        if (instance!=null){
//
//            throw new RuntimeException("Unable to create instance");
//
//        }
//    }
//
//
//    public static VidyoioLibrary getInstance(Activity activity, RoomActivityListener listener){
//
//        context = activity;
//
//        if (instance == null){
//
//            synchronized(VidyoioLibrary.class){
//
//                if (instance == null)
//                    instance = new VidyoioLibrary();
//
//                // Register for local camera, network interface, log events.
////               instance.setEventListener();
//            }
//        }
//
//        instance.setRoomActivityListener(listener);
////
// //       instance.requestPermission(activity);
//
//        return instance;
//    }
//
//    public int getAllPermissionsLength(){
//        return mPermissions.length;
//    }
//
//    public void requestPermission(Activity activity){
//
//        Log.d(TAG,"EVENT LISTENER PERMISSION CALLING");
//        if (Build.VERSION.SDK_INT > 22) {
//            allPermissionsAllowed = false;
//            List<String> permissionsNeeded = new ArrayList<>();
//
//            for (String permission : mPermissions) {
//                // Check if the permission has already been granted.
//                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
//                    permissionsNeeded.add(permission);
//            }
//
//
//            if (permissionsNeeded.size() > 0) {
//                // Request any permissions which have not been granted. The result will be called back in onRequestPermissionsResult.
////              ActivityCompat.requestPermissions(activity, permissionsNeeded.toArray(new String[0]), PERMISSIONS_REQUEST_ALL);
//                ActivityCompat.requestPermissions(activity, permissionsNeeded.toArray(new String[0]), PERMISSIONS_REQUEST_ALL);
//            } else {
//                allPermissionsAllowed = true;
////              Toast.makeText(activity,"Permission already granted",Toast.LENGTH_SHORT).show();
//                // Begin listening for video view size changes.
////              this.startVideoViewSizeListener();
//            }
//        }
//
//    }
//
//
//    public void setRoomActivityListener(RoomActivityListener listener){
//        roomActivityListener = listener;
//    }
//
//    // Set the application's UI context to this activity.
//    public void setApplicationUIContext(Activity activity){
//        if (activity!=null) {
//            ConnectorPkg.setApplicationUIContext(activity);
//        }
//        else{
//            ConnectorPkg.setApplicationUIContext(null);
//        }
//    }
//
//    // Initialize the VidyoClient library - this should be done once in the lifetime of the application.
//    public boolean initialize(){
//        return ConnectorPkg.initialize();
//    }
//
//    public void unInitialize(){
//        ConnectorPkg.uninitialize();
//    }
//
//    public void constructConnector(VideoFrameLayout mVideoFrame){
//        try {
//            mVidyoConnector = new Connector(mVideoFrame,
//                    Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,
//                    7,
//                    "",
//                    "",
//                    0);
//        }catch (Exception ex){
//            ex.printStackTrace();
//            Log.d("Exception","Connector exception "+ex);
//        }
//
//        Log.d("VidyoIOLibrary","Version"+mVidyoConnector.getVersion());
////      setEventListener();
//
//    }
//
//
//    public void setEventListener(){
//        // Register for local camera events
//
//        if (mVidyoConnector!=null) {
//
//            Log.d(TAG,"EVENT LISTENER CALLING");
//            if (!mVidyoConnector.registerLocalCameraEventListener(this)) {
//                Log.d(TAG,"registerLocalCameraEventListener failed");
//            }
//
//            // Register for network interface events
//            if (!mVidyoConnector.registerNetworkInterfaceEventListener(this)) {
//                Log.d(TAG,"registerNetworkInterfaceEventListener failed");
//            }
//            // Register for microphone interface events
//            if (!mVidyoConnector.registerLocalMicrophoneEventListener(this)) {
//                Log.d(TAG,"registerMicrophoneInterfaceEventListener failed");
//            }
//            // Register for speaker interface events
//            if (!mVidyoConnector.registerLocalSpeakerEventListener(this)) {
//                Log.d(TAG,"registerSpeakerInterfaceEventListener failed");
//            }
//            // Register for participants interface events
//            if (!mVidyoConnector.registerParticipantEventListener(this)) {
//                Log.d(TAG,"registerParticipantInterfaceEventListener failed");
//            }
//            // Register for log events
//            if (!mVidyoConnector.registerLogEventListener(this, "info@VidyoClient info@VidyoConnector warning")) {
//                Log.d(TAG,"registerLogEventListener failed");
//            }
//            Log.d(TAG,"EVENT LISTENER CALLED");
//        }else{
//            roomActivityListener.onError(errorMessage);
//        }
//    }
//
//    public void unregisterEventListener(){
//        // Register for local camera events
//
//        if (mVidyoConnector!=null) {
//
//            Log.d(TAG,"EVENT LISTENER CALLING");
//
//
//
//            try {
//                mVidyoConnector.unregisterLocalSpeakerEventListener();
//                mVidyoConnector.unregisterParticipantEventListener();
//                mVidyoConnector.unregisterLocalWindowShareEventListener();
//                mVidyoConnector.unregisterLocalCameraEventListener();
//                mVidyoConnector.unregisterLocalMicrophoneEventListener();
//                mVidyoConnector.disconnect();
//                mVidyoConnector.disable();
////                mVidyoConnector = null;
////            AppData.vidyoConnector.disable();
//                Log.e("onDestroy","Destroy 8");
//            } catch (Exception e) {
//                Log.e("onDestroy_4", e.toString());
//            }
//
////            if (!mVidyoConnector.unregisterLocalCameraEventListener()) {
////                Log.d(TAG,"unregisterLocalCameraEventListener failed");
////            }
////
////            // Register for network interface events
////            if (!mVidyoConnector.unregisterNetworkInterfaceEventListener()) {
////                Log.d(TAG,"unregisterNetworkInterfaceEventListener failed");
////            }
////            // Register for microphone interface events
////            if (!mVidyoConnector.unregisterLocalMicrophoneEventListener()) {
////                Log.d(TAG,"unregisterMicrophoneInterfaceEventListener failed");
////            }
////            // Register for speaker interface events
////            if (!mVidyoConnector.unregisterLocalSpeakerEventListener()) {
////                Log.d(TAG,"unregisterSpeakerInterfaceEventListener failed");
////            }
////            // Register for participants interface events
////            if (!mVidyoConnector.unregisterParticipantEventListener()) {
////                Log.d(TAG,"unregisterParticipantInterfaceEventListener failed");
////            }
////            // Register for log events
////            if (!mVidyoConnector.unregisterLogEventListener()) {
////                Log.d(TAG,"unregisterLogEventListener failed");
////            }
////            Log.d(TAG,"EVENT LISTENER CALLED");
//        }else{
//            roomActivityListener.onError(errorMessage);
//        }
//
//    }
//
//
//
//    public void showViewAt(VideoFrameLayout mVideoFrame){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.showViewAt(mVideoFrame, 0, 0, mVideoFrame.getWidth(), mVideoFrame.getHeight());
//        }else{
//            roomActivityListener.onError(errorMessage);
//        }
//    }
//
//
//    // ONSTOP ONRESTART ONDESTROY Select the previously selected local camera and default mic/speaker
//
//    public void selectLocalCamera(String nullNotNull){
//        if (mVidyoConnector!=null) {
//            if (nullNotNull!=null) {
//                mVidyoConnector.selectLocalCamera(mLastSelectedCamera);
//            } else {
//                mVidyoConnector.selectLocalCamera(null);
//                Log.d("SelectedLocalCamera ","null");
//            }
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorLC ","not connected");
//        }
//    }
//
//    public void selectLocalMicrophone(String nullNotNull){
//        if (mVidyoConnector!=null) {
//            if (nullNotNull!=null) {
//                mVidyoConnector.selectLocalMicrophone(mLastSelectedMicrophone);
//                Log.d(TAG,"onLocalMicroPhoneSelected: LOCAL");
//            }
//            else{
//                mVidyoConnector.selectLocalMicrophone(null);
//            }
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorLM ","not connected");
//        }
//    }
//
//
//    public void selectLocalSpeaker(String nullNotNull){
//        if (mVidyoConnector!=null){
//            if (nullNotNull!=null){
//                mVidyoConnector.selectLocalSpeaker(mLastSelectedSpeaker);
//            }
//            else {
//                mVidyoConnector.selectLocalSpeaker(null);
//            }
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorLS ","not connected");
//        }
//    }
//
//    public void setMode(String mode){
//        if (mVidyoConnector!=null){
//            if (mode.equals("background")) {
//                mVidyoConnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Background);
//            }else if (mode.equals("foreground")){
//                mVidyoConnector.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Foreground);
//            }
////        Toast.makeText(context,"Mode "+mode,Toast.LENGTH_SHORT).show();
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorSM ","not connected");
//        }
//    }
//
//
//    public void selectDefaultSpeaker(){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.selectDefaultSpeaker();
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorDS ","not connected");
//        }
//    }
//
//    public void selectDefaultMicrophone(){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.selectDefaultMicrophone();
//            Log.d(TAG,"onLocalMicroPhoneSelected: DEFAULT " +mLastSelectedMicrophone);
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorDM ","not connected");
//        }
//    }
//
//    public void setCameraPrivacy(boolean mCameraPrivacy){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.setCameraPrivacy(mCameraPrivacy);
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorCV ","not connected");
//        }
//    }
//
//    public void setMicrophonePrivacy(boolean mMicrophonePrivacy){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.setMicrophonePrivacy(mMicrophonePrivacy);
//            Log.d("VidyoioConnectorMP ","connected");
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorMP ","not connected");
//        }
//    }
//
//    public void setSpeakerPrivacy(boolean mSpeakerPrivacy){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.setSpeakerPrivacy(mSpeakerPrivacy);
//            Log.d("VidyoioConnectorSP ","connected");
//        }else{
//            roomActivityListener.onError(errorMessage);
//            Log.d("VidyoioConnectorSP ","not connected");
//        }
//    }
//
//    public void onDestroySetSelectedCameraNull(){
//        mLastSelectedCamera = null;
//    }
//
//    public void setConnectorToNull(){
//        mVidyoConnector = null;
//    }
//
//
//
////    public void enableDebug(){
////      if (mVidyoConnector!=null) {
////          mVidyoConnector.enableDebug(7776, "warning info@VidyoClient info@VidyoConnector");
////      }else{
////
////          Log.d("VidyoioConnectorED ","not connected");
////      }
////    }
////
////    public void disableDebug(){
////        if (mVidyoConnector!=null) {
////            mVidyoConnector.disableDebug();
////        }else{
//////            Toast.makeText(context,"Vidyoio is not connected",Toast.LENGTH_SHORT).show();
////            Log.d("VidyoioConnectorDD ","not connected");
////        }
////    }
//
//    // ONSTOP ONRESTART ONDESTROY END.
//
//
//
//
////    Connect the call.
//
//    public boolean vidyoioCallConnect(String mHost, String mDisplayName,String roomKey, String roomName){
//
//        Log.d(TAG,"CALL CONNECTING ");
//        if (mVidyoConnector != null) {
//
////            roomActivityListener = listener;
//            connectedORNot = mVidyoConnector.connectToRoomAsGuest(
//                    mHost,
//                    mDisplayName,
//                    roomKey,
//                    roomName,
//                    this);
////            connectedORNot = mVidyoConnector.connect(
////                    mHost,
////                    roomKey,
////                    mDisplayName,
////                    resourceId,
////                    this);
//            Log.d(TAG,"CALL CONNECTED "+connectedORNot);
//
//        }
//        return connectedORNot;
//    }
//
//
//    public boolean connectToRoomAsGuest(String mPortal, String mDisplayName, String mRoomKey, String mRoomPin, RoomActivityListener listener){
//
//        if (mVidyoConnector!=null) {
//
//            roomActivityListener = listener;
//            connectedORNot = mVidyoConnector.connectToRoomAsGuest(
//                    mPortal,
//                    mDisplayName,
//                    mRoomKey,
//                    mRoomPin,
//                    this);
//            Log.d(TAG,"CALL CONNECTED "+connectedORNot);
//        }
//
//        return connectedORNot;
//    }
//
//    //    Disconnect the call
//    public void callDisconnect(){
//
//        mVidyoConnector.disconnect();
//
//    }
//
//
//    //    It returns status after call connected successfully.
//    public String getOnSuccess(){
//        return mStateDescription.get(mVidyoConnectorState);
//    }
//
//
//    // Cycle the camera.
//    public void cycleCamera(){
//        if (mVidyoConnector!=null) {
//            mVidyoConnector.cycleCamera();
//        }else{
//            Log.d(TAG,"VidyoConenctor is NULL");
//            roomActivityListener.onError("VidyoIo connector is null");
//        }
//    }
//
//
//    @Override
//    public void onVideoFrameClicked() {
//
//    }
//
//    @Override
//    public void onSuccess() {
//        Log.d(TAG,"onSuccess: successfully connected.");
////        setOnSuccess(VidyoConnectorState.Connected);
//        setEventListener();
//        mVidyoConnectorState = VidyoConnectorState.Connected;
//        roomActivityListener.onRoomConnected();
//    }
//
//    @Override
//    public void onFailure(Connector.ConnectorFailReason connectorFailReason) {
//        Log.d(TAG,"onFailure: connection attempt failed, reason = " + connectorFailReason.toString());
//        roomActivityListener.onRoomDisconnected(mStateDescription.get(VidyoConnectorState.Failure));
//    }
//
//    @Override
//    public void onDisconnected(Connector.ConnectorDisconnectReason reason) {
//        if (reason == Connector.ConnectorDisconnectReason.VIDYO_CONNECTORDISCONNECTREASON_Disconnected) {
//            Log.d(TAG,"onDisconnected: successfully disconnected, reason = " + reason.toString());
//            roomActivityListener.onRoomDisconnected(mStateDescription.get(VidyoConnectorState.Disconnected));
//        } else {
//            Log.d(TAG,"onDisconnected: unexpected disconnection, reason = " + reason.toString());
//            roomActivityListener.onRoomDisconnected(mStateDescription.get(VidyoConnectorState.DisconnectedUnexpected));
//        }
//    }
//
//    @Override
//    public void onLocalCameraAdded(LocalCamera localCamera) {
//        Log.d(TAG,"onLocalCameraAdded: " + localCamera.getName());
//    }
//
//    @Override
//    public void onLocalCameraRemoved(LocalCamera localCamera) {
//        Log.d(TAG,"onLocalCameraRemoved: " + localCamera.getName());
//    }
//
//    @Override
//    public void onLocalCameraSelected(LocalCamera localCamera) {
//        Log.d(TAG,"onLocalCameraSelected: " + (localCamera == null ? "none" : localCamera.getName()));
//        // If a camera is selected, then update mLastSelectedCamera.
//        if (localCamera != null) {
//            mLastSelectedCamera = localCamera;
//        }
//    }
//
//    @Override
//    public void onLocalCameraStateUpdated(LocalCamera localCamera, Device.DeviceState deviceState) {
//        Log.d(TAG,"onLocalCameraStateUpdated: name=" + localCamera.getName() + " state=" + deviceState);
//        if (deviceState.equals(Device.DeviceState.VIDYO_DEVICESTATE_Stopped)){
//            roomActivityListener.onVideoMuted();
//        }
//        else if (deviceState.equals(Device.DeviceState.VIDYO_DEVICESTATE_Started)){
//            roomActivityListener.onVideoUnMuted();
//        }
//    }
//
//    @Override
//    public void onLocalMicrophoneAdded(LocalMicrophone localMicrophone) {
//        Log.d(TAG,"onLocalMicroPhoneAdded: " + localMicrophone.getName());
//    }
//
//    @Override
//    public void onLocalMicrophoneRemoved(LocalMicrophone localMicrophone) {
//        Log.d(TAG,"onLocalMicroPhoneRemoved: " + localMicrophone.getName());
//    }
//
//    @Override
//    public void onLocalMicrophoneSelected(LocalMicrophone localMicrophone) {
//        Log.d(TAG,"onLocalMicroPhoneSelected: " + (localMicrophone == null ? "none" : localMicrophone.getName()));
//        if (localMicrophone!=null){
//            mLastSelectedMicrophone = localMicrophone;
//        }
//    }
//
//    @Override
//    public void onLocalMicrophoneStateUpdated(LocalMicrophone localMicrophone, Device.DeviceState deviceState) {
//        Log.d(TAG,"onLocalMicroPhoneUpdated: name=" + localMicrophone.getName() + " state=" + deviceState);
//        if (deviceState.equals(Device.DeviceState.VIDYO_DEVICESTATE_Stopped)){
//            roomActivityListener.onMicMuted();
//        }
//        else if (deviceState.equals(Device.DeviceState.VIDYO_DEVICESTATE_Started)){
//            roomActivityListener.onMicUnMuted();
//        }
//    }
//
//    @Override
//    public void onLocalSpeakerAdded(LocalSpeaker localSpeaker) {
//        Log.d(TAG,"onLocalSpeakerAdded: " + localSpeaker.getName());
//    }
//
//    @Override
//    public void onLocalSpeakerRemoved(LocalSpeaker localSpeaker) {
//        Log.d(TAG,"onLocalSpeakerRemoved: " + localSpeaker.getName());
//    }
//
//    @Override
//    public void onLocalSpeakerSelected(LocalSpeaker localSpeaker) {
//        Log.d(TAG,"onLocalSpeakerSelected: " + (localSpeaker == null ? "none" : localSpeaker.getName()));
//        mLastSelectedSpeaker = localSpeaker;
//    }
//
//    @Override
//    public void onLocalSpeakerStateUpdated(LocalSpeaker localSpeaker, Device.DeviceState deviceState) {
//        Log.d(TAG,"onLocalSpeakerUpdated: name=" + localSpeaker.getName() + " state=" + deviceState);
//        if (deviceState.equals(Device.DeviceState.VIDYO_DEVICESTATE_Stopped)){
//            roomActivityListener.onSpeakerMuted();
//        }
//        else if (deviceState.equals(Device.DeviceState.VIDYO_DEVICESTATE_Started)){
//            roomActivityListener.onSpeakerUnMuted();
//        }
//    }
//
//    @Override
//    public void onLog(LogRecord logRecord) {
//
//// No need to log to console here, since that is implicitly done when calling registerLogEventListener.
//
//    }
//
//    @Override
//    public void onNetworkInterfaceAdded(NetworkInterface vidyoNetworkInterface) {
//        Log.d(TAG,"onNetworkInterfaceAdded: name=" + vidyoNetworkInterface.getName() +
//                " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() +
//                " family=" + vidyoNetworkInterface.getFamily());
//    }
//
//    @Override
//    public void onNetworkInterfaceRemoved(NetworkInterface vidyoNetworkInterface) {
//        Log.d(TAG,"onNetworkInterfaceRemoved: name=" + vidyoNetworkInterface.getName() +
//                " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() +
//                " family=" + vidyoNetworkInterface.getFamily());
//    }
//
//    @Override
//    public void onNetworkInterfaceSelected(NetworkInterface vidyoNetworkInterface, NetworkInterface.NetworkInterfaceTransportType transportType) {
//        Log.d(TAG,"onNetworkInterfaceSelected: name=" + vidyoNetworkInterface.getName() +
//                " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() +
//                " family=" + vidyoNetworkInterface.getFamily());
//    }
//
//    @Override
//    public void onNetworkInterfaceStateUpdated(NetworkInterface vidyoNetworkInterface, NetworkInterface.NetworkInterfaceState vidyoNetworkInterfaceState) {
//        Log.d(TAG,"onNetworkInterfaceStateUpdated: name=" + vidyoNetworkInterface.getName() +
//                " address=" + vidyoNetworkInterface.getAddress() + " type=" + vidyoNetworkInterface.getType() +
//                " family=" + vidyoNetworkInterface.getFamily() + " state=" + vidyoNetworkInterfaceState);
//    }
//
//    @Override
//    public void onParticipantJoined(Participant participant) {
//        Log.e("OnParticipantJoined", "ParticipantName: " + participant.name + ", UserID: " + participant.userId);
//        roomActivityListener.onParticipantJoined(participant);
//    }
//
//    @Override
//    public void onParticipantLeft(Participant participant) {
//        Log.e("OnParticipantLeft", "ParticipantName: " + participant.name + ", UserID: " + participant.userId);
//        roomActivityListener.onParticipantLeft(participant);
//    }
//
//    @Override
//    public void onDynamicParticipantChanged(ArrayList<Participant> participants) {
//
//    }
//
//    @Override
//    public void onLoudestParticipantChanged(Participant participant, boolean audioOnly) {
//
//    }
//}
