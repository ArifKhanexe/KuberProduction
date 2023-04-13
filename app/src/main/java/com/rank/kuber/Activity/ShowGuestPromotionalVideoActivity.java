package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.AgentRequest;
import com.rank.kuber.Model.AgentResponse;
import com.rank.kuber.Model.ChatModel;
import com.rank.kuber.Model.EmptyRequest;
import com.rank.kuber.Model.ServiceDownTimeResponse;
import com.rank.kuber.R;
import com.rank.kuber.Utils.NetworkBroadcast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGuestPromotionalVideoActivity extends AppCompatActivity implements View.OnClickListener{

    Handler handler;
    AgentRequest agentRequest;
    AgentResponse agentResponse;
    VideoView video_view;
    TextView waiting_status_tv,oops;
    Button cancel_btn, retry_btn;
    ProgressBar agentwaitprogressbar;
    String TAG = "ShowGuestPromotionalVideoActivity";
    BroadcastReceiver networkBroadcastReceiver;
    boolean isCancelledClick = false;
    int count = 1;
    boolean AgentStatus = false;
    public static String ADMIN = "supervisor";
    public static String ADMIN_NAME = "admin/supervisor";
    private boolean isAdminAvailable = false;


    public static ArrayList<ChatModel> al_chat_everyone; //Display chats related to EveryOne
    public static ArrayList<ChatModel> al_chat_specific_user; //Display chats related to Individual User

    public static int msgCounter = 0;
    public static boolean isEveryoneSelected = false;

    public static String previousId="";



    public static ArrayList<String> listOfIds;
    public static ArrayList<String> listOfUsersId ;
    public static ArrayList<String> listOfUsersName;

    public ChatMsgReceiver chatMsgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_guest_promotional_video);
        init();
        registerNetworkBroadcastReceiver();
        registerReceivers();

        video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
                video_view.start();
            }
        });


        agentwaitprogressbar.setVisibility(View.VISIBLE);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              getservicedowmtime();
            }
        }, 1000);

    }


    private void registerNetworkBroadcastReceiver() {
        networkBroadcastReceiver= new NetworkBroadcast();
        registerReceiver(networkBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
        unregisterReceivers();
    }

    public void registerReceivers() {
        try {
            registerReceiver(chatMsgReceiver, new IntentFilter(AppData._intentFilter_CHATMSG_RECEIVED));
        } catch (Exception e) {
            Log.e(AppData.TAG, "RegisterReceiverExceptionCause: " + e.getMessage());
        }
    }

        public void unregisterReceivers() {
        try {
            unregisterReceiver(chatMsgReceiver);
        } catch (Exception e) {
            Log.e("onDestroyUnRegisterRec", "ExceptionCause: " + e.getMessage());
        }
    }


    private void getservicedowmtime(){
        ApiClient.getApiClient().getservicedowntime(EmptyRequest.INSTANCE).enqueue(new Callback<ServiceDownTimeResponse>() {
            @Override
            public void onResponse(Call<ServiceDownTimeResponse> call, Response<ServiceDownTimeResponse> response) {
                if(response.isSuccessful()){
                    ServiceDownTimeResponse serviceDownTimeResponse= response.body();
                    if(serviceDownTimeResponse.isStatus()){
                        Toast.makeText(ShowGuestPromotionalVideoActivity.this,"Searching available agent",Toast.LENGTH_SHORT).show();
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                availableagent();
                            }
                        }, 5000);

                    }else{
                        agentwaitprogressbar.setVisibility(View.GONE);
                        waiting_status_tv.setText(serviceDownTimeResponse.getError().getErrorMessage());
                        oops.setVisibility(View.VISIBLE);
                        retry_btn.setVisibility(View.VISIBLE);
                        cancel_btn.setVisibility(View.VISIBLE);
                        cancel_btn.setText("Understood");
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceDownTimeResponse> call, Throwable t) {
                Toast.makeText(ShowGuestPromotionalVideoActivity.this,"Couldn't fetch details.",Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void availableagent() {
        agentRequest = new AgentRequest();
        agentRequest.setLoginId(AppData.CustID); //CustID from registercustomer is used as LoginId in calltoavailableagent api.
        agentRequest.setCategory(AppData.category);
        agentRequest.setLanguage(AppData.language);
        agentRequest.setService(AppData.selectedService);
        agentRequest.setCallOption(AppData.CallType);
        Log.e(TAG,"CALL OPTION :"+ AppData.CallType);
        if(AppData.location.isEmpty()) {
            agentRequest.setLocation("India");
        }else{
            agentRequest.setLocation(AppData.location);
        }
        if(AppData.Latitude.trim().isEmpty() || AppData.Longitude.trim().isEmpty() || AppData.Latitude == null || AppData.Longitude == null){
            agentRequest.setLatitude("88");
            agentRequest.setLongitude("22");
        }else{
            agentRequest.setLatitude(AppData.Latitude);
            agentRequest.setLongitude(AppData.Longitude);
        }

        Log.e(TAG,"Latitude : " + agentRequest.getLatitude() + " Longitude : " + agentRequest.getLongitude() + " Location : " + agentRequest.getLocation());

        ApiClient.getApiClient().getavailableagent(agentRequest).enqueue(new Callback<AgentResponse>() {
            @Override
            public void onResponse(Call<AgentResponse> call, Response<AgentResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG,"Response=> "+response.isSuccessful());
                    agentResponse = response.body();
                    if (agentResponse.isStatus()) {
                        Log.e(TAG,"StatusResponse=> "+response.isSuccessful());
                        if (!agentResponse.getPayload().getStatus().equals("Not Initiated")) {

//                            Saving Portal address and Roomkey from url.
                            String url = agentResponse.getPayload().getUrl();
                            String[] arrOfStr = url.split("/join/", 2);
                            AppData.Portal_Address = arrOfStr[0];
                            AppData.RoomKey = arrOfStr[1];
                            AppData.Call_ID = agentResponse.getPayload().getCallId();
                            AppData.Agent_id= agentResponse.getPayload().getAgentId();
                            AppData.SocketHostUrl = agentResponse.getPayload().getSocketHostPublic();
                            AppData.CustFName=agentResponse.getPayload().getCustFname();
                            AppData.CustLName=agentResponse.getPayload().getCustLname();
                            AppData.RoomName= agentResponse.getPayload().getRoomName();
                            AppData.Entity_id=agentResponse.getPayload().getEntityId();
                            AppData.Agent_login_id= agentResponse.getPayload().getAgentLoginId();



//                        Saving agent name from status
                            String fullStatus = agentResponse.getPayload().getStatus();
                            String[] arr = fullStatus.split("||", 2);
                            AppData.Agent_Name = arr[0];
                            AppData.Agent_Name = fullStatus.substring(11);

                            listOfUsersId.add(AppData.Agent_login_id);
                            listOfUsersName.add(AppData.Agent_Name);

                            Log.e(TAG,"RoomKey=> "+AppData.RoomKey);
                            Log.e(TAG,"Agent_id=> "+AppData.Agent_id);
                            Log.e(TAG,"Agent_Name=> "+AppData.Agent_Name);
                            Log.e(TAG,"Portal_Address=> "+AppData.Portal_Address);

                            agentwaitprogressbar.setVisibility(View.GONE);
//                        Showing agent name
                            waiting_status_tv.setText("You are going to meet "+AppData.Agent_Name);

//                           Move to conference activities when agent is available and calltype is video or audio. Else move to chat conference.
                            if(!AppData.CallType.equalsIgnoreCase("chat")){
                                Intent i = new Intent(getApplicationContext(), ConferenceActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(getApplicationContext(), EveryoneChatActivity.class);
                                startActivity(i);
                            }

                        } else{
                            agentwaitprogressbar.setVisibility(View.GONE);
                            waiting_status_tv.setText("All our agents are busy right now. Please try again later.");
                            retry_btn.setVisibility(View.VISIBLE);
                            cancel_btn.setVisibility(View.VISIBLE);
                            oops.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AgentResponse> call, Throwable t) {
                Toast.makeText(ShowGuestPromotionalVideoActivity.this, "Video Conference is not available.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void init(){


        if (al_chat_everyone == null) {
            al_chat_everyone = new ArrayList<>();
        }
        if (al_chat_specific_user == null) {
            al_chat_specific_user = new ArrayList<>();
        }
        if (listOfUsersId == null) {
            listOfUsersId = new ArrayList<>();
            listOfUsersId.add("Everyone");
       }

        if (listOfUsersName == null) {
            listOfUsersName = new ArrayList<>();
            listOfUsersName.add("All-users (group chat)");
        }

        /*Init Broadcast Receiver*/
        chatMsgReceiver = new ChatMsgReceiver();

        oops=(TextView) findViewById(R.id.oopsimage);
        video_view = (VideoView) findViewById(R.id.video_view);
        waiting_status_tv = (TextView) findViewById(R.id.waiting_status_tv);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        retry_btn = (Button) findViewById(R.id.retry_btn) ;
        agentwaitprogressbar= findViewById(R.id.agentwaitingprogressBar);

        retry_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        playPromotionalVideo(AppData.PROMOTIONAL_VIDEO);

        cancel_btn.setVisibility(View.GONE);
        retry_btn.setVisibility(View.GONE);
    }

    private void playPromotionalVideo(String videoURL) {
        if(!videoURL.isEmpty())
        {
            if(!video_view.isPlaying())
            {
                video_view.setVideoPath(videoURL);
                video_view.start();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view==retry_btn){
            waiting_status_tv.setText(R.string.waiting_status);
            agentwaitprogressbar.setVisibility(View.VISIBLE);
            cancel_btn.setVisibility(View.GONE);
            retry_btn.setVisibility(View.GONE);
            oops.setVisibility(View.GONE);

            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    availableagent();
                }
            }, 5000);
        }
        if(view==cancel_btn){
            Intent i = new Intent(getApplicationContext(), GuestLoginActivity.class);
            startActivity(i);
            finish();
        }
    }



    /**
     * Broadcast Receiver For Chat
     */
    private class ChatMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String msg = intent.getStringExtra("chatMsg");
                String senderId = intent.getStringExtra("senderId");
                String event = intent.getStringExtra("event");


//                if (senderId.contains(ADMIN)) {
//                    boolean isAdminContain = false;
//                    isAdminAvailable = true;
//
//                    /* THIS CHECKING IS DONE TO AVOID MULTIPLE ADMIN NAMES IN LIST */
//                    for (int i = 0; i < listOfUsersId.size(); i++) {
//                        if (listOfUsersId.get(i).contains(ADMIN)) {
//                            isAdminContain = true;
//                            break;
//                        }
//                    }
//
//                    if (!isAdminContain) {
//                        listOfUsersId.add(senderId);
//                        listOfUsersName.add(ADMIN_NAME);
//                    }
//                }


                Log.e("ChatMsgReceiver", "listOfUsersId: " + listOfUsersId.toString());

                ChatModel chatModel = new ChatModel();

                chatModel.setSenderId(senderId);
                chatModel.setMsg(msg);
                chatModel.setTime(new SimpleDateFormat("dd-MM-yyyy | hh:mm").format(new Date()));
                chatModel.setLeft(true);

                if (event.equalsIgnoreCase("private-chat")) {
                    chatModel.setEveryone(false);
                    isEveryoneSelected = false;
                } else {
                    chatModel.setEveryone(true);
                    isEveryoneSelected = true;
                }

                ShowGuestPromotionalVideoActivity.al_chat_everyone.add(chatModel);

                /* Inside ChatActivity or UserSpecificChatActivity i.e. the user is currently chatting */
                if (AppData.currentContext instanceof EveryoneChatActivity || AppData.currentContext instanceof ChatConferenceActivity) {

                    Log.e("ChatMsgReceiver", "inside ChatActivity");

                    /* Checking whether the user is currently chatting or not i.e. inside ViewSpecificChatListActivity */
                    if ((EveryoneChatActivity.selectedChatUserPos >= 0) && (AppData.currentContext instanceof ChatConferenceActivity)) {

                        Log.e("ChatMsgReceiver", "if part: ViewSpecificChatListActivity");
                        Log.e("ChatMsgReceiver", "SenderId: " + senderId + " selected id: " + ShowGuestPromotionalVideoActivity.listOfUsersId.get(EveryoneChatActivity.selectedChatUserPos)
                                + " pos: " + EveryoneChatActivity.selectedChatUserPos + " listOfUsersId: " + listOfUsersId);

                        /* Update the chat list during chatting */
                        if ((senderId.equalsIgnoreCase(listOfUsersId.get(EveryoneChatActivity.selectedChatUserPos)) && EveryoneChatActivity.selectedChatUserPos > 0 && !isEveryoneSelected)
                                || (!senderId.equalsIgnoreCase(listOfUsersId.get(EveryoneChatActivity.selectedChatUserPos)) && EveryoneChatActivity.selectedChatUserPos == 0 && isEveryoneSelected)) {

                            Log.e("ChatMsgReceiver", "inside if ");
                            ShowGuestPromotionalVideoActivity.al_chat_specific_user.add(chatModel);
                            intent = new Intent(AppData._intentFilter_INDIVIDUAL_CHATMSG);
                            AppData.currentContext.sendBroadcast(intent);
                        }
                        /* While chatting if any msg received from other user, show the toast */
                        else {
                            Log.e("ChatMsgReceiver", "else part");
                            increaseMsgCounter(senderId);

                            //   EveryoneChatActivity.receivedChatId = id;
                            if (isEveryoneSelected) {
                                EveryoneChatActivity.receivedChatId = "Everyone";
                                Toast.makeText(getApplicationContext(), "Chat message received from: " + senderId + " (in Group-chat)", Toast.LENGTH_LONG).show();
                            } else {
                                EveryoneChatActivity.receivedChatId = senderId;
                                Toast.makeText(getApplicationContext(), "Chat message received from: " + senderId, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        /* Inside EveryoneChatActivity, not in ChatShowGuestPromotionalVideoActivity */
                        Log.e("ChatMsgReceiver", "else part: ChatShowGuestPromotionalVideoActivity");

                        increaseMsgCounter(senderId);

                        if (isEveryoneSelected) {
                            EveryoneChatActivity.receivedChatId = "Everyone";
                        } else {
                            EveryoneChatActivity.receivedChatId = senderId;
                        }

                        intent = new Intent(ShowGuestPromotionalVideoActivity.this, EveryoneChatActivity.class);
                        startActivity(intent);
                    }

                } else {
                    /* Not in EveryoneChatActivity, just open the activity */
                    Log.e("ChatMsgReceiver", "not in EveryoneChatActivity");
                    increaseMsgCounter(senderId);

                    if (isEveryoneSelected) {
                        EveryoneChatActivity.receivedChatId = "Everyone";
                    } else {
                        EveryoneChatActivity.receivedChatId = senderId;
                    }

                    intent = new Intent(ShowGuestPromotionalVideoActivity.this, EveryoneChatActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Log.e("ChatMsgReceiver", "ExceptionCause: " + e.getMessage());
            }
        }
    }


    private void increaseMsgCounter(String id) {

        /* Since id of the user sent from Everyone or Privately is same */
        if (isEveryoneSelected) {
            id = "Everyone";
        } else {
            //do nothing
        }

        /* Empty means, first time when anyone sends chat */
        if (previousId.isEmpty() || previousId.equalsIgnoreCase(id)) {
            msgCounter++;
            previousId = id;
            Log.e("increaseMsgCounter", "msgCounter: " + msgCounter + ", previousId: " + previousId);
        }

        /* Refresh the counter, otherwise it will show the unread msg count of other users also */
        else {
            msgCounter = 0;
            msgCounter++;
            previousId = id;
            Log.e("increaseMsgCounter", "msgCounter: " + msgCounter + ", previousId: " + previousId);
        }
    }


}