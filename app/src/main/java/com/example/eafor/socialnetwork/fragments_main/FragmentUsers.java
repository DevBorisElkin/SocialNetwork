package com.example.eafor.socialnetwork.fragments_main;




import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.activities.AuthActivity;
import com.example.eafor.socialnetwork.activities.MainActivity;
import com.example.eafor.socialnetwork.adapter.RecyclerViewAdapter;
import com.example.eafor.socialnetwork.support.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUsers extends Fragment_custom{
    TextView textView;
    boolean flag_local=true;
    View view;
    public static RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout;
    Message msg;

    public List<UserData> listUserData = new ArrayList<>();

    public FragmentUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MainActivity.staticUserDataList.size()>0){
            listUserData= new ArrayList<>(MainActivity.staticUserDataList);
        }
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_users, container, false);
        init();
        return view;
    }

    private void init(){
            swipeLayout = view.findViewById(R.id.swipe_layout);
            swipeLayout.setOnRefreshListener(refreshListener());
            recyclerView=view.findViewById(R.id.recycler_view);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(listUserData,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @NonNull
    private SwipeRefreshLayout.OnRefreshListener refreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msg = new Message();
                msg.obj = "/code_update_users";
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

    // @Override
   // public void update(){
   //     listUserData=null;
   //     listUserData=MainActivity.staticUserDataList;
   //     init();
   // }

}
