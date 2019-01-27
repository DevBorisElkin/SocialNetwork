package com.example.eafor.socialnetwork.fragments_auth;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.activities.MainActivity;
import com.example.eafor.socialnetwork.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {
EditText txtLogin, txtPassword;
Button btn_login;
public static final int ACTION_LOGIN=1;

    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        txtLogin=view.findViewById(R.id.txt_login);
        txtPassword=view.findViewById(R.id.txt_password);
        btn_login=view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(l2());
        return view;
    }

    @NonNull
    private View.OnClickListener l2(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                getActivity().startActivity(intent);

                ((AuthActivity)getActivity()).checkServerStatus();
            }
        };
    }

    @NonNull
    private View.OnClickListener listener(int a) {
            if(a==ACTION_LOGIN&&!txtLogin.getText().equals("")){
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Intent intent = new Intent(getActivity(), MainActivity.class);
                        //startActivity(intent);
                    }
                };
            }else{
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("error");
                    }
                };
            }


    }



}
