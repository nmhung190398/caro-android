package com.nmhung.caro.retrofit.service;

import com.nmhung.caro.model.ResponseModel;
import com.nmhung.caro.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @POST("login")
//    @FormUrlEncoded
    Call<UserModel> login(@Query("username") String username, @Query("password") String password);

    @POST("user")
    Call<Object> register(@Body UserModel userModel);

    @GET("/bang-xep-hang")
    Call<List<UserModel>> bangXepHang();

    @GET("/user")
    Call<UserModel> findByUsername(@Query("username") String username);
}
