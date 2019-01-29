package com.example.eafor.socialnetwork.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.fragments_auth.LogFragment;
import com.example.eafor.socialnetwork.fragments_auth.RegFragment;
import com.example.eafor.socialnetwork.fragments_main.FragmentChat;
import com.example.eafor.socialnetwork.fragments_main.FragmentMessage;
import com.example.eafor.socialnetwork.fragments_main.FragmentProfile;
import com.example.eafor.socialnetwork.server_connection.ServerStatus;
import com.example.eafor.socialnetwork.server_connection.SubThread;


import static com.example.eafor.socialnetwork.activities.AuthActivity.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    NavigationView navigationView;
    //ServerStatus serverStatus;

    Intent intent;
    String login, password, nick;
    Context context;

    public Handler handler;

    SubThread subThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentMessage()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }

        intent=getIntent();
        if(intent!=null){
            login=intent.getStringExtra(AuthActivity.NAME_LOGIN);
            password=intent.getStringExtra(AuthActivity.NAME_PASSWORD);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                appendText(text);
            }
        };

        AuthActivity.serverStatus.interrupt();
        AuthActivity.serverStatus=null;

        if(serverStatus==null)serverStatus=new ServerStatus(this, FLAG_MAIN_ACTIVITY, FLAG_MAIN_ACTIVITY);
        serverStatus.reboot(FLAG_MAIN_ACTIVITY, this);
        //serverStatus.reboot(this,FLAG_MAIN_ACTIVITY);
        //serverStatus.tryToAuth();
        //serverStatus.logIn(login, password);

        subThread=new SubThread(this, 2); subThread.start();
        //TODO: connection

        Toast.makeText(this,login+" "+password,Toast.LENGTH_LONG).show();




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentMessage()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentChat()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentProfile()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share_part", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "send_part", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }




    public void appendText(String a){

      // if(a.equals("/online")){
      //     if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setText("Server status: online");
      //     if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setText("Server status: online");
      // }else if(a.equals("/offline")){
      //     if(LogFragment.getServerStatusView()!=null) LogFragment.getServerStatusView().setText("Server status: offline");
      //     if(RegFragment.getServerStatusView()!=null) RegFragment.getServerStatusView().setText("Server status: offline");
      // }else{
      //     Toast.makeText(this, a,Toast.LENGTH_SHORT).show();
      // }
        if(!a.equals("/offline"))
        Toast.makeText(this, a,Toast.LENGTH_SHORT).show();
    }
}
