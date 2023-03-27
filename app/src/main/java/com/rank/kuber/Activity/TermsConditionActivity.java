package com.rank.kuber.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.Virtualizer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.RegisterRequest;
import com.rank.kuber.Model.RegisterResponse;
import com.rank.kuber.R;
import com.rank.kuber.Utils.NetworkBroadcast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsConditionActivity extends AppCompatActivity implements View.OnClickListener {

    BroadcastReceiver networkBroadcastReceiver;
    RegisterRequest registerRequest;
    RegisterResponse registerResponse;
    CheckBox terms_checkbox;
    Button accept_btn;
    String TAG = "TermsConditionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        init();
        registerBroadcastReceiver();
        setClickListenerEvents();

    }
//         Registering broadcast receiver for runtime network checking
    private void registerBroadcastReceiver() {
        networkBroadcastReceiver= new NetworkBroadcast();
        registerReceiver(networkBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
    }

    public void init(){
        terms_checkbox = (CheckBox) findViewById(R.id.terms_checkbox);
        accept_btn = (Button) findViewById(R.id.accept_btn);
    }

   private void setClickListenerEvents(){
        accept_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==accept_btn){
//        Call Registercustomer() only when the term_checkbox is checked.
            boolean isChecked =   terms_checkbox.isChecked();
            if(isChecked){
                RegisterCustomer();
            } else {
                Toast.makeText(TermsConditionActivity.this, "Please accept Terms of Use", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    Registering the customer.
    private void RegisterCustomer() {
        registerRequest = new RegisterRequest();
        registerRequest.setCategory(AppData.category);
        registerRequest.setLanguage(AppData.language);
        registerRequest.setService(AppData.selectedService);
        registerRequest.setCustomerName(AppData.name);
        registerRequest.setEmail(AppData.email);
        registerRequest.setCellPhone(AppData.phone);
        registerRequest.setNationality(AppData.nationality);

        ApiClient.getApiClient().getregistercustomer(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
               if(response.isSuccessful()){
                   registerResponse=response.body();
                   Log.e(TAG,"Status :" + registerResponse.isStatus());
                   if(registerResponse.isStatus()){
                       Toast.makeText(TermsConditionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                       AppData.PROMOTIONAL_VIDEO = registerResponse.getPayload().getPromotionalVideo();
                       AppData.CustID = registerResponse.getPayload().getCustomerId(); //CustID is used as LoginID in calltoavailabeagent api.

                       AppData.CustName = registerResponse.getPayload().getCustFname();
                       AppData.CustFName = registerResponse.getPayload().getCustFname();
                       AppData.CustLName = registerResponse.getPayload().getCustLname();
                       AppData.SocketHostUrl = registerResponse.getPayload().getSocketHostPublic();
                       AppData.DISPLAY_NAME = AppData.CustFName+" "+AppData.CustLName;

                       Intent i = new Intent(getApplicationContext(), ShowGuestPromotionalVideoActivity.class);
                       startActivity(i);
                       finish();
                   }
               }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(TermsConditionActivity.this, "Cannot register the customer", Toast.LENGTH_SHORT).show();
            }
        });
    }

}