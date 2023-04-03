package com.rank.kuber.Activity;

import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersId;
import static com.rank.kuber.Activity.ShowGuestPromotionalVideoActivity.listOfUsersName;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rank.kuber.ApiClient;
import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.ChatModel;
import com.rank.kuber.Model.HangUpCustomerRequest;
import com.rank.kuber.Model.HangUpCustomerResponse;
import com.rank.kuber.R;
import com.rank.kuber.socket.SocketClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EveryoneChatActivity extends AppCompatActivity {

    String TAG = "EveryoneChatActivity";
    HangUpCustomerRequest hangUpCustomerRequest;
    public static int selectedChatUserPos = -1;
    public static String receivedChatId;  //Required to show a yellow dot beside the user, to identify that chat msg has received
    private ChatUserListAdapter chatUserListAdapter;
    private ImageView back_img;
    private ListView lv_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everyone_chat);

        /*Set Global Context*/
        AppData.currentContext = EveryoneChatActivity.this;

        /*Call Required Functions*/
        getUIComponents();

        chatUserListAdapter = new ChatUserListAdapter();
        lv_chat.setAdapter(chatUserListAdapter);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        showAlertDialogOnBackPressed();
    }

    public void showAlertDialogOnBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit or disconnect the chat ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        listOfUsersId=null;
                        listOfUsersName=null;
                        AppData.Agent_login_id="";
                        AppData.Agent_id="";
                        callHangupApiCall();
                        AppData.socketClass.removeSocket();
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

    public static EveryoneChatActivity getInstance() {
        return (EveryoneChatActivity) AppData.currentContext;
    }

    private void getUIComponents() {
        lv_chat = (ListView) findViewById(R.id.lv_chat);
        back_img = findViewById(R.id.chat_back_img);
    }


    private class ChatUserListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ShowGuestPromotionalVideoActivity.listOfUsersId.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            LayoutInflater layoutInflater = getLayoutInflater();

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.layout_user_specific_chat_row, parent, false);

                viewHolder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
                viewHolder.tv_yellowDot = (TextView) convertView.findViewById(R.id.tv_yellow_dot);
                viewHolder.tv_userName = (TextView) convertView.findViewById(R.id.tv_username);
                viewHolder.tv_userID = (TextView) convertView.findViewById(R.id.tv_userid);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            /* To show yellow dot beside the user for unread chat messages */
            if ((receivedChatId != null) && (receivedChatId.length() > 0)) {
                Log.e("getViewIf", "ReceivedChatId: " + receivedChatId + ", Position: " + position);

                if (ShowGuestPromotionalVideoActivity.isEveryoneSelected) {
                    Log.e("getView_If", "Everyone Selected");

                    if (position == 0) {
                        viewHolder.tv_yellowDot.setVisibility(View.VISIBLE);
                        viewHolder.tv_yellowDot.setText(ShowGuestPromotionalVideoActivity.msgCounter + "");
                        viewHolder.iv_arrow.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_yellowDot.setVisibility(View.GONE);
                        viewHolder.iv_arrow.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("getView_If", " everyone not selected ");

                    if (receivedChatId.equalsIgnoreCase(ShowGuestPromotionalVideoActivity.listOfUsersId.get(position)) && position > 0) {
                        viewHolder.tv_yellowDot.setVisibility(View.VISIBLE);
                        viewHolder.tv_yellowDot.setText(ShowGuestPromotionalVideoActivity.msgCounter + "");
                        viewHolder.iv_arrow.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_yellowDot.setVisibility(View.GONE);
                        viewHolder.iv_arrow.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Log.e("getView_Else", " receivedChatId: " + receivedChatId + " pos: " + position);
                viewHolder.tv_yellowDot.setVisibility(View.GONE);
                viewHolder.iv_arrow.setVisibility(View.VISIBLE);
            }

            viewHolder.tv_userName.setText(ShowGuestPromotionalVideoActivity.listOfUsersName.get(position));
            viewHolder.tv_userID.setText(ShowGuestPromotionalVideoActivity.listOfUsersId.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ShowGuestPromotionalVideoActivity.al_chat_everyone.size() > 0) {
                        filterBySelectedChatUser(ShowGuestPromotionalVideoActivity.listOfUsersId.get(position), position);
                    }

                    /* To make unread messages as read/seen */
                    if (receivedChatId != null && receivedChatId.length() > 0) {
                        Log.e("getView_onClick", " receivedChatId: " + receivedChatId + " pos: " + position);

                        if ((viewHolder.iv_arrow.getVisibility() == View.GONE) && (viewHolder.tv_yellowDot.getVisibility() == View.VISIBLE)) {

                            receivedChatId = "";
                            ShowGuestPromotionalVideoActivity.msgCounter = 0;
                            viewHolder.tv_yellowDot.setVisibility(View.GONE);
                            viewHolder.iv_arrow.setVisibility(View.VISIBLE);
                        } else {

                            viewHolder.tv_yellowDot.setVisibility(View.VISIBLE);
                            viewHolder.tv_yellowDot.setText(ShowGuestPromotionalVideoActivity.msgCounter + "");
                            viewHolder.iv_arrow.setVisibility(View.GONE);
                        }
                    }
                    /* By default while creating 'listOfUsersId' arraylist, 'Everyone' is entered at the zero index */
                    if (position == 0) {
                        ShowGuestPromotionalVideoActivity.isEveryoneSelected = true;
                    } else {
                        ShowGuestPromotionalVideoActivity.isEveryoneSelected = false;
                    }

                    selectedChatUserPos = position;
                    startActivity(new Intent(EveryoneChatActivity.this, ChatConferenceActivity.class));
                    chatUserListAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
    private static class ViewHolder {
        private TextView tv_userName, tv_userID, tv_yellowDot;
        private ImageView iv_arrow;
    }


    private void filterBySelectedChatUser(String id, int pos) {

        try {
            ShowGuestPromotionalVideoActivity.al_chat_specific_user.clear();

            /* Filter by specific/selected user, excluding 'Everyone' */
            if (pos > 0) {
                for (int i = 0; i < ShowGuestPromotionalVideoActivity.al_chat_everyone.size(); i++) {

                    if (!ShowGuestPromotionalVideoActivity.al_chat_everyone.get(i).getEveryone()) {
                        ShowGuestPromotionalVideoActivity.al_chat_specific_user.add(ShowGuestPromotionalVideoActivity.al_chat_everyone.get(i));
                    }
                }
            }
            else {  //TODO To Be REMOVED
                /* Filter by 'Everyone' */
                for (int i = 0; i < ShowGuestPromotionalVideoActivity.al_chat_everyone.size(); i++) {

                    if (ShowGuestPromotionalVideoActivity.al_chat_everyone.get(i).getEveryone()) {
                        ShowGuestPromotionalVideoActivity.al_chat_specific_user.add(ShowGuestPromotionalVideoActivity.al_chat_everyone.get(i));
                    }
                }
            }

            Log.e("filterBySelectChatUser", "size: " + ShowGuestPromotionalVideoActivity.al_chat_specific_user.size());
        } catch (Exception e) {
            Log.e("filterBySelectChatUser", e.toString());
        }
    }



}