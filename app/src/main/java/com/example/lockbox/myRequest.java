package com.example.lockbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class myRequest extends AsyncTask<String , Void , String> {

    @SuppressLint("StaticFieldLeak")
    protected Context context;
    protected String post;
    protected String url = Params.url;
    protected int requestType = Request.Method.POST;
    protected JSONObject PostData;


    interface onSecuss{
        void onSecuss(JSONObject data);
    }

    interface onError{
        void onError(String error);
    }

    onSecuss onSecuss;
    onError onError;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setOnSecussListener(myRequest.onSecuss onSecuss) {
        this.onSecuss = onSecuss;
    }

    public void setOnErrorListener(myRequest.onError onError) {
        this.onError = onError;
    }

    public void setPostData(JSONObject postData) {
        PostData = postData;
    }

    public myRequest(){}
    public myRequest(Context context, JSONObject postData, String post,myRequest.onSecuss onSecuss, myRequest.onError onError) {
        this.context = context;
        this.post = post;
        PostData = postData;
        this.onSecuss = onSecuss;
        this.onError = onError;
    }

    public myRequest(Context context, JSONObject postData, String post) {
        this.context = context;
        this.post = post;
        PostData = postData;
    }


    @Override
    protected String doInBackground(String... urls) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(requestType,
                urls[0],
                response -> {
                    Log.d(Params.loogdTag, "myRequest/doInBackground/response/request done " + new String(Base64.getDecoder().decode(response)));
                    try{
                        JSONObject jsonObject = new JSONObject(new String(Base64.getDecoder().decode(response)));
                        if(onSecuss!=null){
                            onSecuss.onSecuss(jsonObject);
                        }
                    } catch (JSONException e) {
                        Log.d(Params.loogdTag, "myRequest/doInBackground/JSONException: Exception in sendRequest json " + e.getMessage());
                        onError.onError(Arrays.toString(e.getStackTrace()));
                    }
                    catch (IllegalArgumentException ex){
                        Log.d(Params.loogdTag,"myRequest/doInBackground/IllegalArgumentException: Exception in sendRequest json "+ex.getMessage());
                    }
                },
                error -> {
                    try{
                        Log.d(Params.loogdTag, "myRequest/doInBackground/error/error in request " + error);
                    }
                    catch (Exception e){
                        Log.d(Params.loogdTag, "myRequest/doInBackground/error/catch in request " + e.getMessage());
                    }
                    onError.onError(error.toString());
                }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(post, "1");
                params.put("json", Base64.getEncoder().encodeToString(PostData.toString().getBytes()));

                return params;
            }
        };

        requestQueue.add(request);

        return null;
    }

    public void sendRequest(){
            doInBackground(url);
    }


}
