package com.example.lockbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView show,countTv;
    CheckBox numbers,mixed,latters,punch;
    SeekBar seekBar;
    Button signout,saved;

    boolean isnumber=false,ismixed=false,islatters=false,ispunch=false;

    String  numbersArr;
    String lattesArr;
    String mixedArr;
    String punchArr;

    String finalPass;

    String finalArray;
    Vibrator vibrator;
    SharedPreferences get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = getSharedPreferences(Params.SHAREDP_REFERENCES, MODE_PRIVATE);

        if (get.getString("email", null) == null) {
            Intent send = new Intent(this,SignIn.class);
            startActivity(send);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_menu,menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        show = findViewById(R.id.textView3);
        numbers = findViewById(R.id.numbers);
        mixed = findViewById(R.id.mixed);
        latters = findViewById(R.id.latters);
        punch = findViewById(R.id.punch);
        seekBar = findViewById(R.id.seekBar);
        countTv = findViewById(R.id.textView4);
        finalPass = "";
        finalArray = "";
        signout = findViewById(R.id.signOut);
        saved=findViewById(R.id.saved);

        numbersArr = "0123456789";
        lattesArr = "abcdefghijklmnopqrstuvwxyz";
        mixedArr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        punchArr = "!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        seekBar.setMax(40);
        seekBar.setMin(5);
        seekBar.setProgress(1, true);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                show.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
                    vibrator.vibrate(vibrationEffect);
                } else vibrator.vibrate(200);

                StringBuilder builder = new StringBuilder();

                if (islatters) {
                    builder.append(lattesArr);
                }

                if (ismixed) {
                    builder.append(mixedArr);
                }

                if (isnumber) {
                    builder.append(numbersArr);
                }

                if (ispunch) {
                    builder.append(punchArr);
                }

                if (!ispunch && !islatters && !ismixed && !isnumber) {
                    islatters = true;
                    latters.setChecked(true);
                    builder.append(lattesArr);

                }

                finalArray = builder.toString();

                try {
                    finalPass = mkPass(finalArray, progress);
                    show.setText(finalPass);
                } catch (StringIndexOutOfBoundsException se) {
                    Toast.makeText(MainActivity.this, "please select a feald to continew", Toast.LENGTH_SHORT).show();
                }


                countTv.setText(String.valueOf(progress));

                Log.d("just", "onProgressChanged: " + finalArray);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        signout.setOnClickListener(v->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
                vibrator.vibrate(vibrationEffect);
            } else vibrator.vibrate(200);

            SharedPreferences sharedPreferences = getSharedPreferences(Params.SHAREDP_REFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent signout = new Intent(this, SignIn.class);
            startActivity(signout);


        });

        numbers.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isnumber = isChecked;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);
                vibrator.vibrate(vibrationEffect);
            } else vibrator.vibrate(200);
        });

        latters.setOnCheckedChangeListener((buttonView, isChecked) -> {
            islatters = isChecked;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);
                vibrator.vibrate(vibrationEffect);
            } else vibrator.vibrate(200);
        });

        mixed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ismixed = isChecked;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);
                vibrator.vibrate(vibrationEffect);
            } else vibrator.vibrate(200);
        });

        punch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ispunch = isChecked;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);
                vibrator.vibrate(vibrationEffect);
            } else vibrator.vibrate(200);
        });

        show.setOnLongClickListener(v -> {
            copyToClipBoard("pass1", finalPass);
            Intent save = new Intent(getApplicationContext(), savePassword.class);
            save.putExtra("pass",finalPass);
            startActivity(save);
            return true;
        });

        saved.setOnClickListener(v->{
            Intent savedAct = new Intent(this,SavedPasswordView.class);
            startActivity(savedAct);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        show = null;
        numbers = null;
        mixed = null;
        latters = null;
        punch = null;
        seekBar = null;
        countTv = null;
        finalPass = null;
        finalArray = null;
    }

    public String mkPass(String string, int l) throws StringIndexOutOfBoundsException{
        Log.d("just", "mkPass: before loop "+ string);
        StringBuilder passData= new StringBuilder();
        for(int i=0;i<=l;i++){
            passData.append(string.charAt((int) (Math.random() * (string.length()-2 + 1) + 0)));
            Log.d("just", "mkPass: after second for loop "+passData);
        }
        return passData.toString();
    }

    public void copyToClipBoard(String label,String data){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, data);
        clipboard.setPrimaryClip(clip);
    }
}