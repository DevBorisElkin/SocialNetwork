package com.example.eafor.socialnetwork.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.support.UserData;
import com.example.eafor.socialnetwork.fragments_main.FragmentChat;
import com.example.eafor.socialnetwork.fragments_main.FragmentMessage;
import com.example.eafor.socialnetwork.fragments_main.FragmentProfile;
import com.example.eafor.socialnetwork.fragments_main.FragmentUsers;
import com.example.eafor.socialnetwork.fragments_main.Fragment_custom;
import com.example.eafor.socialnetwork.server_connection.ServerStatus;
import com.example.eafor.socialnetwork.server_connection.SubThread;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.eafor.socialnetwork.activities.AuthActivity.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    NavigationView navigationView;

    public List<UserData> userDataList = new ArrayList<>();
    public static List<UserData> staticUserDataList = new ArrayList<>();
    public List<Fragment_custom> fragmentList = new ArrayList<>();
    FragmentUsers   fragmentUsers   = new FragmentUsers();
    FragmentChat    fragmentChat    = new FragmentChat();
    FragmentMessage fragmentMessage = new FragmentMessage();
    FragmentProfile fragmentProfile = new FragmentProfile();

    Intent intent;
    String login, password, nick;
    Context context;

    public Handler handler;
    SubThread subThread;

    ImageView img_avatar;
    TextView txt_nickname;
    public static List<String> list;  //Используются при парсинге списка пользователей

    public static List<String> getList(){return list;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentUsers.set_id(0);   fragmentList.add(fragmentUsers);
        fragmentChat.set_id(1);    fragmentList.add(fragmentChat);
        fragmentMessage.set_id(2); fragmentList.add(fragmentMessage);
        fragmentProfile.set_id(3); fragmentList.add(fragmentProfile);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        intent=getIntent();
        if(intent!=null){
            login=intent.getStringExtra(AuthActivity.NAME_LOGIN);
            password=intent.getStringExtra(AuthActivity.NAME_PASSWORD);
        }

        img_avatar=navigationView.findViewById(R.id.header_img);
        txt_nickname=navigationView.findViewById(R.id.header_txt_nick);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                appendText(text);
            }
        };

        AuthActivity.serverStatus.interrupt();


        if(serverStatus==null)serverStatus=new ServerStatus(this, FLAG_MAIN_ACTIVITY, FLAG_MAIN_ACTIVITY);
        serverStatus.reboot(FLAG_MAIN_ACTIVITY, this);


        subThread=new SubThread(this, 2); subThread.start();
        serverStatus.getUsers();


        if(savedInstanceState==null) {
            update(0);
            navigationView.setCheckedItem(R.id.nav_users);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_users:
                update(0);
                break;
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

    public void update(int id){
        if(id==0){
            FragmentUsers fragmentTMP = new FragmentUsers();
            getSupportFragmentManager().beginTransaction().remove(fragmentList.get(0)).replace(R.id.fragment_container,fragmentTMP).commit();
            fragmentList.set(0,fragmentTMP);
            //list.get(0).update();
        }else{
            // Доделать позже
        }

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
        if(a.startsWith("/compl_data")){
            String[]tokens=a.split(" ");
            list= Arrays.asList(tokens);
        }else if(a.startsWith("/all_users_data@")){
            userDataList=null; userDataList = new ArrayList<>();
            String id, login, password, nickname, avatar, description, status, last_online;
            String[]tokens=a.split("@");
            for(int i=1;i<tokens.length;i++){
                String[] params=tokens[i].split(" ");
                id=params[0];
                login=params[1];
                password=params[2];
                nickname=params[3];
                avatar=params[4];
                description=params[5];
                status=params[6];
                last_online=params[7];
                userDataList.add(new UserData(id,login,password,nickname,avatar,description,status,last_online));
            }
            staticUserDataList = new ArrayList<>(userDataList);
            if(navigationView.getCheckedItem().getItemId()==R.id.nav_users)
            update(0);




        } else if(!a.equals("/offline")) Toast.makeText(this, a,Toast.LENGTH_SHORT).show();
    }
}














