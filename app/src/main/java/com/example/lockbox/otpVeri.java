package com.example.lockbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class otpVeri extends AppCompatActivity {
    myRequest request;
    myMethods methods;
    Button veri;

    Button resend;

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_veri);

        methods = new myMethods();
        veri = findViewById(R.id.verify);
        et=findViewById(R.id.email);
    }

    @Override
    protected void onStart() {
        super.onStart();

        veri.setOnClickListener(v->{
            Intent recive = getIntent();
            String St_name = recive.getStringExtra("name");
            String St_email = recive.getStringExtra("email");
            String St_number = recive.getStringExtra("number");
            String St_pass = recive.getStringExtra("pass");
            String otp = recive.getStringExtra("otp");

            int enteredOtp = Integer.parseInt(otp);

            if(enteredOtp==Integer.parseInt(et.getText().toString()))
            {
                request=new myRequest(getApplicationContext(),methods.setJsonAdduser(St_name,St_email,St_number,St_pass),Params.ADD_USER);

                request.setOnSecussListener(data -> {
                    if(methods.isSccuss(data))
                    {
                        Intent send = new Intent(getApplicationContext(),SignIn.class);
                        Toast.makeText(getApplicationContext(), "Your account has been created.🥳", Toast.LENGTH_SHORT).show();
                        startActivity(send);
                    }
                    else if (methods.getCode(data)==5) {
                        Toast.makeText(getApplicationContext(), "there's an account with this email please use a different email.😟😟", Toast.LENGTH_SHORT).show();
                    }
                    else if (methods.getCode(data)==9) {
                        Toast.makeText(getApplicationContext(), "there is no such email exist please recheck email. 🤦‍♂️🤦‍♂️", Toast.LENGTH_SHORT).show();
                    }
                });

                request.setOnErrorListener(error -> {
                    Log.d(Params.loogdTag, "sign_up/add.onclick/onError: "+error);
                    Toast.makeText(getApplicationContext(), "there is a problame with server plese stay tuned.😅", Toast.LENGTH_LONG).show();
                });

                request.sendRequest();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"wrong OTP",Toast.LENGTH_SHORT).show();
            }


        });
    }
}