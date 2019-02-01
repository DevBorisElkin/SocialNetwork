package com.example.eafor.socialnetwork.server_connection;


import android.content.Intent;
import android.os.Message;

import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.activities.MainActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ServerStatus {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    AuthActivity auth_activity;
    MainActivity main_activity;
    Message msg;
    public int flag=0;
    public static int currentActivityFlag=1;


    final String IP_ADDRESS = "192.168.0.101";
    final int PORT = 8189;

    private static boolean isAuthorized=false;
    private static boolean isConnected=false;

    public void setConnected(boolean b){isConnected=b;}
    public static boolean getConnected(){return  isConnected;}

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }
    public static boolean getAuthorized() { return isAuthorized; }

    public void setFlag(int flag) { this.flag = flag; }

    public ServerStatus(AuthActivity activity, int flag) {
        this.auth_activity =activity;
        this.flag=flag;
    }
    public ServerStatus(AuthActivity activity, int flag, int activityFlag) {
        this.auth_activity =activity;
        this.flag=flag;
        currentActivityFlag=activityFlag;
    }
    public ServerStatus(MainActivity activity, int flag) {
        this.main_activity =activity;
        this.flag=flag;
    }
    public ServerStatus(MainActivity activity, int flag, int activityFlag) {
        this.main_activity =activity;
        this.flag=flag;
        currentActivityFlag=activityFlag;
    }


    public Socket getSocket() { return socket; }


    public void execQuery(String msg) {
        try {
            if(out!=null&&!socket.isClosed()&&socket.isConnected()&&getConnected()){out.writeUTF(msg);}else{sendMsgToMain("/offline");}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void connect() {
        try {
            if(flag==1){
                socket = new Socket(IP_ADDRESS, PORT);
            }else if(flag==2){
                socket = new Socket();
                socket.connect(new InetSocketAddress(IP_ADDRESS, PORT), 1000);
            }

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            setConnected(true);
            Thread thread = new Thread(() -> {
                try {
                    while (socket.isConnected()) {
                        String str = in.readUTF();
                        if(str.startsWith("/auth_ok")) {
                            String[] tokens = str.split(" ");
                            AuthActivity.nickStr=tokens[1];
                            auth_activity.launchActivity();
                            setAuthorized(true);
                            break;
                        } else {
                                sendMsgToMain(str);
                        }
                    }
                    while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/")) {
                                if (str.equals("/serverclosed")) break;
                                if (str.startsWith("/clientslist ")) {
                                    String[] tokens = str.split(" ");
                                }
                                if(str.startsWith("/compl_data")){
                                    sendMsgToMain(str);
                                    //TODO
                                }
                                if(str.startsWith("/all_users_data@")){
                                    sendMsgToMain(str);
                                }
                            } else {
                                sendMsgToMain(str);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setConnected(false);
                        setAuthorized(false);
                        sendMsgToMain("/offline");
                    }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void tryToAuth() {
        if (socket == null || socket.isClosed()||!socket.isConnected()||!getConnected()) {
            connect();
        }
        try {
            if(out!=null){
                out.writeUTF("/check");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void breakConnection() {
        if(socket!=null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setAuthorized(false);
    }

    public void sendMsgToMain(String txt){
        msg = new Message();
        msg.obj = txt;
        if(currentActivityFlag ==AuthActivity.FLAG_AUTH_ACTIVITY){
            if(auth_activity!=null&&auth_activity.handler!=null)auth_activity.handler.sendMessage(msg);
        }else if(currentActivityFlag ==AuthActivity.FLAG_MAIN_ACTIVITY){
            if(main_activity!=null&&main_activity.handler!=null) main_activity.handler.sendMessage(msg);
        }

    }


    public void logIn(String login, String password){
        execQuery("/auth_check "+login+" "+password);
    }

    public void regIn(String login, String password, String nick){
        execQuery("/add_user "+login+" "+password+" "+nick);
    }

    public void getMainInfo(String nick){//TODO:
        execQuery("/get_main_info "+nick);
    }

    public void sendMsgToAll(String txt){
        //
    }

    public void reboot(int activityFlag, MainActivity activity){
        if(main_activity==null) this.main_activity =activity;
        currentActivityFlag =activityFlag;
        //auth_activity=null;
    }

   // public void loggedIn(){
   //     if(currentActivityFlag==1){
   //         intent = new Intent(auth_activity, MainActivity.class);
   //     }
   //
   //     intent.putExtra(AuthActivity.NAME_LOGIN, AuthActivity.loginStr);
   //     intent.putExtra(AuthActivity.NAME_PASSWORD, AuthActivity.passwordStr);
   //     startActivity(intent);
   // }

    public void interrupt(){
        try {
            //socket.close();
            if(auth_activity.subThread!=null) auth_activity.subThread.stop();
            auth_activity.subThread=null;
            auth_activity=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getUsers(){
        execQuery("/get_all_users");
    }




}

