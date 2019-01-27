package com.example.eafor.socialnetwork.activities;


import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.server_connection.ServerStatus;



public class TestActivity extends AppCompatActivity {
    Button btnInit, btnSend, btnBreak;
    TextView output;
    EditText input;
    String text_output="";
    ServerStatus serverStatus;
    public Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnInit=findViewById(R.id.btn_init_connection);
        btnSend=findViewById(R.id.btn_send);
        btnBreak=findViewById(R.id.btn_break_connection);
        output=findViewById(R.id.txt_output);
        input=findViewById(R.id.txt_enter);


        btnInit.setOnClickListener(listener(1));
        btnSend.setOnClickListener(listener(2));
        btnBreak.setOnClickListener(listener(3));


        serverStatus=new ServerStatus(this);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                appendText(text);
            }
        };


    }



    @NonNull
    private View.OnClickListener listener(int i) {
        if(i==1){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverStatus.tryToAuth();
                }
            };
        }else if(i==2){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverStatus.execQuery(input.getText().toString());
                    input.setText("");
                }
            };
        }
        else if(i==3){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverStatus.breakConnection();

                }
            };
        }

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empty
            }
        };
    }

    public void appendText(String a){
        StringBuilder builder = new StringBuilder(text_output);
        builder.append(a+"\n");
        text_output=builder.toString();
        output.setText(text_output);
    }
}
