package com.nmhung.caro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class LoginActivity extends AppCompatActivity {

    private static Socket mSocket;
    private EditText mInputMessageView;
    private EditText mTextArea;
    private Button btnLogin;

    public static Socket socket() {
        if (mSocket == null) {
            try {
                mSocket = IO.socket("http://192.168.0.100:3000/");
            } catch (URISyntaxException e) {
            }
        }
        return mSocket;
    }

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        mInputMessageView = findViewById(R.id.msg);
        mTextArea = findViewById(R.id.frmChat);

        createEvent();

        socket = socket();
        socket.on("new_client", onNewClient);
        socket.on("chat_message", onChatMessage);
        socket.connect();
        socket.emit("new_client", "Hdz");
        socket.emit("chat_message", "ahihi");
        socket.emit("hdz", "data ne");
    }

    private void createEvent() {
        btnLogin.setOnClickListener(view -> {
            attemptSend();
        });

    }


    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mInputMessageView.setText("");
        mSocket.emit("chat_message", message);
        this.mTextArea.setText(this.mTextArea.getText() + "\r\nMe :" + message);
    }

    Emitter.Listener onChatMessage = (final Object... args) -> {
        Log.i("HDZ", "");
    };

    Emitter.Listener onNewClient = (final Object... args) -> {

        Log.i("HDZ", "");

    };
}
