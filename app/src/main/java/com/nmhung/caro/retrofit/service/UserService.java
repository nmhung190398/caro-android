package com.nmhung.caro.retrofit.service;

import com.nmhung.caro.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @POST("login")
//    @FormUrlEncoded
    Call<Object> login(@Query("username") String username, @Query("password") String password);
}
