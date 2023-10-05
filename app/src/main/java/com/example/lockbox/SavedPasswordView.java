package com.example.lockbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

public class SavedPasswordView extends AppCompatActivity {
    myMethods methods;
    myRequest request;
    Button back;
    SwipeRefreshLayout refreshLayout;
    ListView listView;
    SharedPreferences get;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_password_view);
        back = findViewById(R.id.back);
        refreshLayout = findViewById(R.id.refresh);
        listView = findViewById(R.id.list1);
        methods = new myMethods();get = getSharedPreferences(Params.SHAREDP_REFERENCES, MODE_PRIVATE);

        if (get.getString("email", null) == null) {
            Intent send = new Intent(this,SignIn.class);
            startActivity(send);
        }
        else {
            email=get.getString("email", null);
        }
        request = new myRequest(getApplicationContext(),methods.setGetPass(email),Params.GET_PASS);
        request.setOnSecussListener(data -> {
            Log.d(Params.loogdTag, "onCreate:seccuss "+data.toString());
            methods.setListViewData(listView,data.toString());
            refreshLayout.canChildScrollUp();
        });
        request.setOnErrorListener(error -> {
            Log.d(Params.loogdTag, "onCreate:error "+error);
        });
        refreshLayout.setRefreshing(false);
        request.sendRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshLayout.setOnRefreshListener(() -> {

            request = new myRequest(getApplicationContext(),methods.setGetPass(email),Params.GET_PASS);
            request.setOnSecussListener(data -> {
                Log.d(Params.loogdTag, "onCreate:seccuss "+data.toString());
                methods.setListViewData(listView,data.toString());
                refreshLayout.canChildScrollUp();
            });
            request.setOnErrorListener(error -> {
                Log.d(Params.loogdTag, "onCreate:error "+error);
            });
            refreshLayout.setRefreshing(false);
            request.sendRequest();
        });

        back.setOnClickListener(v->{
            Intent backInt = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(backInt);
        });
    }
}