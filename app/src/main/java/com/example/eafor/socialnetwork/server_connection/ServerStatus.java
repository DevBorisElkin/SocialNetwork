package com.example.eafor.socialnetwork.server_connection;

import android.app.Activity;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerStatus implements Runnable {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    Activity activity;

    final String IP_ADDRESS = "localhost";
    final int PORT = 8189;

    private boolean isAuthorized;

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    Thread thread;


    public ServerStatus(Activity activity) throws IOException {
        this.activity = activity;
        thread = new Thread(this);
        thread.setDaemon(true);
        startConnection();
        thread.start();
    }

    public void startConnection() throws IOException {
        socket = new Socket(IP_ADDRESS, PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        setAuthorized(false);
    }


    @Override
    public void run() {
        try {
            while (!isAuthorized) {
                out.writeUTF("/checkServerStatus");
                String str = in.readUTF();
                if (str.equals("/online")) {
                    System.out.println("The server is online!");
                    Toast.makeText(activity, "Online", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("The server is offline!");
                    Toast.makeText(activity, "Offline", Toast.LENGTH_SHORT).show();
                    Thread.sleep(5000);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            String str = null;
            try {
                str = in.readUTF();

                if (str.startsWith("/")) {
                    if (str.equals("/serverclosed")) break;
                    if (str.startsWith("/clientslist ")) {
                        String[] tokens = str.split(" ");
//                               Platform.runLater(() -> {
//                                   clientsList.getItems().clear();
//                                   for (int i = 1; i < tokens.length; i++) {
//                                       clientsList.getItems().add(tokens[i]);
//                                   }
//                               });
                    }
                } else {
                    //chatArea.appendText(str + "\n");
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

        }


    }
}
