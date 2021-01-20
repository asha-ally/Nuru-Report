package com.openinstitute.nuru.rest;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class RegistrationResponse {
    @SerializedName("data")private JSONObject data;
    @SerializedName("user_id")private int id;
    @SerializedName("auth_token")private String token;

//    int code, String token

    public RegistrationResponse(JSONObject data) {
        this.data =data;
        this.id = id;
        this.token = token;
    }

    public String getData() {
        return data.toString();
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
