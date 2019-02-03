package com.example.eafor.socialnetwork.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    public static ServerStatus serverStatus;
    public Handler handler;
    public SubThread subThread;
    public static final String NAME_LOGIN="name_login";           //
    public static final String NAME_PASSWORD="name_password";     // Data to put into Intent
    public static final String NAME_NICK="name_nick";             //
    public static final String KEY="key7256262";            //
    public static final String KEY_CHECKBOX="key84635";     //
    public static final String KEY_LOGIN="key94763l";       //keys for SharedPreferences
    public static final String KEY_PASSWORD="key123325";    //
    public static final int FLAG_AUTH_ACTIVITY=1;   //
    public static final int FLAG_MAIN_ACTIVITY=2;   //2 flags that indicate what activity is given to ServerStatus of Thread
    public static String loginStr;     //login
    public static String passwordStr;  //password
    public static String nickStr;      //nick of a current user
    public static SharedPreferences mySharedPref;
    public static boolean checkboxChecked;
    public static boolean launchAuto = true;


    static Dialog dialog;
    Button btn_cancel, btn_ok;
    TextView txt_quit;


    Intent intent;





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

        if(serverStatus!=null)serverStatus.interrupt();
        serverStatus=null;  serverStatus=new ServerStatus(this,2, FLAG_AUTH_ACTIVITY);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                appendText(text);
            }
        };

        //serverStatus.reboot(FLAG_AUTH_ACTIVITY);
        serverStatus.tryToAuth();
        subThread=new SubThread(this, 1); subThread.start();

        mySharedPref=getSharedPreferences(AuthActivity.KEY, MODE_PRIVATE);
        checkboxChecked=mySharedPref.getBoolean(KEY_CHECKBOX, false);

        String savedLogin=mySharedPref.getString(KEY_LOGIN,"");
        String savedPass=mySharedPref.getString(KEY_PASSWORD,"");
        if(!savedLogin.equals("")&!savedPass.equals("")&&launchAuto){
            serverStatus.logIn(savedLogin,savedPass);
        }
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
            if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setText("online");
            if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setText("online");
            if(LogFragment.getServerStatusView()!=null)  LogFragment.getServerStatusView().setTextColor(getResources().getColor(R.color.colorGreenOnline));
            if(RegFragment.getServerStatusView()!=null)  RegFragment.getServerStatusView().setTextColor(getResources().getColor(R.color.colorGreenOnline));

        }else if(a.equals("/offline")){
            if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setText("offline");
            if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setText("offline");
            if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setTextColor(getResources().getColor(R.color.colorRedOffline));
            if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setTextColor(getResources().getColor(R.color.colorRedOffline));
        }else if(a.startsWith("/wrong")){
            Toast.makeText(this, a,Toast.LENGTH_SHORT).show();
        }
    }

    public void launchActivity(){
        if(ServerStatus.currentActivityFlag==1){
            intent = new Intent(this, MainActivity.class);
        }
        subThread.stop();
        subThread=null;
        intent.putExtra(AuthActivity.NAME_LOGIN, AuthActivity.loginStr);
        intent.putExtra(AuthActivity.NAME_PASSWORD, AuthActivity.passwordStr);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        makeLogoutDialog("Do you want to quit this app?");
    }

    public void makeLogoutDialog(String text){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.popup_logout);
        dialog.setCanceledOnTouchOutside(true);
        txt_quit=dialog.findViewById(R.id.text_quit);
        txt_quit.setText(text);
        btn_cancel=dialog.findViewById(R.id.button_cancel);
        btn_ok=dialog.findViewById(R.id.button_quit);
        btn_cancel.setOnClickListener(close());
        btn_ok.setOnClickListener(logOut());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @NonNull
    private View.OnClickListener logOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        };
    }

    @NonNull
    private static View.OnClickListener close() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }
}
