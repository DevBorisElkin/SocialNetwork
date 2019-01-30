package com.example.eafor.socialnetwork.fragments_auth;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {
EditText txtLogin, txtPassword;
Button btn_login;
public static final int ACTION_LOGIN=1;
static TextView serverStatusView;
CheckBox checkBox;


String loginStr, passwordStr;

    public LogFragment() {
        // Required empty public constructor
    }

    public static TextView getServerStatusView(){return serverStatusView;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        txtLogin=view.findViewById(R.id.txt_login);
        txtPassword=view.findViewById(R.id.txt_password);
        btn_login=view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(listener(ACTION_LOGIN));
        serverStatusView=view.findViewById(R.id.txt_server_status);
        checkBox=view.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(checkboxChoice());
        checkBox.setChecked(AuthActivity.checkboxChecked);

        return view;
    }

    @NonNull
    private View.OnClickListener checkboxChoice() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!AuthActivity.checkboxChecked);
                AuthActivity.checkboxChecked=!AuthActivity.checkboxChecked;

                SharedPreferences.Editor mEditor=AuthActivity.mySharedPref.edit();
                mEditor.putBoolean(AuthActivity.KEY_CHECKBOX, checkBox.isChecked());
                mEditor.apply();

            }
        };
    }


    @NonNull
    private View.OnClickListener listener(int a) {
            if(a==ACTION_LOGIN){
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginStr =txtLogin.getText().toString();
                        passwordStr =txtPassword.getText().toString();
                        if(!loginStr.equals("")&&!passwordStr.equals("")){
                            if(checkBox.isChecked()){
                                SharedPreferences.Editor mEditor=AuthActivity.mySharedPref.edit();
                                mEditor.putString(AuthActivity.KEY_LOGIN, loginStr);
                                mEditor.putString(AuthActivity.KEY_PASSWORD, passwordStr);
                                mEditor.apply();
                            }
                            AuthActivity.loginStr=loginStr;
                            AuthActivity.passwordStr=passwordStr;
                            AuthActivity.serverStatus.logIn(txtLogin.getText().toString(),txtPassword.getText().toString());
                        }else{
                            Toast.makeText(getContext(),"Fill in all fields first", Toast.LENGTH_SHORT).show();
                        }

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
