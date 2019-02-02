package com.example.eafor.socialnetwork.fragments_main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
boolean flag=true;
View view;

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
        Thread thread = new Thread(()->{
            while (true){
                if(MainActivity.oneUserData!=null){
                    if(MainActivity.oneUserData.nickname.equals(AuthActivity.nickStr)){
                        changeText(MainActivity.oneUserData);
                        break;
                    }
                }
            }
        });
        thread.start();

        return view;
    }

    private void intiViews() {
        text_nick=view.findViewById(R.id.profile_nick);
        txt_online=view.findViewById(R.id.profile_last_online);
        txt_messages=view.findViewById(R.id.profile_messages);
        txt_painted=view.findViewById(R.id.profile_painted);
        txt_joined_at=view.findViewById(R.id.profile_joined);
        text_edit=view.findViewById(R.id.profile_data_field);
        btn_upload_photo=view.findViewById(R.id.profile_btn_upload);
        btn_save=view.findViewById(R.id.profile_btn_save);
        circleImageView=view.findViewById(R.id.profile_image);
    }

    void changeText(UserData userData){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text_nick.setText(userData.nickname);
                if(userData.status.equals("online")){ txt_online.setText(userData.status); }else{ txt_online.setText(userData.last_online); }
                txt_messages.setText(userData.messages);
                txt_painted.setText(userData.painted);
                txt_joined_at.setText(userData.joined);
                String tmp=userData.description.replace("&"," ");
                text_edit.setText(tmp);
                AvatarAdapter.setImg(Integer.parseInt(userData.avatar),getContext(), circleImageView);
                MainActivity.oneUserData=null;
            }
        });
    }

}
