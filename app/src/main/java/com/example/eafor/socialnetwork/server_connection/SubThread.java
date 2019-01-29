package com.example.eafor.socialnetwork.server_connection;
import android.content.Intent;

import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.activities.MainActivity;

import static com.example.eafor.socialnetwork.activities.AuthActivity.FLAG_AUTH_ACTIVITY;
import static com.example.eafor.socialnetwork.activities.AuthActivity.serverStatus;


public class SubThread implements Runnable {
    Thread thread;
    int flag=1;
    MainActivity mainActivity;
    AuthActivity authActivity;
    Intent intent;
    boolean oneTime=true;

    public SubThread(MainActivity mainActivity, int flag){
        this.flag=flag;
        this.mainActivity=mainActivity;
        thread=new Thread(this);
    }
    public SubThread(AuthActivity authActivity, int flag){
        this.flag=flag;
        this.authActivity=authActivity;
        thread=new Thread(this);
    }

    public void start(){
        thread.start();
    }

    @Override
    public void run() {
        block:{
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag == 1) {
                    if (!ServerStatus.getConnected()) {
                        serverStatus.setFlag(1);
                        serverStatus.tryToAuth();
                    }
                    serverStatus.execQuery("/check");
                } else if (flag == 2) {
                    if (!ServerStatus.getConnected() || !ServerStatus.getAuthorized()) {
                        if(oneTime){
                            //serverStatus.reboot(FLAG_AUTH_ACTIVITY);
                            intent=new Intent(mainActivity, AuthActivity.class);
                            mainActivity.startActivity(intent);
                            oneTime=false;
                            break block;
                        }

                    }
                }
            }
        } //end of block
    }

    public void setFlag(int flag){
        this.flag=flag;
    }

    public void stop(){
        thread.interrupt();
        thread=null;
    }
}
