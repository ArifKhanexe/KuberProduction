package com.rank.kuber.Interfaces;

import android.graphics.Bitmap;


import com.vidyo.VidyoClient.Endpoint.Participant;

import java.util.List;

public interface RoomActivityListener {
    void onError(String errorMessage);
    void onRoomConnected();
    void onRoomDisconnected(String message);
    void onActiveParticipantChanged(List streamList);
    //    void onScreenShareStarted(EnxStream sharedStream);
    void onScreenShareStopped();
    void onMicMuted();
    void onMicUnMuted();
    void onVideoMuted();
    void onVideoUnMuted();
    //    void onChatReceived(String message,String senderId,boolean isGroupChat);
    void onSpeakerMuted();
    void onSpeakerUnMuted();
    void onScreenShotCaptured(Bitmap bitmapImage);

    void onParticipantJoined(Participant participant);
    void onParticipantLeft(Participant participant);
}