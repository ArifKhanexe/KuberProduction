package com.rank.kuber.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rank.kuber.Activity.ChatConferenceActivity;
import com.rank.kuber.R;

public class ChatAdapter extends BaseAdapter {

    private Context mContext;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return ChatConferenceActivity.al_chat_specific_user.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (ChatConferenceActivity.al_chat_specific_user.get(position).getLeft()) {
            convertView = inflater.inflate(R.layout.sent_chat_layout, parent, false);
        } else {
            convertView = inflater.inflate(R.layout.receive_chat_layout, parent, false);
        }

        TextView chatDate = (TextView) convertView.findViewById(R.id.txtChatDateTime);
        TextView chatPerson = (TextView) convertView.findViewById(R.id.txtChatSender);
        TextView chatMessage = (TextView) convertView.findViewById(R.id.txtChatMessage);

        chatDate.setText(ChatConferenceActivity.al_chat_specific_user.get(position).getTime());
        chatPerson.setText(ChatConferenceActivity.al_chat_specific_user.get(position).getSenderId());
        chatMessage.setText(ChatConferenceActivity.al_chat_specific_user.get(position).getMsg());

        return convertView;
    }
}
