package com.example.eafor.socialnetwork.activities;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.adapter.SectionsPageAdapter;
import com.example.eafor.socialnetwork.fragments_auth.LogFragment;
import com.example.eafor.socialnetwork.fragments_auth.RegFragment;
import com.example.eafor.socialnetwork.server_connection.ServerStatus;
import com.example.eafor.socialnetwork.server_connection.SubThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthActivity extends AppCompatActivity {
    SectionsPageAdapter mSectionsPageAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    String text_output="";
    public static ServerStatus serverStatus;
    public Handler handler;
    Thread thread;
    SubThread subThread;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container_auth);
        setupViewPager(mViewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        serverStatus=new ServerStatus(this,2);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                appendText(text);
            }
        };

        serverStatus.tryToAuth();
        subThread=new SubThread(); subThread.start();

    }

    public static ServerStatus getServerStatus(){return serverStatus;}

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LogFragment(), "Log in");
        adapter.addFragment(new RegFragment(), "Registration");
        viewPager.setAdapter(adapter);
    }



    public void appendText(String a){
        if(a.equals("/online")){
            if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setText("Server status: online");
            if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setText("Server status: online");
        }else if(a.equals("/offline")){
            if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setText("Server status: offline");
            if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setText("Server status: offline");
        }else{
            Toast.makeText(this, a,Toast.LENGTH_SHORT).show();
        }

    }
}
