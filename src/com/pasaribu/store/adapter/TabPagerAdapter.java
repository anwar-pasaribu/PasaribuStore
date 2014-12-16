package com.pasaribu.store.adapter;

import com.pasaribu.store.Favorite;
import com.pasaribu.store.Home;
import com.pasaribu.store.Shopping;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
    public TabPagerAdapter(FragmentManager fm) {
    	//TODO Menangani penyajian Tab 
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            //Fragement for Android Tab
            return new Home();
        case 1:
           //Fragment for Ios Tab
            return new Shopping();
        case 2:
            //Fragment for Windows Tab
            return new Favorite();
        }
		return null;
		
	}

	@Override
	public int getCount() {
		return 3; //No of Tabs
	}


    }