package com.example.eafor.socialnetwork.server_connection;


import android.os.Message;

import com.example.eafor.socialnetwork.activities.TestActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ServerStatus {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    TestActivity activity;
    Message msg;

    final String IP_ADDRESS = "192.168.0.104";
    final int PORT = 8189;

    private boolean isAuthorized;

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }


    public ServerStatus(TestActivity activity) {
        this.activity=activity;
    }

    public void execQuery(String msg) {
        try {
            if(out!=null) out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/authok")) {
                            sendMsgToMain(str);
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
                    setAuthorized(false);
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void tryToAuth() {
        if (socket == null || socket.isClosed()) {
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
        activity.handler.sendMessage(msg);
    }

    public void sendMsgToAll(String txt){
        //
    }


}

