package com.openinstitute.nuru.rest;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest {
    @SerializedName("data")private JSONObject data;
    @SerializedName("code")private int code;
    @SerializedName("token")private String token;

//    @SerializedName("auth_token")private final String token;

//    int code, String token



    public LoginRequest(JSONObject data) {
        this.data = data;


    }
    public JSONObject getData() {
        Log.d("data",data.toString());
        return data;
    }



    public String getToken() {

        try {
            token = data.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return token;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
}
