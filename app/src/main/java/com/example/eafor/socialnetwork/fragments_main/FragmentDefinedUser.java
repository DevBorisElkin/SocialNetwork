package com.example.eafor.socialnetwork.fragments_main;

import static com.example.eafor.socialnetwork.activities.MainActivity.*;

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
import com.example.eafor.socialnetwork.support.TimeManager;
import com.example.eafor.socialnetwork.support.UserData;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDefinedUser extends Fragment_custom {
    TextView text_nick, txt_online, txt_messages, txt_painted, txt_joined_at;
    TextView text_edit;
    CircleImageView circleImageView;
    SwipeRefreshLayout swipeLayout;
    View view;
    Message msg;

    public FragmentDefinedUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_defined_user, container, false);

        intiViews();
        changeText();

        return view;
    }

    private void intiViews() {
        definedUserOpened=true;
        swipeLayout = view.findViewById(R.id.swipe_layout_profile);
        swipeLayout.setOnRefreshListener(refreshListener());
        text_nick=view.findViewById(R.id.profile_nick);
        txt_online=view.findViewById(R.id.profile_last_online);
        txt_messages=view.findViewById(R.id.profile_messages);
        txt_painted=view.findViewById(R.id.profile_painted);
        txt_joined_at=view.findViewById(R.id.profile_joined);
        text_edit=view.findViewById(R.id.profile_data_field);
        circleImageView=view.findViewById(R.id.profile_image);
    }

    void changeText(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    text_nick.setText(staticUserDataList.get(chosenUserId).nickname);
                    if(staticUserDataList.get(chosenUserId).status.equals("online")){ txt_online.setText(staticUserDataList.get(chosenUserId).status);
                        txt_online.setTextColor(getContext().getResources().getColor(R.color.colorGreenOnline)); }else{ txt_online.setText(TimeManager.parseString(staticUserDataList.get(chosenUserId).last_online)); }
                    txt_messages.setText(staticUserDataList.get(chosenUserId).messages);
                    txt_painted.setText(staticUserDataList.get(chosenUserId).painted);
                    txt_joined_at.setText(staticUserDataList.get(chosenUserId).joined);
                    String tmp=staticUserDataList.get(chosenUserId).description.replace("&"," ");
                    text_edit.setText(tmp);
                    AvatarAdapter.setImg(Integer.parseInt(staticUserDataList.get(chosenUserId).avatar),getContext(), circleImageView);

            }
        });
    }

    @NonNull
    private SwipeRefreshLayout.OnRefreshListener refreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msg = new Message();
                msg.obj = "/code_update_chosen_user";
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




}
