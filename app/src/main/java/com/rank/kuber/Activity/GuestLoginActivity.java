package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.EmptyRequest;
import com.rank.kuber.Model.ServiceModel;
import com.rank.kuber.R;
import com.rank.kuber.Utils.NetworkBroadcast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestLoginActivity extends AppCompatActivity implements View.OnClickListener {

    List<ServiceModel.PayloadBean> serviceLists ;

    Handler handler;
    BroadcastReceiver networkBroadcastReceiver;
    ServiceAdapter serviceAdapter=null;
    LocationManager locationManager;
    RelativeLayout layoutcontainer;
    CheckBox terms_checkbox;
    ProgressBar loadingGuestLogin;
    FusedLocationProviderClient fusedLocationClient;
    EditText name_edt, email_edt,mobile_edt, nationality_edt;
    TextView service_dropdown, term_of_use;
    RadioGroup call_type_radioGroup;
    RadioButton videoradioButton, audiooradioButton, chatoradioButton;
    Button login_btn;
    String TAG = "GuestLoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        init();
        registerNetworkBroadcastReceiver();
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationService();
            }
        }, 3000);

        getServiceDetails();
        setClickListenerEvents();

    }

    //     Registering broadcast receiver for runtime network checking
    private void registerNetworkBroadcastReceiver() {
        networkBroadcastReceiver= new NetworkBroadcast();
        registerReceiver(networkBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
    }

    private void locationService() {
      /*  AppData.Longitude = "0.0";
        AppData.Latitude = "0.0" ;*/
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
//              Store the value of Longitude and Latitude in Appdata. For further use.
                        if (location != null) {
                            AppData.Longitude = String.valueOf(location.getLongitude());
                            AppData.Latitude = String.valueOf(location.getLatitude());
//              Using Longitude and Latitude to get City name
                            try {
                                Geocoder geocoder;
                                geocoder = new Geocoder(GuestLoginActivity.this);
                                List<Address> addresses = geocoder.getFromLocation(Double.valueOf(AppData.Latitude), Double.valueOf(AppData.Longitude), 1);
                                AppData.location = addresses.get(0).getLocality();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "Longitude :" + AppData.Longitude);
                            Log.e(TAG, "Latitude :" + AppData.Latitude);
                            Log.e(TAG, "Location :" + AppData.location);
                        }
                    }
                });
            } /*else {
                AppData.Longitude = "0.0";
                AppData.Latitude = "0.0" ;
                Log.e(TAG, "Longitude :" + AppData.Longitude + " (When no location permission is given)");
                Log.e(TAG, "Latitude :" + AppData.Latitude + " (When no location permission is given)");
            }
        }else{
            AppData.Longitude="0.0";
            AppData.Latitude="0.0";
            Log.e(TAG, "Longitude :" + AppData.Longitude + " (When location is Switched off)");
            Log.e(TAG, "Latitude :" + AppData.Latitude + " (When location is Switched off)");
        }*/

        }
    }

//      To get the servicelist details from servicelist API
    private void getServiceDetails() {
        loadingGuestLogin.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ApiClient.getApiClient().getservice(EmptyRequest.INSTANCE).enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                ServiceModel serviceModel = response.body();
                if (response.isSuccessful()) {
                    if (serviceModel.isStatus()) {
                        loadingGuestLogin.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        serviceLists = response.body().getPayload();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Toast.makeText(GuestLoginActivity.this, "No Service Available", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {

        layoutcontainer= (RelativeLayout) findViewById(R.id.RelativeLayoutContainer);
        loadingGuestLogin= (ProgressBar)findViewById(R.id.loadingGuestLogin);
        terms_checkbox = (CheckBox) findViewById(R.id.terms_checkbox);
        name_edt = (EditText) findViewById(R.id.name_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        mobile_edt = (EditText) findViewById(R.id.mobile_edt);
        nationality_edt = (EditText) findViewById(R.id.nationality_edt);
        service_dropdown=(TextView) findViewById(R.id.service_dropdown_textview);
        term_of_use=(TextView) findViewById(R.id.termsText);
        call_type_radioGroup = (RadioGroup) findViewById(R.id.call_type_radioGroup);
        videoradioButton = (RadioButton) findViewById(R.id.videoradioButton);
        audiooradioButton = (RadioButton) findViewById(R.id.audiooradioButton);
        chatoradioButton = (RadioButton) findViewById(R.id.chatoradioButton);
        login_btn = (Button) findViewById(R.id.login_btn);

        call_type_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.audiooradioButton:
                        AppData.CallType = "audio";
                        break;
                    case R.id.videoradioButton:
                        AppData.CallType = "video";
                        break;
                    case R.id.chatoradioButton:
                        AppData.CallType = "chat";
                        break;
                }
            }
        });
    }

    private void setClickListenerEvents(){

        service_dropdown.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        term_of_use.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){

        if(view==service_dropdown){
            dropdown(service_dropdown);
        }
        if(view==term_of_use){
//            Opens the terms and condiotion dialog box
            term_of_use.setTextColor(getResources().getColor(R.color.primary_project_color));
            Dialog dialog= new Dialog(this);

            dialog.setContentView(R.layout.terms_dialog_box);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            Button accepted_button= dialog.findViewById(R.id.accept_btn);
            TextView hyperlink = dialog.findViewById(R.id.hyperlink);
            hyperlink.setMovementMethod(LinkMovementMethod.getInstance());
            accepted_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    terms_checkbox.setChecked(true);
                    dialog.dismiss();
                }
            });
        }

//        Checks validity of the data entered by the user then saving it in Appdata for further usage. Moving to TermsConditionActivity.
        if(view==login_btn){
            String name = name_edt.getText().toString().trim();
            String phone = mobile_edt.getText().toString().trim();
            String email = email_edt.getText().toString().trim();
            String nationality = nationality_edt.getText().toString().trim();
            final String selectedService= AppData.selectedService;
            boolean isChecked =   terms_checkbox.isChecked();

            if(name.length()<1){
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            }
            else if(email.length()<1){
                Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            }
            else if(!isValidEmail(email)){
                Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            }
            else if(phone.length()<10){
                Toast.makeText(this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
            }
            else if(nationality.length()<1){
                Toast.makeText(this, "Please enter nationality", Toast.LENGTH_SHORT).show();
            }
            else if(selectedService.length()<1){
                Toast.makeText(this,"Please select service",Toast.LENGTH_SHORT).show();
            }
            else if(AppData.CallType.length()<1){
                Toast.makeText(this,"Please select call type",Toast.LENGTH_SHORT).show();
            }
            else if(!isChecked){
                Toast.makeText(GuestLoginActivity.this, "Please accept Terms of Use", Toast.LENGTH_SHORT).show();
            }
            else {

                AppData.name = name;
                AppData.email = email;
                AppData.phone = phone;
                AppData.nationality = nationality;
                AppData.selectedService = selectedService;

                Intent i = new Intent(GuestLoginActivity.this, TermsConditionActivity.class);
                startActivity(i);


                name_edt.setText("");
                mobile_edt.setText("");
                nationality_edt.setText("");
                email_edt.setText("");
                service_dropdown.setText("Select the Service");

            }

        }

    }
//    Set selected servicelist on the TextView
    private void dropdown(TextView service_dropdown) {
        if(serviceLists!=null && serviceLists.size()>0){
            serviceAdapter=new ServiceAdapter(this,serviceLists);
        }else{
            Toast.makeText(this, "Null List", Toast.LENGTH_SHORT).show();
        }

        final ListPopupWindow popupWindow= new ListPopupWindow(GuestLoginActivity.this);
        popupWindow.setAnchorView(service_dropdown);
        popupWindow.setWidth(service_dropdown.getWidth());
        popupWindow.setModal(false);
        popupWindow.setHeight(popupWindow.WRAP_CONTENT);
        popupWindow.setAdapter(serviceAdapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppData.selectedService= serviceLists.get(i).getServiceName();
                service_dropdown.setText(AppData.selectedService);

                popupWindow.dismiss();
            }
        });
        popupWindow.show();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                service_dropdown.setBackground(getResources().getDrawable(R.drawable.selected_service_list));
            }
        });

        if(popupWindow.isShowing()){
            service_dropdown.setBackground(getResources().getDrawable(R.drawable.up_service_list));
        }
    }

//    Service list Adapter for setting for the value of servicelist.
    public class ServiceAdapter extends BaseAdapter {

        private Context context;
        private List<ServiceModel.PayloadBean> slist;

        public ServiceAdapter(Context context, List<ServiceModel.PayloadBean> slist) {
            this.context = context;
            this.slist = slist;
        }

        @Override
        public int getCount() {
            return slist.size();
        }

        @Override
        public Object getItem(int i) {
            return slist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.service_list,viewGroup,false);
            }

            TextView textViewItemview= (TextView) view.findViewById(R.id.service_list_name);
            View lineview= (View) view.findViewById(R.id.view1);
            textViewItemview.setText(slist.get(i).getServiceName());
            return view;

        }
    }

//    Checking if email is valid or not.
    public final boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}



  /*  private void showAlertMessageLocationDisabled() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Device Location is turned off, Do you want to turn on the Location ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }*/
