package com.rank.kuber.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.rank.kuber.Common.AppData;
import com.rank.kuber.Model.ChatModel;
import com.rank.kuber.R;
import com.rank.kuber.Utils.ChatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatConferenceActivity extends AppCompatActivity implements View.OnClickListener {


    private ListView lv_userSpecificChat;
    private EditText et_writeChatMsg;
    private ImageView iv_sendChat;
    private ChatAdapter chatAdapter;
    private ChatReceivedUserReceiver chatReceivedUserReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conference);

        AppData.currentContext = ChatConferenceActivity.this;
        AppData.TAG = "UserSpecificChatActivity";

        /*Call Required Functions*/
        getUIComponents();
        setClickListenerEvents();

        /*Initialize Objects*/
        initObjects();
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
    }

    public static ChatConferenceActivity getInstance() {
        return (ChatConferenceActivity) AppData.currentContext;
    }

    private void getUIComponents() {
        lv_userSpecificChat = (ListView) findViewById(R.id.lv_userSpecificChat);
        et_writeChatMsg = (EditText) findViewById(R.id.et_writeChatMsg);
        iv_sendChat = (ImageView) findViewById(R.id.iv_sendChat);
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
    }

    @Override
    public void onClick(View view) {
        if (view == iv_sendChat) {
            if (et_writeChatMsg.getText().length() > 0) {

                ChatModel chatModel = new ChatModel();
                chatModel.setMsg(et_writeChatMsg.getText().toString());
                chatModel.setTime(new SimpleDateFormat("dd-MM-yyyy | hh:mm").format(new Date()));
                chatModel.setLeft(false);
                chatModel.setSenderId(ShowGuestPromotionalVideoActivity.listOfUsersId.get(EveryoneChatActivity.selectedChatUserPos));
                chatModel.setEveryone(ShowGuestPromotionalVideoActivity.isEveryoneSelected);


                ShowGuestPromotionalVideoActivity.al_chat_everyone.add(chatModel);
                ShowGuestPromotionalVideoActivity.al_chat_specific_user.add(chatModel);

                if (!ShowGuestPromotionalVideoActivity.isEveryoneSelected) {
                    Log.e("UserSpecificChatAct", "Chat Send To Specific User");
                    AppData.socketClass.sendPrivateChat(ShowGuestPromotionalVideoActivity.listOfUsersId.get(EveryoneChatActivity.selectedChatUserPos), et_writeChatMsg.getText().toString());
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
}