package com.pasaribu.store.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.pasaribu.store.Favorite;
import com.pasaribu.store.Home;
import com.pasaribu.store.Shopping;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	
    public TabPagerAdapter(FragmentManager fm) {
    	//TODO Menangani penyajian Tab 
		super(fm);
	}

    
    
	@Override
	public Fragment getItem(int i) {
		switch (i) {
        case 0:
            //Fragement for Home Tab
        	Log.v("Adapter", "Buka Fragment Home");
            return new Home();
        case 1:
           //Fragment for Shopping Tab
        	Log.v("Adapter", "Buka Fragment Shopping");
            return new Shopping();
        case 2:
            //Fragment for Favorite Tab
        	Log.v("Adapter", "Buka Fragment Favorite");
            return new Favorite();
        }
		return null;
		
	}

	@Override
	public int getCount() {
		return 3; //No of Tabs
	}


}