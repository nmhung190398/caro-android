package com.nmhung.caro.service;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import java.net.URISyntaxException;

import static com.nmhung.caro.constant.Constant.BASE_URL;

public class SocketService {
    private static Socket mSocket;
    String username;
    String password;

    private SocketService() {

    }

    public static Socket socket() {
        if (mSocket == null) {
            try {
                mSocket = IO.socket(BASE_URL);
            } catch (URISyntaxException e) {
            }
        }
        return mSocket;
    }

}
