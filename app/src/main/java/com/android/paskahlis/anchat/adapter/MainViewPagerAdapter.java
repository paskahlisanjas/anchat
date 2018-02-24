package com.android.paskahlis.anchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fajar on 22/02/18.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFrags = null;
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFrags = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFrags.get(position);
    }

    public void addFragment(Fragment fragment){
        mFrags.add(fragment);
    }
    @Override
    public int getCount() {
        return mFrags.size();
    }
}
