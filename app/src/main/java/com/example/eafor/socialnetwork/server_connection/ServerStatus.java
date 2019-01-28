package com.example.eafor.socialnetwork.server_connection;


import android.os.Message;

import com.example.eafor.socialnetwork.activities.AuthActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ServerStatus {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    AuthActivity auth_activity;
    Message msg;
    public int flag=0;

    final String IP_ADDRESS = "192.168.0.104";
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
                        if (str.startsWith("/authok")) {
                            //sendMsgToMain(str);
                            sendMsgToMain("Вы успешно авторизовались!");
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
        if (socket == null || socket.isClosed()||!socket.isConnected()) {
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
        auth_activity.handler.sendMessage(msg);
    }

    public void logIn(String login, String password){
        execQuery("/auth "+login+" "+password);
    }

    public void regIn(String login, String password, String nick){
        execQuery("/add_user "+login+" "+password+" "+nick);
    }

    public void sendMsgToAll(String txt){
        //
    }


}

