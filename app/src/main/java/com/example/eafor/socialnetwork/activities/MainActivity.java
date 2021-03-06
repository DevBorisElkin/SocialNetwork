package com.example.eafor.socialnetwork.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.fragments_main.FragmentDefinedUser;
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
    FragmentUsers   fragmentUsers           = new FragmentUsers();
    FragmentChat    fragmentChat            = new FragmentChat();
    FragmentMessage fragmentMessage         = new FragmentMessage();
    FragmentProfile fragmentProfile         = new FragmentProfile();
    FragmentDefinedUser fragmentDefinedUser = new FragmentDefinedUser();
    public static UserData oneUserData;  //Данные одного пользователя.
    public static boolean allowUpdate=true;
    public static int chosenUserId = 0;
    public static boolean definedUserOpened=false;

    static Dialog dialog;
    Button btn_cancel, btn_ok;
    TextView txt_quit;


    Intent intent;
    String login, password, nick;
    Context context;

    public static Handler handler;
    SubThread subThread;

    ImageView img_avatar;
    TextView txt_nickname;
    public static List<String> list;  //Данные пользователя

    public static List<String> getList(){return list;}

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        fragmentUsers.set_id(0);
        fragmentList.add(fragmentUsers);
        fragmentChat.set_id(1);
        fragmentList.add(fragmentChat);
        fragmentMessage.set_id(2);
        fragmentList.add(fragmentMessage);
        fragmentProfile.set_id(3);
        fragmentList.add(fragmentProfile);
        fragmentDefinedUser.set_id(4);
        fragmentList.add(fragmentDefinedUser);


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


        subThread=new SubThread(this, 2);
        subThread.start();
        serverStatus.getUsers();
        serverStatus.getMainInfo(AuthActivity.nickStr);


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
                update(3);
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
            getSupportFragmentManager().beginTransaction().remove(fragmentList.get(id)).replace(R.id.fragment_container,fragmentTMP).commit();
            fragmentList.set(id,fragmentTMP);
        }else if(id==3){
            FragmentProfile fragmentTMP = new FragmentProfile();
            getSupportFragmentManager().beginTransaction().remove(fragmentList.get(id)).replace(R.id.fragment_container,fragmentTMP).commit();
            fragmentList.set(id,fragmentTMP);
        }else if(id==4){
            FragmentDefinedUser fragmentTMP = new FragmentDefinedUser();
            getSupportFragmentManager().beginTransaction().remove(fragmentList.get(id)).replace(R.id.fragment_container,fragmentTMP).commit();
            fragmentList.set(id,fragmentTMP);
        }else{
            // Доделать позже
        }

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else if(definedUserOpened){
           definedUserOpened=false;
           update(0);
        }else if(definedUserOpened){

        }else{
            makeLogoutDialog("Would you like to log off?");
        }
    }




    public void appendText(String a){
        if(a.startsWith("/compl_data")){
            oneUserData(a);
        }else if(a.startsWith("/all_users_data@")){
            allUsersData(a);
        } else if(a.equals("/code_update_users")){
            update(0);
        }else if(a.equals("/code_update_profile")){
            update(3);
        }else if(a.equals("/code_load_user")){
            update(4);
        }else if(!a.equals("/offline")&&!a.startsWith("/all_users")) {/*Toast.makeText(this, a,Toast.LENGTH_SHORT).show();*/}
    }

    private void allUsersData(String a) {
        userDataList=null;
        userDataList = new ArrayList<>();
        String id, login, password, nickname, avatar, description, status, last_online, messages, painted, joined, paint_delay;
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
            messages=params[8];
            painted = params[9];
            joined = params[10];
            paint_delay = params[11];
            userDataList.add(new UserData(id,login,password,nickname,avatar,description,status,last_online, messages, painted, joined, paint_delay));
        }
        staticUserDataList = new ArrayList<>(userDataList);
        if(navigationView.getCheckedItem().getItemId()== R.id.nav_users) {
            if(allowUpdate){
                new Thread(()->{
                    try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                    update(0);
                    allowUpdate=false;
                }).start();

            }
        }
    }

    private void oneUserData(String a) {
        String[]params=a.split(" ");
        String id, login, password, nickname, avatar, description, status, last_online, messages, painted, joined, paint_delay;
        id=params[0];
        login=params[1];
        password=params[2];
        nickname=params[3];
        avatar=params[4];
        description=params[5];
        status=params[6];
        last_online=params[7];
        messages=params[8];
        painted = params[9];
        joined = params[10];
        paint_delay = params[11];
        oneUserData = new UserData(id,login,password,nickname,avatar,description,status,last_online, messages, painted, joined, paint_delay);
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
                intent=new Intent(MainActivity.this, AuthActivity.class);
                subThread.stop();
                subThread=null;
                launchAuto=false;
                serverStatus.execQuery("/end");
                startActivity(intent);
                dialog=null;
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














