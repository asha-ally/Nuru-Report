package com.openinstitute.nuru.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface{





    @POST("api/register")
    Call<RegistrationResponse> registerUser(@Body JsonObject user);
//    Call<RegistrationResponse>registerUser(@Field("email") String email, @Field("password") String password, @Field("password_confirmation") String password_confirmation, @Field("profile") String profile, @Field("phone") String phone, @Field("name") String name);


    @POST("api/token")
    Call <LoginRequest>  loginUser(@Body JsonObject user);


}
