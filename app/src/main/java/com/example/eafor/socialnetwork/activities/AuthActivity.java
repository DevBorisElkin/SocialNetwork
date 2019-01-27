package com.example.eafor.socialnetwork.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.adapter.SectionsPageAdapter;
import com.example.eafor.socialnetwork.fragments_auth.LogFragment;
import com.example.eafor.socialnetwork.fragments_auth.RegFragment;

public class AuthActivity extends AppCompatActivity {
    SectionsPageAdapter mSectionsPageAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container_auth);
        setupViewPager(mViewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LogFragment(), "Log in");
        adapter.addFragment(new RegFragment(), "Registration");
        viewPager.setAdapter(adapter);
    }
}
