package com.nmhung.caro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nmhung.caro.R;
import com.nmhung.caro.constant.Constant;
import com.nmhung.caro.retrofit.RetrofitClient;
import com.nmhung.caro.retrofit.service.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nmhung.caro.constant.Constant.BASE_URL;
import static com.nmhung.caro.constant.Constant.USER_LOGIN;


public class LoginActivity extends BaseActivity {


    private EditText txtIpConfig;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private UserService userService;

    public LoginActivity() {
        super(R.layout.activity_login);
    }


    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnLogin = findViewById(R.id.btnLogin);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        txtIpConfig = findViewById(R.id.ip);
        txtIpConfig.setText(BASE_URL);




        txtUsername.setText("admin");
        txtPassword.setText("123456");

        userService = RetrofitClient.getAPIService(UserService.class);
        createEvent();
//
//        socket = socket();
//        socket.on("new_client", onNewClient);
//        socket.on("chat_message", onChatMessage);
//        socket.connect();
//        socket.emit("new_client", "Hdz");
//        socket.emit("chat_message", "ahihi");
//        socket.emit("hdz", "data ne");
    }

    private void createEvent() {


        btnLogin.setOnClickListener(view -> {
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            Constant.BASE_URL = txtIpConfig.getText().toString();
            userService.login(username, password).enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    String msg = "";
                    switch (response.code()) {
                        case 404:
                            msg = "Lỗi hệ thống";
                            break;
                        case 300:
                            msg = "Sai mật khẩu hoặc tài khoản";
                            break;
                        default:
                            Context context = getBaseContext();
                            Intent intent = BaseActivity.newIntent(context, RoomActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            USER_LOGIN = username;
                            context.startActivity(intent);
                            //login

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    System.out.println();
                }
            });
        });

    }



}
