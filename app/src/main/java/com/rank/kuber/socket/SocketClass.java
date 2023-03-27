package com.rank.kuber.socket;

import android.util.Log;

import com.rank.kuber.Common.AppData;
import com.socket.SocketListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SocketClass {
    private String socketUrl, socketPort;

    /**
     * Returns the SocketIO server URL
     */
    public String getSocketUrl() {
        return socketUrl;
    }

    /**
     * This sets the SocketIO server URL Must be set before 'createSocket'
     *
     * @param socketUrl URL of SocketIO server
     */
    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    /**
     * Returns the SocketIO server port
     */
    public String getSocketPort() {
        return socketPort;
    }

    /**
     * This sets the port of the SocketIO server. Must be set before 'createSocket'.
     *
     * @param socketPort Port of SocketIO server. Default value is 3000
     */
    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    /*Socket Creation*/

    /**
     * Creates the socket in the specified SocketIO server, using socketName. Generally called after successful login to the app.
     *
     * @param socketName     Identifier by which socket will be created
     * @param socketListener Listener for the socket events.
     * @throws Exception
     */
    public void createSocket(String socketName, SocketListener socketListener) throws Exception {
        if (getSocketUrl().equalsIgnoreCase("")) {
            throw new Exception("Socket Url is empty", new Throwable("You must set socket url properly for socket communication"));
        }

        String socketUrlWithPort = socketUrl + ":" + socketPort;

        Log.e("socketUrlWithPort", "" + socketUrlWithPort);
        /**
         * Create Socket Reference
         */
        AppData.socket = AppData.socketLibrary.startSocket(socketUrlWithPort, socketName, socketListener);
        Log.e("Socket", "SocketCreated Successfully");
    }

    /**
     * Communication through socket
     *
     * @param receiver To whom the message will be sent
     * @param message  The message to be sent
     */
    public void send(String receiver, String message) {

        JSONObject payLoad = new JSONObject();
        try {
            payLoad.put("customer", receiver);
            payLoad.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppData.socket.emit("send message", payLoad);
        Log.e("SocketSend", receiver+" "+message);
    }

    /**
     * Communication through socket
     *
     * @param receiver    To whom the message will be sent
     * @param jsonMessage The message to be sent as JSON Object
     */
    public void send(String receiver, JSONObject jsonMessage) {

        JSONObject payLoad = new JSONObject();
        try {
            payLoad.put("customer", receiver);
            payLoad.put("message", jsonMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppData.socket.emit("send message", payLoad);
    }

    /*private chat*/

    /**
     * Sending private chat message
     * @param receiver To whom the message will be sent
     * @param message  The message to be sent
     */
    public void sendPrivateChat(String receiver, String message) {

        JSONObject payLoad = new JSONObject();
        try {
            payLoad.put("customer", receiver);
            payLoad.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppData.socket.emit("private-chat", payLoad);
    }

    /*group chat*/

    /**
     * Sending group chat message to a list of users
     * @param receivers List of receivers to whom the message will be sent
     * @param message   The message to be sent
     */
    public void sendGroupChat(ArrayList<String> receivers, String message) {
        JSONObject payLoad = new JSONObject();
        JSONArray receiversJson = new JSONArray(receivers);
        try {
            payLoad.put("receivers", receiversJson);
            payLoad.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppData.socket.emit("group-chat", payLoad);
    }

    /*socket disconnection*/

    /**
     * Removes the socket from the SocketIO server. Generally called after successful logout from the app.
     */
    public void removeSocket() {
        if (AppData.socket != null)
            AppData.socket.disconnect();
    }
}
