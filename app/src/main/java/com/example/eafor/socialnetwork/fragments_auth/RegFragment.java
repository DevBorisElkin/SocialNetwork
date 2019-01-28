package com.example.eafor.socialnetwork.fragments_auth;


import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.activities.AuthActivity;


public class RegFragment extends Fragment {
    EditText txtLogin, txtPassword_1,txtPassword_2, txt_nickname;
    Button btn_reg;
    public static TextView serverStatusView;
    public static TextView getServerStatusView(){return serverStatusView;}

    public RegFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reg, container, false);

        txtLogin=view.findViewById(R.id.txt_login);
        txtPassword_1=view.findViewById(R.id.txt_pass1);
        txtPassword_2=view.findViewById(R.id.txt_pass2);
        txt_nickname=view.findViewById(R.id.txt_nick);
        btn_reg=view.findViewById(R.id.btn_reg);
        serverStatusView=view.findViewById(R.id.txt_server_status);

        btn_reg.setOnClickListener(listener());

        return view;
    }

    @NonNull
    private View.OnClickListener listener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtLogin.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Login field is empty", Toast.LENGTH_SHORT).show();
                }else if(txtPassword_1.getText().toString().equals("")||txtPassword_2.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Password field is empty", Toast.LENGTH_SHORT).show();
                } else if(txt_nickname.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Nickname field is empty", Toast.LENGTH_SHORT).show();
                }else if(!txtPassword_1.getText().toString().equals(txtPassword_2.getText().toString())){
                    Toast.makeText(getContext(),"Passwords must be equal", Toast.LENGTH_SHORT).show();
                }else{
                    AuthActivity.getServerStatus().regIn(txtLogin.getText().toString(), txtPassword_1.getText().toString(), txt_nickname.getText().toString());
                }

            }
        };
    }
}
