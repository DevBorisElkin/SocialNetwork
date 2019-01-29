package com.example.eafor.socialnetwork.fragments_main;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.activities.MainActivity;
import com.example.eafor.socialnetwork.adapter.RecyclerViewAdapter;
import com.example.eafor.socialnetwork.adapter.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUsers extends Fragment {
    TextView textView;
    boolean flag_local=true;
    View view;

    public List<UserData> listUserData = new ArrayList<>();

    public FragmentUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MainActivity.staticUserDataList.size()>0){
            listUserData=MainActivity.staticUserDataList;
        }
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_users, container, false);

       // AuthActivity.serverStatus.getUsers();
        initImageBitmaps();
        return view;
    }

    private void initImageBitmaps(){
                if(MainActivity.staticUserDataList.size()>0){
                    listUserData=MainActivity.staticUserDataList;
                    initRecyclerView();
                }


    }

    private void initRecyclerView(){
        RecyclerView recyclerView=view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(listUserData,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}