package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.AgentRequest;
import com.rank.kuber.Model.AgentResponse;
import com.rank.kuber.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGuestPromotionalVideoActivity extends AppCompatActivity implements View.OnClickListener{

    Handler handler;
    AgentRequest agentRequest;
    AgentResponse agentResponse;
    VideoView video_view;
    TextView waiting_status_tv;
    Button cancel_btn, retry_btn;
    ProgressBar agentwaitprogressbar;
    String TAG = "ShowGuestPromotionalVideoActivity";
    boolean isCancelledClick = false;
    int count = 1;
    boolean AgentStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_guest_promotional_video);
        init();

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
                    availableagent();
                }
            }, 6000);
        }

        @Override
        protected void onStart() {
        super.onStart();

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

//                        Saving agent name from status
                            String fullStatus = agentResponse.getPayload().getStatus();
                            String[] arr = fullStatus.split("||", 2);
                            AppData.Agent_Name = arr[0];
                            AppData.Agent_Name = fullStatus.substring(11);

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
                                Intent i = new Intent(getApplicationContext(), ChatConferenceActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else{
                            agentwaitprogressbar.setVisibility(View.GONE);
                            waiting_status_tv.setText("Sorry, no Agent is available right now. Please try again later.");
                            retry_btn.setVisibility(View.VISIBLE);
                            cancel_btn.setVisibility(View.VISIBLE);
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
            availableagent();
        }
        if(view==cancel_btn){
            Intent i = new Intent(getApplicationContext(), GuestLoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}