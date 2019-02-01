package com.example.eafor.socialnetwork.fragments_main;




import android.os.Bundle;
import android.os.Parcelable;
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
    public static final String BUNDLE_RECYCLER_LAYOUT="key695847";
    public static int scrollPosition=0;
    public static int x, y;
    public static boolean firstLaunchUsers =true;

    public List<UserData> listUserData = new ArrayList<>();

    public static Parcelable savedRecyclerLayoutState;
    public static Bundle savedState = new Bundle();

    public FragmentUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MainActivity.staticUserDataList.size()>0){ listUserData= new ArrayList<>(MainActivity.staticUserDataList); }
        view= inflater.inflate(R.layout.fragment_users, container, false);
        initRecyclerView();

        if (savedInstanceState != null){
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            restoreLayoutManagerPosition();
        }else if(savedState!=null){
            savedRecyclerLayoutState = savedState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            restoreLayoutManagerPosition();
        }
        return view;
    }

    public static void restoreLayoutManagerPosition() {
        if (savedRecyclerLayoutState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    private void initRecyclerView(){


            recyclerView=view.findViewById(R.id.recycler_view);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(listUserData,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

           //recyclerView.onScrolled(x,y);

           //recyclerView.setScrollX(scrollPosition);

    }

    public void getScrollPosition(){

        //recyclerView.getScrollState()
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ recyclerView.getScrollX(), recyclerView.getScrollY()});
        //savedState=outState;
    }






}
