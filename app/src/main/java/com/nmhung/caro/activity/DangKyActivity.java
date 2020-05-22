package com.nmhung.caro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nmhung.caro.R;
import com.nmhung.caro.model.ResponseModel;
import com.nmhung.caro.model.UserModel;
import com.nmhung.caro.retrofit.RetrofitClient;
import com.nmhung.caro.retrofit.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends BaseActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private UserService userService;
    private Button btnDangKy;

    public DangKyActivity() {
        super(R.layout.activity_dang_ky);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnDangKy = findViewById(R.id.dangKy);
        findViewById(R.id.dangKy).setOnClickListener(v -> {
            UserModel userModel = new UserModel();
            userModel.setUsername(txtUsername.getText().toString());
            userModel.setPassword(txtPassword.getText().toString());
            RetrofitClient.getAPIService(UserService.class).register(userModel).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    String msg = "";
                    switch (response.code()) {
                        case 200:
                            Context context = getBaseContext();
                            Intent intent = BaseActivity.newIntent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case 300:
                            msg = "tài khoản đã tồn tại";
                            break;
                        default:
                            msg = "dữ liệu đầu vào không đúng";
                            //login
                    }
                    if (response.code() != 200) {
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Lỗi hệ thống", Toast.LENGTH_LONG).show();
                    Log.e("ERROR", t.getMessage());
                }
            });
        });
    }

    @Override
    protected void findViews() {
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
    }

    @Override
    protected void setEvents() {

    }
}
