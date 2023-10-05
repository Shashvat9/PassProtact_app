package com.example.lockbox;

import static java.lang.System.gc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    Button signin,signup;
    EditText email_et,pass_et;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    myRequest request;
    myMethods methods;

    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sharedPreferences = getSharedPreferences(Params.SHAREDP_REFERENCES,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email_et = findViewById(R.id.email);
        pass_et = findViewById(R.id.password);
        signin = findViewById(R.id.bt_sign_in);
        signup = findViewById(R.id.bt_sign_up);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        methods = new myMethods();


        SharedPreferences sharedPreferences = getSharedPreferences(Params.SHAREDP_REFERENCES,MODE_PRIVATE);

        SharedPreferences get = getSharedPreferences(Params.SHAREDP_REFERENCES, MODE_PRIVATE);

        if(get.getString("email",null)!=null)
        {
            Log.d(Params.loogdTag, "Signin_page/onCreate: "+get.getString("email",null));
            Intent send = new Intent(this,MainActivity.class);
            startActivity(send);
        }

        signin.setTranslationY(100);
        signin.animate().translationYBy(-50).setDuration(1000);

        signup.setTranslationY(100);
        signup.animate().translationYBy(-50).setDuration(1000);

        gc();

    }

    @Override
    protected void onStart() {
        super.onStart();

//        String email = String.valueOf(email_et.getText());
//        String pass = String.valueOf(pass_et.getText());

        Drawable red_line = ContextCompat.getDrawable(getApplicationContext(),R.drawable.empty);
        Drawable green_line = ContextCompat.getDrawable(getApplicationContext(),R.drawable.green_line);

        email_et.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!methods.isValidEmail(email_et.getText().toString())) {
                    email_et.setBackground(red_line);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        VibrationEffect effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
                        vibrator.vibrate(effect);
                    }
                } else {
                    email_et.setBackground(green_line);

                }
            }
        });

        signin.setOnClickListener(v -> {
            VibrationEffect effect = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
            }
            vibrator.vibrate(effect);
            request = new myRequest(this,methods.setJsonValidate(email_et.getText().toString(),pass_et.getText().toString()),Params.VALIDATE);

            request.setOnSecussListener(data -> {
                Log.d(Params.loogdTag, "onCreate: "+data.toString());
                if(methods.isSccuss(data)){
                    Intent send = new Intent(getApplicationContext(), MainActivity.class);
                    Log.d(Params.loogdTag, "/Signin_pageonstart/signin/onclick/onJsonReceived/if: sign in");
                    Toast.makeText(getApplicationContext(), "wellcome", Toast.LENGTH_SHORT).show();
                    editor.putString("email",email_et.getText().toString());
                    editor.apply();
                    startActivity(send);
                }
                else{
                    if(methods.getCode(data)==3){
                        Toast.makeText(this, "there is no such user found please create one account", Toast.LENGTH_SHORT).show();
                    } else if (methods.getCode(data)==2) {
                        Toast.makeText(this, "wrong pasword", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            request.setOnErrorListener(error -> {
                Log.d(Params.loogdTag, "onCreate: "+error);
            });

            if (email_et.getText().toString().equals("")||pass_et.getText().toString().equals(""))
            {
                Toast.makeText(this, "both filds must not be empty", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, , Toast.LENGTH_SHORT).show();
                Log.d(Params.loogdTag, "onStart: "+"email = "+ email_et.getText() +" pass= "+pass_et.getText().toString());
            }
            else
            {
                if(!methods.isValidEmail(email_et.getText().toString()))
                {
                    Toast.makeText(this,"invalid email formate.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    request.sendRequest();
                }
            }
        });

        signup.setOnClickListener(v->{
            Intent send = new Intent(getApplicationContext(), SignUp.class);
            startActivity(send);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        signup=null;
        signin=null;
        email_et=null;
        pass_et=null;
        sharedPreferences = null;
        editor.clear();
        editor = null;
        request=null;
        methods=null;
        vibrator = null;
        gc();
    }
}