package com.example.eafor.socialnetwork.fragments_main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.activities.MainActivity;
import com.example.eafor.socialnetwork.support.AvatarAdapter;
import com.example.eafor.socialnetwork.support.UserData;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment_custom {
TextView text_nick, txt_online, txt_messages, txt_painted, txt_joined_at;
EditText text_edit;
Button btn_upload_photo, btn_save;
CircleImageView circleImageView;
SwipeRefreshLayout swipeLayout;
View view;
Message msg;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile, container, false);

        intiViews();

        AuthActivity.serverStatus.getMainInfo(AuthActivity.nickStr);
        changeText();

        return view;
    }

    private void intiViews() {
        swipeLayout = view.findViewById(R.id.swipe_layout_profile);
        swipeLayout.setOnRefreshListener(refreshListener());
        text_nick=view.findViewById(R.id.profile_nick);
        txt_online=view.findViewById(R.id.profile_last_online);
        txt_messages=view.findViewById(R.id.profile_messages);
        txt_painted=view.findViewById(R.id.profile_painted);
        txt_joined_at=view.findViewById(R.id.profile_joined);
        text_edit=view.findViewById(R.id.profile_data_field);
        btn_upload_photo=view.findViewById(R.id.profile_btn_upload);
        btn_save=view.findViewById(R.id.profile_btn_save);
        circleImageView=view.findViewById(R.id.profile_image);
        btn_save.setOnClickListener(updateChanges());
    }

    void changeText(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                        if(MainActivity.oneUserData!=null||!text_nick.getText().toString().equals("-")){
                            text_nick.setText(MainActivity.oneUserData.nickname);
                            if(MainActivity.oneUserData.status.equals("online")){ txt_online.setText(MainActivity.oneUserData.status);
                                txt_online.setTextColor(getContext().getResources().getColor(R.color.colorGreenOnline)); }else{ txt_online.setText(MainActivity.oneUserData.last_online); }
                            txt_messages.setText(MainActivity.oneUserData.messages);
                            txt_painted.setText(MainActivity.oneUserData.painted);
                            txt_joined_at.setText(MainActivity.oneUserData.joined);
                            String tmp=MainActivity.oneUserData.description.replace("&"," ");
                            text_edit.setText(tmp);
                            AvatarAdapter.setImg(Integer.parseInt(MainActivity.oneUserData.avatar),getContext(), circleImageView);
                        }
            }
        });
    }

    @NonNull
    private SwipeRefreshLayout.OnRefreshListener refreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msg = new Message();
                msg.obj = "/code_update_profile";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        new Thread(()->{
                            try { Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
                            MainActivity.handler.sendMessage(msg);
                        }).start();
                    }
                },1500);
            }
        };
    }

    @NonNull
    private View.OnClickListener updateChanges() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_edit.getText().toString().length()>=400){
                    Toast.makeText(getContext(),"Length > 400 not allowed",Toast.LENGTH_SHORT).show();
                }else{
                    String txt=text_edit.getText().toString();
                    txt=txt.replace(" ","&");
                    AuthActivity.serverStatus.updateUserData(AuthActivity.nickStr,txt);
                    Thread thread = new Thread(()->{
                        try { Thread.sleep(150); } catch (InterruptedException e) { e.printStackTrace(); }
                        AuthActivity.serverStatus.getMainInfo(AuthActivity.nickStr);
                    });
                    thread.start();
                }
            }
        };
    }

}
