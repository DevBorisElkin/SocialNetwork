package com.example.eafor.socialnetwork.fragments_main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.activities.MainActivity;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment_custom {
TextView textView;
boolean flag=true;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

      //  textView=view.findViewById(R.id.txt);
      //  textView.setText("Push me!");
      //  textView.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          AuthActivity.serverStatus.getMainInfo(AuthActivity.nickStr);
      //          Thread thread = new Thread(()->{
      //              while (flag){
      //                  if(MainActivity.getList()!=null){
      //                      flag=false;
      //                      String data=MainActivity.getList().toString();
      //                      changeTezt(data);

      //                      break;
      //                  }
      //              }
      //          });
      //          thread.setDaemon(true);
      //          thread.start();

      //      }
      //  });

        return view;
    }

    void changeTezt(String data){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(data);
            }
        });
    }

}
