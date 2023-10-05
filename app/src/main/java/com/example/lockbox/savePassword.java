package com.example.lockbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class savePassword extends AppCompatActivity {
    private String finalPass = "";
    TextView id,name,pass;
    Button save;
    myRequest request;
    myMethods methods;
    Intent back;
    SharedPreferences getShared;
    String email="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_password);

        Intent get =  getIntent();
        finalPass = get.getStringExtra("pass");
        Log.d(Params.loogdTag, "onCreate: "+finalPass);
        back = new Intent(this,MainActivity.class);
        getShared = getSharedPreferences(Params.SHAREDP_REFERENCES, MODE_PRIVATE);


        if (getShared.getString("email", null) == null) {
            Intent send = new Intent(this,SignIn.class);
            startActivity(send);
        }

        email = getShared.getString("email",null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        id= findViewById(R.id.idToSave);
        name = findViewById(R.id.nameToSave);
        pass = findViewById(R.id.PasswordToSave);
        save = findViewById(R.id.save);
        methods = new myMethods();
        pass.setText(finalPass);
    }

    @Override
    protected void onResume() {
        super.onResume();

        save.setOnClickListener(v ->{
            request = new myRequest(this,
                    methods.setJsonAddPass(id.getText().toString(),name.getText().toString(),pass.getText().toString(),email),
                    Params.ADD_PASS);


            request.setOnSecussListener(data -> {
                if(methods.isSccuss(data)){
                    Toast.makeText(this, "data added secusses fully.", Toast.LENGTH_SHORT).show();
                    startActivity(back);
                }
                else {
                    Toast.makeText(this, "error in server", Toast.LENGTH_SHORT).show();
                    Log.d(Params.loogdTag, "onResume: "+data.toString());
                }
            });

            request.setOnErrorListener(error -> {
                Log.d(Params.loogdTag, "onResume: "+error);
            });

            request.sendRequest();
        });

    }
}