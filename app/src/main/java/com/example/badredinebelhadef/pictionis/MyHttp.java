package com.example.badredinebelhadef.etnaapp;

import org.json.JSONObject;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by badredinebelhadef on 26/01/2017.
 */

public class MyHttp{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected OkHttpClient client;

    public MyHttp(){
        this.client = new OkHttpClient();
    }

    public Request post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }
}
