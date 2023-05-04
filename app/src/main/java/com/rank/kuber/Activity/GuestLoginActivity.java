package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.EmptyRequest;
import com.rank.kuber.Model.RegisterRequest;
import com.rank.kuber.Model.RegisterResponse;
import com.rank.kuber.Model.ServiceModel;
import com.rank.kuber.R;
import com.rank.kuber.Utils.NetworkBroadcast;
import com.rank.kuber.socket.SocketClass;
import com.rank.kuber.socket.SocketParser;
import com.socket.SocketLibrary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestLoginActivity extends AppCompatActivity implements View.OnClickListener {

    List<ServiceModel.PayloadBean> serviceLists ;
    Handler handler;
    LinearLayout linearLayoutcontainer;
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
    View view1;
    RegisterRequest registerRequest;
    RegisterResponse registerResponse;
    String TAG = "GuestLoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_guest_login);
        AppData.currentContext = GuestLoginActivity.this;

        init();
        registerNetworkBroadcastReceiver();
        handler = new Handler();

//        not running the locationService() as it is not needed.
        //locationService();


        getServiceDetails();
        setClickListenerEvents();
        setTextChangeListener();


    }

    private void setTextChangeListener() {
        name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 name_edt.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

            email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email_edt.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mobile_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               mobile_edt.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nationality_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nationality_edt.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        name_edt.setText("");
        mobile_edt.setText("");
        nationality_edt.setText("");
        email_edt.setText("");
        service_dropdown.setText("Select the Service");

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
        linearLayoutcontainer.setVisibility(View.GONE);
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
                        linearLayoutcontainer.setVisibility(View.VISIBLE);
                        serviceLists = response.body().getPayload();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                loadingGuestLogin.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Dialog dialog= new Dialog(GuestLoginActivity.this);

                dialog.setContentView(R.layout.no_internet_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.show();
                TextView noServicetext=dialog.findViewById(R.id.no_internet_textView);
                noServicetext.setText("No Service List Available");
                Button tryagain_button= dialog.findViewById(R.id.tryagain_button);

                tryagain_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getServiceDetails();
                                dialog.dismiss();
                            }
                        }, 400);



                    }
                });

//                Toast.makeText(GuestLoginActivity.this, "No Service List Available", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {


        linearLayoutcontainer=(LinearLayout) findViewById(R.id.linear_layout_container);
        loadingGuestLogin= (ProgressBar)findViewById(R.id.loadingGuestLogin);
        terms_checkbox = (CheckBox) findViewById(R.id.terms_checkbox);
        view1= findViewById(R.id.view1);
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
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 400);
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
                name_edt.requestFocus();
                name_edt.setError("Please enter name.");
//                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            }
            else if(email.length()<1){
                email_edt.requestFocus();
                email_edt.setError("Please enter a email address.");
//                Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            }
            else if(!isValidEmail(email)){
                email_edt.requestFocus();
                email_edt.setError("Please enter a valid email address.");
//                Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            }
            else if(phone.length()<10){
                mobile_edt.requestFocus();
                mobile_edt.setError("Please enter a valid mobile no.");
//                Toast.makeText(this, "Please enter valid mobile no", Toast.LENGTH_SHORT).show();
            }
            else if(nationality.length()<1){
                nationality_edt.requestFocus();
                nationality_edt.setError("Please enter the nationality.");
//                Toast.makeText(this, "Please enter nationality", Toast.LENGTH_SHORT).show();
            }
            else if(service_dropdown.getText().toString().contains("Select the Service")){
                globalSnackbar("Please select the service.");
                view1.setBackgroundColor(getResources().getColor(R.color.colorRed));
//             Toast.makeText(this,"Please select the service.",Toast.LENGTH_SHORT).show();
            }
            else if(AppData.CallType.length()<1){
                globalSnackbar("Please select the call type.");
//                Toast.makeText(this,"Please select the call type.",Toast.LENGTH_SHORT).show();
            }
            else if(!isChecked){
                globalSnackbar("Please accept the Terms of Use.");
//                Toast.makeText(GuestLoginActivity.this, "Please accept the Terms of Use.", Toast.LENGTH_SHORT).show();
            }
            else if(selectedService.length()<1){
                globalSnackbar("Please select the service.");
//                Toast.makeText(this,"Please select the service.",Toast.LENGTH_SHORT).show();
            }

            else {

                AppData.name = name;
                AppData.email = email;
                AppData.phone = phone;
                AppData.nationality = nationality;
                AppData.selectedService = selectedService;

                loadingGuestLogin.setVisibility(View.VISIBLE);

                RegisterCustomer();




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
        popupWindow.setVerticalOffset(-10);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppData.selectedService= serviceLists.get(i).getServiceName();
                service_dropdown.setText(AppData.selectedService);
                service_dropdown.setTextColor(getResources().getColor(R.color.black));
                view1.setBackgroundColor(getResources().getColor(R.color.grey_project_color));
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                service_dropdown.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.selected_service_list,0);

            }
        });

        if(popupWindow.isShowing()){
            service_dropdown.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up_service_list,0);


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
        registerRequest.setCallMedium("Android");

        ApiClient.getApiClient().getregistercustomer(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    registerResponse=response.body();
                    Log.e(TAG,"Status :" + registerResponse.isStatus());
                    if(registerResponse.isStatus()){
                        Toast.makeText(GuestLoginActivity.this, "Guest Registered.", Toast.LENGTH_SHORT).show();
                        AppData.PROMOTIONAL_VIDEO = registerResponse.getPayload().getPromotionalVideo();
                        AppData.CustID = registerResponse.getPayload().getCustomerId(); //CustID is used as LoginID in calltoavailabeagent api.

                        AppData.CustName = registerResponse.getPayload().getCustFname();
                        AppData.CustFName = registerResponse.getPayload().getCustFname();
                        AppData.CustLName = registerResponse.getPayload().getCustLname();
                        AppData.SocketHostUrl = registerResponse.getPayload().getSocketHostPublic();
                        AppData.DISPLAY_NAME = AppData.CustFName+" "+AppData.CustLName;

                        createSocket();

                        Intent i = new Intent(getApplicationContext(), ShowGuestPromotionalVideoActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                loadingGuestLogin.setVisibility(View.GONE);
                Toast.makeText(GuestLoginActivity.this, "Cannot register the guest.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /* @param
     * @task Create Socket Connection
     */
    private void createSocket() {
        try {
            AppData.socketClass = new SocketClass();
            AppData.socketParser = new SocketParser();
            AppData.socketLibrary = new SocketLibrary();


//            String[] arrOfStr = AppData.SocketHostUrl.split(":", 2);
//            AppData.SOCKET_URL = arrOfStr[0];
//            AppData.SOCKET_PORT = arrOfStr[1];

            Log.e("Socket", "Socket URl :" + AppData.SOCKET_URL );
            Log.e("Socket", "Socket Port" + AppData.SOCKET_PORT );

            AppData.socketClass.setSocketUrl(AppData.SOCKET_URL);
            AppData.socketClass.setSocketPort(AppData.SOCKET_PORT);

            AppData.socketParser.createSocket();
            Log.e("Socket", "Socket Created");
        } catch (Exception e) {
            Log.e("CreateSocketException", "ExceptionCause: " + e.getMessage());
        }
    }
    public void globalSnackbar(String text) {
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            Snackbar mysnac= Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT);
            View sbview=mysnac.getView();
            sbview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            mysnac.show();
        }
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
