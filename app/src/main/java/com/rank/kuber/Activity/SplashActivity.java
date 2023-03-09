package com.rank.kuber.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.rank.kuber.Common.AppData;
import com.rank.kuber.R;


public class SplashActivity extends AppCompatActivity {


    public static final int PERMISSIONS_REQUEST_ALL = 100;

    private String[] mPermissions = {
            "android.permission.CAMERA"
            , "android.permission.WRITE_EXTERNAL_STORAGE"
            , "android.permission.ACCESS_FINE_LOCATION"
            , "android.permission.ACCESS_NETWORK_STATE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//      Checks if permission is already granted. If true, then move to GuestLoginActivity else request permission.
        if(checkPermission()){
            Toast.makeText(SplashActivity.this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
            mainFunction();
        }else {
            requestPermission();
        }

    }

    private void mainFunction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, GuestLoginActivity.class));
                finish();
            }
        }, 2000);
    }
    public boolean checkPermission() {

        int result1 = ContextCompat.checkSelfPermission(this, mPermissions[0]);
        int result2 = ContextCompat.checkSelfPermission(this, mPermissions[1]);
        int result3 = ContextCompat.checkSelfPermission(this, mPermissions[2]);
        int result4 = ContextCompat.checkSelfPermission(this, mPermissions[3]);
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;
    }


//    If camera permission was already not granted then show a dialog box for giving camera permission. Else request permissions.
    private void requestPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (shouldShowRequestPermissionRationale(mPermissions[0])) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("Camera permission is crucial for the functioning of app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{mPermissions[0], mPermissions[1] ,mPermissions[2]/*mPermissions[3]*/}, PERMISSIONS_REQUEST_ALL);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(SplashActivity.this, "Permission Denied, You cannot access all the facilities.", Toast.LENGTH_LONG).show();
                            }
                        }).create().show();
            } else {
                if (Build.VERSION.SDK_INT > 22) {
                    ActivityCompat.requestPermissions(this, new String[]{mPermissions[0], mPermissions[1] ,mPermissions[2]/*mPermissions[3]*/}, PERMISSIONS_REQUEST_ALL);
                }
            }
        }
    }

//    Move to GuestLoginActivity if only camera permission is granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        {

            if (requestCode == PERMISSIONS_REQUEST_ALL) {

                if(grantResults.length>0) {

                    boolean cameraPermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraPermissionAccepted) {
                        Toast.makeText(this, "Permission Granted for Camera, Now you can access all the facilities.", Toast.LENGTH_LONG).show();
                        mainFunction();
                    } else {
                        Toast.makeText(this, "Permission Denied, You cannot access all the facilities.", Toast.LENGTH_LONG).show();
                    }
                }
               /* if (Build.VERSION.SDK_INT > 22){
                    if (shouldShowRequestPermissionRationale(mPermissions[0]) ||
                            shouldShowRequestPermissionRationale(mPermissions[1]) /* ||
                            shouldShowRequestPermissionRationale(mPermissions[2]) ||
                            shouldShowRequestPermissionRationale(mPermissions[3])){
                        new AlertDialog.Builder(this)
                                .setTitle("Permission Needed")
                                .setMessage("This permission is crucial for the functioning of app")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                      requestPermission();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mainFunction();
                                    }
                                }).create().show();
                    }
                }*/
            } else {
                Toast.makeText(this, "permission deny", Toast.LENGTH_SHORT).show();
                Log.d(AppData.TAG,
                        "ERROR! Unexpected permission requested. Video will not be rendered."
                );
            }

        }
    }


}