package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.EmptyRequest;
import com.rank.kuber.Model.GetFeedbackResponse;
import com.rank.kuber.Model.SaveFeedbackRequest;
import com.rank.kuber.Model.SaveFeedbackResponse;
import com.rank.kuber.R;
import com.rank.kuber.Utils.NetworkBroadcast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    GetFeedbackResponse getFeedbackResponse;

    BroadcastReceiver networkBroadcastReceiver;
    SaveFeedbackRequest saveFeedbackRequest;
    SaveFeedbackResponse saveFeedbackResponse;
    TextView test,query,satisfaction;
    RatingBar testRB, queryRB, satisfactionRB;
    EditText feedbacktext;
    RelativeLayout relativeLayout;
    Button submit, skip;
    String testRating, queryRating, satisfactionRating,comment;
    ProgressBar loadingfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
        registerNetworkBroadcastReceiver();
        getfeedback();
    }

    private void registerNetworkBroadcastReceiver() {
        networkBroadcastReceiver= new NetworkBroadcast();
        registerReceiver(networkBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
    }

    private void init(){

        relativeLayout=findViewById(R.id.RelativeLayoutContainerFeedback);
        test= findViewById(R.id.texttest);
        query=findViewById(R.id.textquery);
        satisfaction=findViewById(R.id.textsatisfaction);

        testRB=findViewById(R.id.testratingBar);
        queryRB=findViewById(R.id.queryratingBar);
        satisfactionRB= findViewById(R.id.satisfactionratingBar);

        loadingfeedback=findViewById(R.id.loadingFeedback);

        feedbacktext=findViewById(R.id.Comment);
        submit=findViewById(R.id.submitbutton);
        skip=findViewById(R.id.skipbutton);
        submit.setOnClickListener(this);
        skip.setOnClickListener(this);

    }

    private void getfeedback() {
        loadingfeedback.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ApiClient.getApiClient().getfeedback(EmptyRequest.INSTANCE).enqueue(new Callback<GetFeedbackResponse>() {
            @Override
            public void onResponse(Call<GetFeedbackResponse> call, Response<GetFeedbackResponse> response) {
                 if(response.isSuccessful()){
                     try {
                         loadingfeedback.setVisibility(View.GONE);
                         relativeLayout.setVisibility(View.VISIBLE);
                         getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                         getFeedbackResponse = response.body();
                         test.setText(getFeedbackResponse.getPayload().getQuestionOne());
                         satisfaction.setText(getFeedbackResponse.getPayload().getQuestionTwo());
                         query.setText(getFeedbackResponse.getPayload().getQuestionThree());

                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
            }

            @Override
            public void onFailure(Call<GetFeedbackResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to load feedback page.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FeedbackActivity.this, GuestLoginActivity.class);
                startActivity(i);
              
            }
        });
    }

    private void savefeedback() {
//        testRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                testRating= (ratingBar.getRating());
//            }
//        });

        testRating=String.valueOf(testRB.getRating());

//        satisfactionRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                satisfactionRating= String.valueOf(ratingBar.getRating());
//            }
//        });

        satisfactionRating= String.valueOf(satisfactionRB.getRating());

//        queryRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                queryRating= String.valueOf(ratingBar.getRating());
//            }
//        });

        queryRating= String.valueOf(queryRB.getRating());

        comment= feedbacktext.getText().toString().trim();

        saveFeedbackRequest=new SaveFeedbackRequest();
        saveFeedbackRequest.setCallId(AppData.Call_ID);
        saveFeedbackRequest.setCust_id(AppData.CustID);
        saveFeedbackRequest.setQuestion1Val(testRating);
        saveFeedbackRequest.setQuestion2Val(satisfactionRating);
        saveFeedbackRequest.setQuestion3Val(queryRating);

        ApiClient.getApiClient().getsavefeedback(saveFeedbackRequest).enqueue(new Callback<SaveFeedbackResponse>() {
            @Override
            public void onResponse(Call<SaveFeedbackResponse> call, Response<SaveFeedbackResponse> response) {
                saveFeedbackResponse= response.body();
                if(saveFeedbackResponse.getPayload().getResMessage().equalsIgnoreCase("SUCCESS")){
                    Toast.makeText(FeedbackActivity.this, "Feedback Saved", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(FeedbackActivity.this, GuestLoginActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<SaveFeedbackResponse> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        if(view==submit){
            savefeedback();
        }
        if(view==skip){
            Intent i = new Intent(FeedbackActivity.this, GuestLoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}