package com.example.eafor.socialnetwork.server_connection;

import com.example.eafor.socialnetwork.activities.AuthActivity;

public class SubThread implements Runnable {
    Thread thread;

    public SubThread(){
        thread=new Thread(this);
    }

    public void start(){
        thread.start();
    }

    @Override
    public void run() {
        while (true){
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                if(!ServerStatus.getConnected()) {
                    AuthActivity.serverStatus.setFlag(1);
                    AuthActivity.serverStatus.tryToAuth();
                    //appendText("/online");
                }


            AuthActivity.serverStatus.execQuery("/check");
        }
    }
}
