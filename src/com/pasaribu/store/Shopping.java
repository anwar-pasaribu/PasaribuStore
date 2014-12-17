package com.pasaribu.store;

import com.pasaribu.store.adapter.SupplierListAdapter;
import com.pasaribu.store.control.AppsController;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Shopping extends Fragment {
	private final String TAG = Shopping.class.getSimpleName();
	
	//Widget utk di tampilkan
	private View viewShopping; //view yg akan dikembalikan ke Main.java
	private ListView lv_listSupplier, lv_listShopping;
	private Button btn_addShoppingItem, btn_cartStatus;
	
	//Application Controller, utk memperoleh data bersama
	private AppsController aController;
	
	//Adapter utk Supplier
	private SupplierListAdapter supplierListAdapter;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		Log.i(TAG, "onCreateView");
		//Inflate layout utk dikembalikan ke main activity
		viewShopping = inflater.inflate(R.layout.activity_shopping, container, false);
        
		//Application Controller, berisi data ter-share utk semua activity.
		aController = (AppsController) getActivity().getApplicationContext();
		
        //Inisialisasi seluruh widget
		initilizeWidget();
		
		
		//Mengirm data ke adapter list supplier
		Log.i(TAG, "Supplier size : " + aController.getList_supplier().size());
		supplierListAdapter = new SupplierListAdapter(getActivity(), R.layout.list_item_supplier, aController.getList_supplier());
		
		//Mengisi data pada listView Supplier
		pupulateLvSupplier();
        
        return viewShopping;
	}
	
	/**
	 * Mengisi data pada ListView Supplier. 
	 */
	private void pupulateLvSupplier() {
		Log.v(TAG, "pupulateLvSupplier");
		lv_listSupplier.setSelector(R.drawable.list_secondary_background);
		lv_listSupplier.setAdapter(supplierListAdapter);
		lv_listSupplier.setTextFilterEnabled(true);
		lv_listSupplier.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.i(TAG, position + " selected");
				
			}
		});
	}

	private void initilizeWidget() {
		Log.v(TAG, "Inisialisasi Widget");
		
		lv_listSupplier = (ListView) viewShopping.findViewById(R.id.list_supplier);
		lv_listShopping = (ListView) viewShopping.findViewById(R.id.list_shopping);
		
		btn_addShoppingItem = (Button) viewShopping.findViewById(R.id.btn_addShoppingItem);
		btn_cartStatus = (Button) viewShopping.findViewById(R.id.btn_cartStatus);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
	}	
	
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i(TAG, "onDestroyView");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}
}
