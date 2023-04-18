package com.rank.kuber.Utils;

import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersId;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersName;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rank.kuber.Activity.FeedbackActivity;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.R;

public class NetworkBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (!isOnline(context)) {

            showDialog(context);

            Toast.makeText(context, "No Internet Connection. Try Again.", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isNetworkConnected(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isOnline(Context context){
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] allNetworks = connectivityManager.getAllNetworks(); // added in API 21 (Lollipop)

        for (Network network : allNetworks) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    isConnected = true;
            }
        }
        return isConnected;
    }

    public static void showDialog(Context context){
        Handler handler;
        handler = new Handler();

        Dialog dialog= new Dialog(context);

        dialog.setContentView(R.layout.no_internet_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        Button tryagain_button= dialog.findViewById(R.id.tryagain_button);

        tryagain_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOnline(context)){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 400);
                }else{
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showDialog(context);
                        }
                    }, 400);

                }

            }
        });
//
//        new AlertDialog.Builder(context)
//                .setTitle("Alert")
//                .setMessage("No Internet Connection")
//                .setCancelable(false)
//                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if(isOnline(context)){
//                            dialogInterface.dismiss();
//                        }else{
//                            showDialog(context);
//                        }
//                    }
//                }).create().show();
    }
}
