package com.android.paskahlis.anchat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.paskahlis.anchat.adapter.MainViewPagerAdapter;
import com.android.paskahlis.anchat.fragment.ChatFragment;
import com.android.paskahlis.anchat.widget.NavBarViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private NavBarViewPager mViewPager;
    private List<Fragment> mFrags;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    mTextMessage.setText(R.string.title_chat);
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_chat:
                    mTextMessage.setText(R.string.title_chat);
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_setting);
                    mViewPager.setCurrentItem(0);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        mainViewPagerAdapter.addFragment(ChatFragment.newInstance());
        mViewPager = findViewById(R.id.main_view_pager);


        mViewPager.setAdapter(mainViewPagerAdapter);

    }

}
