package com.pasaribu.store;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pasaribu.store.view.DisplayGui;

public class Main extends FragmentActivity {
	
	protected static final String TAG = Main.class.getSimpleName();
	
	private ViewPager Tab;
    private TabPagerAdapter TabAdapter;
	private ActionBar actionBar;
	
	private DisplayGui displayGui;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //TODO Inisialisasi Kelas yang akan digunakan pada main class
        
        displayGui = new DisplayGui(this); //Kelas untuk menampilkan GUI pada aplikasi
                
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        
        Tab = (ViewPager) findViewById(R.id.main_pager);
        
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                       
                    	actionBar = getActionBar();
                    	actionBar.setSelectedNavigationItem(position);                    
                    }
                });
        
        Tab.setAdapter(TabAdapter);
        
        actionBar = getActionBar();
        
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
        	
        	//tab yang tidak di seleksi lagi, klik tab lain
        	@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO lakukan jika tab unselected
        		
        		Log.i(TAG, tab.getPosition() + " Position (UnSelected)");
				restoreIconAwal(tab);
			}
        	
        	//Klik lagi pada tab yang sama
			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				
				Log.i(TAG, tab.getPosition() + " Position (Reselected)");
				
			}

			@Override
			 public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	          
	            Tab.setCurrentItem(tab.getPosition());
	            
	            Log.i(TAG, tab.getPosition() + " Position (Selected)");
	            setTabIconAktif(tab);
	            
	        }

			
			private void setTabIconAktif(android.app.ActionBar.Tab tab) {
				
				int posisi = tab.getPosition();
        		
        		switch (posisi) {
				case 0:
					tab.setIcon(R.drawable.action_button_home_active);
					break;
				case 1:
					tab.setIcon(R.drawable.action_button_belanja_active);
					break;
				case 2:
					tab.setIcon(R.drawable.action_button_favorite_active);
					break;

				default:
					displayGui.displayToast("ERROR, Selected!!!");
					break;
				}
				
			}
			
			private void restoreIconAwal(android.app.ActionBar.Tab tab) {
				
        		int posisi = tab.getPosition();
        		
        		switch (posisi) {
				case 0:
					tab.setIcon(R.drawable.action_button_home_normal);
					break;
				case 1:
					tab.setIcon(R.drawable.action_button_belanja_normal);
					break;
				case 2:
					tab.setIcon(R.drawable.action_button_favorite_normal);
					break;

				default:
					displayGui.displayToast("ERROR, Unselected!!!");
					break;
				}
				
			}

			
			
        };
			
			android.app.ActionBar.Tab menu_home = actionBar.newTab()
					.setIcon(R.drawable.action_button_home_normal)
					.setTabListener(tabListener);
			
			//Add New Tab
			actionBar.addTab(menu_home);
			actionBar.addTab(actionBar.newTab().setIcon(R.drawable.action_button_belanja_normal).setTabListener(tabListener));
			actionBar.addTab(actionBar.newTab().setIcon(R.drawable.action_button_favorite_normal).setTabListener(tabListener));

    
    }
    //end of onCreate
    
    

    //MEMBUAT OPTION
    //dari xml item
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
				
		int id = item.getItemId();
		
		switch (id) {
		case R.id.option_add_barang:
			
			//TODO Membuka activity AddDataBarang tanpa data yang dikirimkan
			Intent i = new Intent(getBaseContext(), AddDataBarang.class);
			startActivity(i);
			
			break;
		case R.id.option_setting:
			
			//TODO Membuka activity AddDataBarang tanpa data yang dikirimkan
			Intent intent_setting = new Intent(getBaseContext(), Setting.class);
			startActivity(intent_setting);

			break;
		default:
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
    


    
}
