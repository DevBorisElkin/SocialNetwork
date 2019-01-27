package com.example.eafor.socialnetwork.server_connection;

public class SubThread implements Runnable {
    Thread thread;
    ServerStatus serverStatus;
    public SubThread(ServerStatus serverStatus){
        this.serverStatus=serverStatus;
        thread=new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    @Override
    public void run() {

    }
}
