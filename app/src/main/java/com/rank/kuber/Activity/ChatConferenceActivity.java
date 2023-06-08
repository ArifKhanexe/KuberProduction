package com.rank.kuber.Activity;

import static com.rank.kuber.Activity.EveryoneChatActivity.selectedChatUserPos;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.al_chat_everyone;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.al_chat_specific_user;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersId;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersName;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.ChatModel;
import com.rank.kuber.Model.GetEmployeesRequest;
import com.rank.kuber.Model.GetEmployeesResponse;
import com.rank.kuber.Model.HangUpCustomerRequest;
import com.rank.kuber.Model.HangUpCustomerResponse;
import com.rank.kuber.Model.UploadResponse;
import com.rank.kuber.Model.conferenceUsersDtoList;
import com.rank.kuber.R;
import com.rank.kuber.Utils.ChatAdapter;
import com.rank.kuber.Utils.NetworkBroadcast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatConferenceActivity extends AppCompatActivity implements View.OnClickListener {


    private ListView lv_userSpecificChat;
    private EditText et_writeChatMsg;
    private ImageView iv_sendChat, iv_backbutton, iv_upload, iv_chatend;
    TextView chatusername;
    private ChatAdapter chatAdapter;
    private DownloadFileReceiver downloadFileReceiver;
    private ChatReceivedUserReceiver chatReceivedUserReceiver;
    HangUpCustomerRequest hangUpCustomerRequest;
    BroadcastReceiver networkBroadcastReceiver;
    private static final int FILE_UPLOAD_REQUEST_CODE =4556 ;
    String imageFilePath="";
    public List<conferenceUsersDtoList> employeeLists;
    static boolean activeChat = false;
    private List<MultipartBody.Part> multipartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conference);
        activeChat = true;
        AppData.currentContext = ChatConferenceActivity.this;
        AppData.TAG = "UserSpecificChatActivity";
        registerNetworkBroadcastReceiver();

        /*Call Required Functions*/
        getUIComponents();
        setClickListenerEvents();

        /*Initialize Objects*/
        initObjects();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    //     Registering broadcast receiver for runtime network checking
    private void registerNetworkBroadcastReceiver() {
        networkBroadcastReceiver= new NetworkBroadcast();
        downloadFileReceiver = new DownloadFileReceiver();
        registerReceiver(networkBroadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(downloadFileReceiver, new IntentFilter(AppData._intentFilter_FILERECEIVED));

    }
    @Override
    protected void onResume() {
        super.onResume();

        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(chatReceivedUserReceiver);
        unregisterReceiver(networkBroadcastReceiver);
        unregisterReceiver(downloadFileReceiver);
        activeChat = false;
    }

    public static ChatConferenceActivity getInstance() {
        return (ChatConferenceActivity) AppData.currentContext;
    }

    private void getUIComponents() {
        lv_userSpecificChat = (ListView) findViewById(R.id.lv_userSpecificChat);
        et_writeChatMsg = (EditText) findViewById(R.id.et_writeChatMsg);
        iv_sendChat = (ImageView) findViewById(R.id.iv_sendChat);
        iv_backbutton = findViewById(R.id.chat_back_img2);
        iv_chatend=findViewById(R.id.chatend);
        iv_upload=findViewById(R.id.upload_chat);
        chatusername=findViewById(R.id.chat_username_title);
        chatusername.setText(ShowGuestPromotionalVideoActivity.listOfUsersName.get(selectedChatUserPos));

        if(!AppData.CallType.equalsIgnoreCase("chat")){
            iv_upload.setVisibility(View.GONE);
            iv_chatend.setVisibility(View.GONE);
        }
    }
    private void initObjects() {
        chatAdapter = new ChatAdapter(ChatConferenceActivity.this);

        if (null == chatReceivedUserReceiver) {
            chatReceivedUserReceiver = new ChatReceivedUserReceiver();
        }

        registerReceiver(chatReceivedUserReceiver, new IntentFilter(AppData._intentFilter_INDIVIDUAL_CHATMSG));

        lv_userSpecificChat.setAdapter(chatAdapter);


    }

    private void setClickListenerEvents() {
        iv_sendChat.setOnClickListener(this);
        iv_backbutton.setOnClickListener(this);
        iv_chatend.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == iv_sendChat) {
            if (et_writeChatMsg.getText().length() > 0) {

                ChatModel chatModel = new ChatModel();
                chatModel.setMsg(et_writeChatMsg.getText().toString());
                chatModel.setTime(new SimpleDateFormat("dd-MM-yyyy | hh:mm").format(new Date()));
                chatModel.setLeft(false);
                chatModel.setSenderId(AppData.CustName);
                chatModel.setEveryone(ShowGuestPromotionalVideoActivity.isEveryoneSelected);


                ShowGuestPromotionalVideoActivity.al_chat_everyone.add(chatModel);
                ShowGuestPromotionalVideoActivity.al_chat_specific_user.add(chatModel);

                if (!ShowGuestPromotionalVideoActivity.isEveryoneSelected) {
                    Log.e("UserSpecificChatAct", "Chat Send To Specific User");
                    AppData.socketClass.sendPrivateChat(ShowGuestPromotionalVideoActivity.listOfUsersId.get(selectedChatUserPos), et_writeChatMsg.getText().toString());
                } else {
                    Log.e("UserSpecificChatAct", "ListUserId "+ShowGuestPromotionalVideoActivity.listOfUsersId);
                    Log.e("UserSpecificChatAct", "Chat Send To Everyone");
                    AppData.socketClass.sendGroupChat(ShowGuestPromotionalVideoActivity.listOfUsersId, et_writeChatMsg.getText().toString());
                }

                et_writeChatMsg.setText("");
                chatAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Field is blank, can't send blank chat", Toast.LENGTH_LONG).show();
            }
        }
        if (view == iv_backbutton){
            onBackPressed();
        }
        if(view == iv_chatend){
            showAlertDialogOnBackPressed();
        }
        if(view == iv_upload){

            Dialog dialog= new Dialog(ChatConferenceActivity.this);

            dialog.setContentView(R.layout.upload_dialogbox);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.show();

            Button image_upload = dialog.findViewById(R.id.images_button);
            Button document_upload= dialog.findViewById(R.id.document_button);
            Button close=dialog.findViewById(R.id.close_button);
            image_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                    dialog.dismiss();
                }
            });
            document_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                    dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void showFileChooser() {

        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);//TODO New Changes here 18-11-2021
        // The MIME data type filter
        intent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.choose_file));

        try {
            startActivityForResult(chooserIntent, FILE_UPLOAD_REQUEST_CODE);
//            someActivityResultLauncher.launch(chooserIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FILE_UPLOAD_REQUEST_CODE:
                //If the file selection was successful
                if ((resultCode == RESULT_OK) && (data != null)) {
                    final Uri uri = data.getData();
                    try {

                        String fileName = "Test";
                        imageFilePath=copyFileToInternal(this,uri);
                        final File file = new File(imageFilePath);
                        uploadFileDuringCall(file,uri,fileName);

                    } catch (Exception e) {
                        Log.e("patFileUploadDuringCl", "ExceptionCause: " + e.getMessage());
                        Toast.makeText(this, "Please select a proper file manager to select file", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public static String copyFileToInternal(Context context, Uri fileUri) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor cursor = context.getContentResolver().query(fileUri, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE}, null, null);
            cursor.moveToFirst();

            @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            @SuppressLint("Range") long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));

            File file = new File(context.getFilesDir() + "/" + displayName);
            double lengthInBytes = file.length();
            double length = lengthInBytes / (1024 * 1024);
            if (length > 5.0) {
                Toast.makeText(context.getApplicationContext(), "File size should be less than 5 MB", Toast.LENGTH_LONG).show();
            } else {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                    byte buffers[] = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffers)) != -1) {
                        fileOutputStream.write(buffers, 0, read);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                    return file.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    private void uploadFileDuringCall(File file,Uri uri, String file_name) {
//        final File file = new File(filePath);
        double lengthInBytes = file.length();
        double length = lengthInBytes / (1024 * 1024);
        if (length <= 5.0) {
//            if (file.exists()) {
//                String image_name = file.getAbsolutePath();
            String image_name = file_name;
            getincallemployees(file,image_name,uri);



//            }else{
//                Toast.makeText(ConferenceActivity.this, "File doesn't exist", Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(this, "File size should be less than 5 MB", Toast.LENGTH_LONG).show();
        }
    }



    private void getincallemployees(File uploadFile, String image_name, Uri uri) {
        GetEmployeesRequest getEmployeesRequest= new GetEmployeesRequest();
        getEmployeesRequest.setCallId(AppData.Call_ID);

        ApiClient.getApiClient().getincallemployees(getEmployeesRequest).enqueue(new Callback<GetEmployeesResponse>() {
            @Override
            public void onResponse(Call<GetEmployeesResponse> call, Response<GetEmployeesResponse> response) {
                if(response.isSuccessful()) {
                    GetEmployeesResponse getEmployeesResponse = response.body();
                    if(getEmployeesResponse.isStatus()){
                        List<GetEmployeesResponse.PayloadBean> payloadBeanList= response.body().getPayload();
                        employeeLists = new ArrayList<>();
                        for(GetEmployeesResponse.PayloadBean a:payloadBeanList){
                            conferenceUsersDtoList employeeList = new conferenceUsersDtoList();
                            employeeList.setId(a.getId());
                            employeeList.setLoginId(a.getLoginId());
                            employeeLists.add(employeeList);
                        }



                        fileUploadDuringCallService(uploadFile,image_name,uri);

//                        Toast.makeText(ChatConferenceActivity.this, employeeLists.get(0).getId() + employeeLists.get(0).getLoginId(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChatConferenceActivity.this, "Error Message : " + getEmployeesResponse.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChatConferenceActivity.this, "Couldn't fetch employee details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetEmployeesResponse> call, Throwable t) {
                Toast.makeText(ChatConferenceActivity.this, "Cannot upload due to server issue.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fileUploadDuringCallService(File uploadFile, String image_name, Uri uri) {

        Log.e("Conference", "Outside " );
        if (multipartList!=null && multipartList.size()>0){
            multipartList.clear();
        }
        final Long fileSize = uploadFile.length();
        final String fileName = uploadFile.getName();
        RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), uploadFile);
        MultipartBody.Part fileMultipart = MultipartBody.Part.createFormData("files", fileName, requestBody);

        Log.e("MultiPart: ",requestBody.toString());
        Log.e("MultiPart: ",fileMultipart.toString());
        multipartList.add(fileMultipart);


//        UploadRequest uploadRequest = new UploadRequest();
//        uploadRequest.setFile(fileMultipart);
//        uploadRequest.setConferenceUsersDtoList(employeeLists);
//        uploadRequest.setCallId(AppData.Call_ID);
//        uploadRequest.setCustId(AppData.CustID);
//        uploadRequest.setDocumentTitle("test");

//        String stringToPost = new Gson().toJson(employeeLists);
//        RequestBody requestBody1 = RequestBody.create(
//                MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
//                stringToPost
//        );

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("file", uploadFile.getName(),RequestBody.create(MediaType.parse(getContentResolver().getType(uri)),uploadFile)) ;
        builder.addFormDataPart("conferenceUsersDtoList", new Gson().toJson(employeeLists));
        builder.addFormDataPart("custId", AppData.CustID);
        builder.addFormDataPart("callId",AppData.Call_ID);
        builder.addFormDataPart("documentTitle",fileName);


//        MultipartBody.Part Listdescription = MultipartBody.Part.createFormData("conferenceUsersDtoList", stringToPost);
//
//        RequestBody CustIDPart = RequestBody.create(MultipartBody.FORM, AppData.CustID);
//        RequestBody CallIDPart = RequestBody.create(MultipartBody.FORM, AppData.Call_ID);
//        RequestBody DocumentTitlePart = RequestBody.create(MultipartBody.FORM, "Test");

        RequestBody requestBody1 = builder.build();


        ApiClient.getApiClient().getuploadfile(requestBody1).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {

                if(response.isSuccessful()){
                    UploadResponse uploadResponse= response.body();

                    try {
                        if(uploadResponse.getPayload().getSuccess().equals("SUCCESS")){
                            Log.e("Conference", "inside " );
                            for(conferenceUsersDtoList a: employeeLists){
                                Log.e("Conference", "Filepath : " + uploadResponse.getPayload().getFilepath() );
                                AppData.socketClass.send(a.getLoginId(), "fileSentByCust#"+ uploadResponse.getPayload().getFilepath() + "#" + uploadResponse.getPayload().getDocTitle());
                            }
                            Toast.makeText(ChatConferenceActivity.this,uploadResponse.getPayload().getSuccess() /*+ uploadResponse.getPayload().getFilepath()*/,Toast.LENGTH_LONG).show();
                        }else if(!uploadResponse.isStatus()){
                            Toast.makeText(ChatConferenceActivity.this, uploadResponse.getError().getErrorMessage(),Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(ChatConferenceActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Toast.makeText(ChatConferenceActivity.this,"Upload failure",Toast.LENGTH_LONG).show();
            }
        });

    }


    public void showAlertDialogOnBackPressed(){
        new AlertDialog.Builder(this,R.style.AlertDialogTheme)
                .setTitle("Exit")
                .setMessage("Do you want to exit or disconnect the chat ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                      Setting username, id and all displayed messages to null on exit. It is important to do so otherwise
                        listOfUsersId=null;
                        listOfUsersName=null;
                        al_chat_everyone=null;
                        al_chat_specific_user=null;

                        // Destroy the ShowGuestPromotionalVideoActivity remotely from ChatConferenceActivity. This will unregister(chatMsgReceiver);
                        ShowGuestPromotionalVideoActivity.SGPA.finish();

                        AppData.Agent_login_id="";
                        AppData.Agent_id="";
                        AppData.CallType="";
                        callHangupApiCall();
                        AppData.socketClass.removeSocket();

                        // Destroy the EveryoneChatActivity remotely from ChatConferenceActivity.
                        EveryoneChatActivity.ECA.finish();

                        startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                        finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void callHangupApiCall() {
        hangUpCustomerRequest = new HangUpCustomerRequest();
        hangUpCustomerRequest.setCallId(AppData.Call_ID);
        hangUpCustomerRequest.setCustId(AppData.CustID);

        ApiClient.getApiClient().gethangupcustomer(hangUpCustomerRequest).enqueue(new Callback<HangUpCustomerResponse>() {
            @Override
            public void onResponse(Call<HangUpCustomerResponse> call, Response<HangUpCustomerResponse> response) {

            }

            @Override
            public void onFailure(Call<HangUpCustomerResponse> call, Throwable t) {

            }
        });
    }

    private class ChatReceivedUserReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.e("chatRecUserReceiver", "UserListSize: " + ShowGuestPromotionalVideoActivity.al_chat_specific_user.size());
                lv_userSpecificChat.setAdapter(chatAdapter);
            } catch (Exception e) {
                Log.e("chatRecUserReceiverEx", "ExceptionCause: " + e.getMessage());
            }
        }
    }
    /**
     * BroadCast Receiver For File Receive During Call
     */
    private class DownloadFileReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(!AppData.CallType.equalsIgnoreCase("chat")){
                    Toast.makeText(context, "File received. Go back to video conference page for more info.", Toast.LENGTH_SHORT).show();

                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatConferenceActivity.this, R.style.AlertDialogTheme);

                    alertDialog.setTitle("File Receive");
                    alertDialog.setMessage("You have received a file");
                    alertDialog.setIcon(R.drawable.ic_receive_file);
                    alertDialog.setCancelable(false)
                            .setPositiveButton("View", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                                   /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppData.FILE_RECEIVE_URL));
                        startActivity(Intent.createChooser(browserIntent, "Select your choice"));*/

                                    Log.e("FileReceiveURL: ",AppData.FILE_RECEIVE_URL);
                                    Intent launchGoogleChrome = new Intent(Intent.ACTION_VIEW, Uri.parse(AppData.FILE_RECEIVE_URL));
//                        launchGoogleChrome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        launchGoogleChrome.setPackage("com.android.chrome");
//                        launchGoogleChrome.putExtra("com.android.chrome.EXTRA_OPEN_NEW_INCOGNITO_TAB", true);

                                    try {
                                        startActivity(Intent.createChooser(launchGoogleChrome,"Select your choice"));
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
//                            launchGoogleChrome.setPackage(null);
//                            startActivity(launchGoogleChrome);
                                        startActivity(Intent.createChooser(launchGoogleChrome,"Select your choice"));
                                    }


                                    dialog.cancel();
                                }
                            })
                        .setNeutralButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DownloadManager.Request request= new DownloadManager.Request(Uri.parse(AppData.FILE_RECEIVE_URL));
                                String title = URLUtil.guessFileName(AppData.FILE_RECEIVE_URL,null,null);
                                request.setTitle(title);
                                request.setDescription("Downloading file. Please wait...");
                                String cookies= CookieManager.getInstance().getCookie(AppData.FILE_RECEIVE_URL);
                                request.addRequestHeader("cookies",cookies);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                                DownloadManager downloadManager= (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);

                                Toast.makeText(context, "Downloading started.", Toast.LENGTH_SHORT).show();
                            }
                        })
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = alertDialog.create();

                    dialog.show();
                }

            } catch (Exception e) {
                Log.e("DownloadFileReceiver", "ExceptionCause: " + e.getMessage());
            }
        }
    }
}