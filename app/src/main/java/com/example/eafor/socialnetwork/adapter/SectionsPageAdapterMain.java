package com.example.eafor.socialnetwork.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.eafor.socialnetwork.fragments_main.FragmentChat;
import com.example.eafor.socialnetwork.fragments_main.FragmentMessage;
import com.example.eafor.socialnetwork.fragments_main.FragmentProfile;
import com.example.eafor.socialnetwork.fragments_main.FragmentUsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class SectionsPageAdapterMain extends FragmentPagerAdapter {
    private Map<Integer, String> mFragmentTags;
    private FragmentManager fragmentManager;
    private Context context;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> myFragmentTitleList = new ArrayList<>();

    public SectionsPageAdapterMain(FragmentManager fm, Context context) {
        super(fm);
        fragmentManager=fm;
        mFragmentTags=new HashMap<>();
        this.context=context;
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        myFragmentTitleList.add(title);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return new FragmentUsers();
            case 1: return new FragmentMessage();
            case 2: return new FragmentChat();
            case 3: return new FragmentProfile();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        Object obj = super.instantiateItem(container, position);
        if(obj instanceof Fragment){
            Fragment f = (Fragment)obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);
        if(tag==null)return null;
        return fragmentManager.findFragmentByTag(tag);
    }
}
