package com.nmhung.caro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nmhung.caro.R;
import com.nmhung.caro.constant.Constant;
import com.nmhung.caro.model.UserModel;
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
import static com.nmhung.caro.constant.Constant.USER_LOGIN_MODEL;


public class LoginActivity extends BaseActivity {


    private EditText txtIpConfig;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private UserService userService;
    private Button openDAO;

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

    @Override
    protected void findViews() {

    }

    @Override
    protected void setEvents() {

    }

    private void createEvent() {

        findViewById(R.id.btnDangKy).setOnClickListener(v -> {
            Context context = getBaseContext();
            Intent intent = BaseActivity.newIntent(context, DangKyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });


        btnLogin.setOnClickListener(view -> {
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            Constant.BASE_URL = txtIpConfig.getText().toString();
//            USER_LOGIN = "admin";
//            Context context = getBaseContext();
//            Intent intent = BaseActivity.newIntent(context, RoomActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);


            RetrofitClient.getAPIService(UserService.class).login(username, password).enqueue(new Callback<UserModel>() {

                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    String msg = "Đăng nhập thành công";
                    switch (response.code()) {
                        case 200:
                            Context context = getBaseContext();
                            Intent intent = BaseActivity.newIntent(context, RoomActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            USER_LOGIN = username;
                            USER_LOGIN_MODEL = response.body();
                            context.startActivity(intent);
                            break;
                        case 404:
                            msg = "Lỗi hệ thống";
                            break;
                        case 300:
                            msg = "Sai mật khẩu hoặc tài khoản";
                            break;
                        default:
                            msg = "Lỗi hệ thống";
                            //login
                    }
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                    Log.e("call-api", t.getMessage());
                }
            });
        });

    }


}
