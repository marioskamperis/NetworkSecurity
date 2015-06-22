package com.example.marios.networksecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by marios on 20/06/2015.
 */
public class Generator extends Activity {

    TextView text ;
    private String TAG = "Generator";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generator);


        text = (TextView)findViewById(R.id.txtCode);


        Intent i = getIntent();

        String txt = i.getStringExtra("message");

        txt = txt.substring(txt.indexOf(':')+1).trim();
        Log.d(TAG, txt);
        text.setText(txt);
    }
//    protected void onStop(){
//        finish();
//    }
//    protected void onPause(){
//        finish();
//    }

}
