package com.rank.kuber.Utils;

import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersId;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersName;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.rank.kuber.Activity.FeedbackActivity;
import com.rank.kuber.Common.AppData;

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
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage("No Internet Connection")
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(isOnline(context)){
                            dialogInterface.dismiss();
                        }else{
                            showDialog(context);
                        }
                    }
                }).create().show();
    }
}
