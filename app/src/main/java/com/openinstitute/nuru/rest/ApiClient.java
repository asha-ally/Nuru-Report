package com.openinstitute.nuru.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.openinstitute.nuru.app.Globals.CONF_DOMAIN;
import static com.openinstitute.nuru.app.Globals.CONF_POSTS_API_PUSH;

public class ApiClient {

    public static Retrofit createRetrofit() {
        String API_BASE_URL = CONF_DOMAIN;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();
        return retrofit;
    }
}
